package org.drools.examples.twittercbr.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drools.ClockType;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.agent.impl.PrintStreamSystemEventListener;
import org.drools.event.knowledgeagent.AfterChangeSetAppliedEvent;
import org.drools.event.rule.DebugKnowledgeAgentEventListener;
import org.drools.event.rule.DefaultKnowledgeAgentEventListener;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionPseudoClock;

import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * This is a command servlet which triggers kagent/ksession start/stop and
 * responds tweets to Ajax polling. NOTE : This servlet expects only single
 * threaded access for demo purpose and implemented in a dirty
 * static/synchronized way.
 * This is offline version so that you don't have to rely on twitter access.
 */
public class TwitterServletOffline extends HttpServlet {

    private static int MAX_RESPONSE_SIZE = 20;

    // Only one ksession is executed in this sample servlet
    private static KnowledgeAgent kagent;
    private static StatefulKnowledgeSession ksession;

    private static List<Status> tweetList = new ArrayList<Status>();
    private static boolean kagentRunning = false;
    private static boolean ksessionRunning = false;
    
    private static boolean feedStop = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("doGet");

        String result = processCommand(req);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println(result);
        writer.flush();
    }

    private synchronized String processCommand(HttpServletRequest req)
            throws ServletException {
        // req.setCharacterEncoding("UTF-8");

        StringBuffer buf = new StringBuffer();

        String cmd = req.getParameter("cmd");

        System.out.println("processCommand : cmd = " + cmd
                + ", kagentRunning = " + kagentRunning + ", ksessionRunning = "
                + ksessionRunning);

        if (cmd == null) {
            buf.append("Not Supported : cmd = " + cmd);
        } else if (cmd.equals("start") && !kagentRunning && !ksessionRunning) {
            startKnowledgeAgent();
            run();
            buf.append("kagent and ksession is running");
        } else if (cmd.equals("refresh") && kagentRunning && ksessionRunning) {
            stopKnowledgeSession();
            run();
            buf.append("A new ksession is created and running");
        } else if (cmd.equals("stop") && kagentRunning && ksessionRunning) {
            String result = poll();
            buf.append("kagent and ksession stopped<br>");
            buf.append("-------------------------<br>");
            buf.append(result);
            stopKnowledgeSession();
            stopKnowledgeAgent();
        } else if (cmd.equals("poll") && kagentRunning && ksessionRunning) {
            String result = poll();
            buf.append(result);
            // buf.append("-------------------------");
        } else {
            buf.append("Not Supported : cmd = " + cmd + ", kagentRunning = "
                    + kagentRunning + ", ksessionRunning = " + ksessionRunning);
        }
        return buf.toString();
    }

    private String poll() {
        System.out.println("poll!");

        StringBuffer buf = new StringBuffer();
        int start = tweetList.size() < MAX_RESPONSE_SIZE ? 0 : tweetList.size()
                - MAX_RESPONSE_SIZE;
        List<Status> outList = new ArrayList<Status>(tweetList.subList(start,
                tweetList.size()));

        for (int i = 0; i < outList.size(); i++) {
            Status status = outList.get(i);
            buf.append("<span id='screenName'>"
                    + status.getUser().getScreenName() + "</span> "
                    + status.getText() + "<br>");
        }

        return buf.toString();
    }

    private static void stopKnowledgeSession() {
        if (!ksessionRunning) {
            throw new RuntimeException("KSession is not running!");
        }

        feedStop = true;
        try {Thread.sleep(10);} catch (InterruptedException e) {}
        
        ksession.halt();
        ksession.dispose();
        tweetList = new ArrayList<Status>();

        ksessionRunning = false;
        System.out.println("KSession is stopped");

    }

    private static void run() {

        if (ksessionRunning) {
            throw new RuntimeException("KSession is running!");
        }

        // Get a knowledge base from knowledge agent
        final KnowledgeBase kbase = kagent.getKnowledgeBase();
        System.out.println(kbase);

        // Creates a knowledge session
        ksession = createKnowledgeSession(kbase);

        tweetList = new ArrayList<Status>();
        ksession.setGlobal("tweetList", tweetList);

        // Gets the stream entry point
        final WorkingMemoryEntryPoint ep = ksession
                .getWorkingMemoryEntryPoint("twitter");

        new Thread( new Runnable() {
            
            public void run() {
                // Starts to fire rules in Drools Fusion
                feedEvents( ksession, ep );
            }
        }).start();

        // Starts to fire rules in Drools Fusion
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                ksession.fireUntilHalt();
            }
        }).start();

        ksessionRunning = true;
        System.out.println("KSession is running");
    }

    private static void startKnowledgeAgent() {

        // create a knowledge agent with change set
        KnowledgeAgentConfiguration aconf = KnowledgeAgentFactory
                .newKnowledgeAgentConfiguration();
        kagent = KnowledgeAgentFactory.newKnowledgeAgent("MyAgent", aconf);
        kagent.addEventListener(new DebugKnowledgeAgentEventListener());
        kagent.addEventListener(new HaltKnowledgeAgentEventListener());

        kagent.applyChangeSet(ResourceFactory
                .newClassPathResource("ChangeSet.xml"));

        // start ResourceChangeScanner
        ResourceChangeScannerConfiguration sconf = ResourceFactory
                .getResourceChangeScannerService()
                .newResourceChangeScannerConfiguration();
        sconf.setProperty("drools.resource.scanner.interval", "20");
        ResourceFactory.getResourceChangeScannerService().configure(sconf);
        ResourceFactory.getResourceChangeScannerService()
                .setSystemEventListener(new PrintStreamSystemEventListener());
        ResourceFactory.getResourceChangeScannerService().start();
        ResourceFactory.getResourceChangeNotifierService().start();

        kagentRunning = true;
        System.out.println("KAgent is running!");

        return;
    }

    private static void stopKnowledgeAgent() {

        ResourceFactory.getResourceChangeScannerService().stop();
        ResourceFactory.getResourceChangeNotifierService().stop();
        kagent.monitorResourceChangeEvents(false);

        kagentRunning = false;
    }

    private static StatefulKnowledgeSession createKnowledgeSession(
            final KnowledgeBase kbase) {
        final KnowledgeSessionConfiguration ksconf = KnowledgeBaseFactory
                .newKnowledgeSessionConfiguration();
        ksconf.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));

        final StatefulKnowledgeSession ksession = kbase
                .newStatefulKnowledgeSession(ksconf, null);
        return ksession;
    }

    static class HaltKnowledgeAgentEventListener extends
            DefaultKnowledgeAgentEventListener {
        public void afterChangeSetApplied(AfterChangeSetAppliedEvent event) {
            // If the changeset is updated, halt the ksession and re-run
            if (ksessionRunning) {
                try {
                    stopKnowledgeSession();
                    run();
                } catch (Exception e) {
                    // maybe fatal error
                    e.printStackTrace();
                }
            }
        }
    }

    private static void feedEvents(final StatefulKnowledgeSession ksession,
            final WorkingMemoryEntryPoint ep) {
        try {
            
            StatusListener listener = new TwitterStatusListener(ep);
            ObjectInputStream in = new ObjectInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "twitterstream.dump"));
            SessionPseudoClock clock = ksession.getSessionClock();

            for (int i = 0;; i++) {
                try {
                    // Read an event
                    Status st = (Status) in.readObject();
                    // Using the pseudo clock, advance the clock
                    clock.advanceTime(
                            st.getCreatedAt().getTime()
                                    - clock.getCurrentTime(),
                            TimeUnit.MILLISECONDS);
                    // call the listener
                    listener.onStatus(st);
                    
                    Thread.sleep(5); // just to look fancy in ajax web app
                    
                    if (feedStop) {
                        break;
                    }
                    
                } catch (IOException ioe) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        feedStop = false;
    }
}
