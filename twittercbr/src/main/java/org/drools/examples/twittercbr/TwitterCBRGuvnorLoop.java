/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.examples.twittercbr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.drools.ClockType;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.agent.impl.PrintStreamSystemEventListener;
import org.drools.conf.EventProcessingOption;
import org.drools.event.knowledgeagent.AfterChangeSetAppliedEvent;
import org.drools.event.knowledgeagent.AfterChangeSetProcessedEvent;
import org.drools.event.knowledgeagent.AfterResourceProcessedEvent;
import org.drools.event.knowledgeagent.BeforeChangeSetAppliedEvent;
import org.drools.event.knowledgeagent.BeforeChangeSetProcessedEvent;
import org.drools.event.knowledgeagent.BeforeResourceProcessedEvent;
import org.drools.event.knowledgeagent.KnowledgeAgentEventListener;
import org.drools.event.knowledgeagent.KnowledgeBaseUpdatedEvent;
import org.drools.event.knowledgeagent.ResourceCompilationFailedEvent;
import org.drools.event.rule.DebugKnowledgeAgentEventListener;
import org.drools.event.rule.DefaultKnowledgeAgentEventListener;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * TwitterCBRGuvnorLoop : Get resources from Guvnor via ChangeSet. Good to test
 * dynamic update.
 */
public class TwitterCBRGuvnorLoop {

    private static KnowledgeAgent kagent;
    private static StatefulKnowledgeSession ksession;

    /**
     * Main method
     */
    public static void main(String[] args) throws TwitterException, IOException {

        startKnowledgeAgent();

        while (true) {

            // Get a knowledge base from knowledge agent
            final KnowledgeBase kbase = kagent.getKnowledgeBase();
            System.out.println(kbase);

            // Creates a knowledge session
            ksession = createKnowledgeSession(kbase);

            // Gets the stream entry point
            final WorkingMemoryEntryPoint ep = ksession
                    .getWorkingMemoryEntryPoint("twitter");

            List<Status> tweetList = new ArrayList<Status>();
            ksession.setGlobal("tweetList", tweetList);
            // Connects to the twitter stream and register the listener
            StatusListener listener = new TwitterStatusListener(ep);
            TwitterStream twitterStream = new TwitterStreamFactory()
                    .getInstance();
            twitterStream.addListener(listener);
            twitterStream.sample();

            // Starts to fire rules in Drools Fusion

            // ksession will be halted by KnowledgeAgentEventListener
            ksession.fireUntilHalt();

            twitterStream.cleanUp();
            ksession.dispose();

            System.out.println("next session!");

        }

        // stopKnowledgeAgent();
    }

    /**
     * Initialize and start a knowledge agent
     */
    private static void startKnowledgeAgent() {

        // create a knowledge agent with change set

        KnowledgeBaseConfiguration kbaseConf = KnowledgeBaseFactory
                .newKnowledgeBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConf);
        KnowledgeAgentConfiguration kagentConf = KnowledgeAgentFactory
                .newKnowledgeAgentConfiguration();
        kagent = KnowledgeAgentFactory.newKnowledgeAgent("MyAgent", kbase,
                kagentConf);

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

    }

    private static void stopKnowledgeAgent() {
        ResourceFactory.getResourceChangeScannerService().stop();
        ResourceFactory.getResourceChangeNotifierService().stop();
        kagent.monitorResourceChangeEvents(false);
    }

    /**
     * Creates a Drools Stateful Knowledge Session
     */
    private static StatefulKnowledgeSession createKnowledgeSession(
            final KnowledgeBase kbase) {
        final KnowledgeSessionConfiguration ksconf = KnowledgeBaseFactory
                .newKnowledgeSessionConfiguration();
        ksconf.setOption(ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId()));
        final StatefulKnowledgeSession ksession = kbase
                .newStatefulKnowledgeSession(ksconf, null);
        return ksession;
    }

    static class HaltKnowledgeAgentEventListener extends
            DefaultKnowledgeAgentEventListener {
        public void afterChangeSetApplied(AfterChangeSetAppliedEvent event) {
            // If the changeset is updated, halt the ksession and trigger the loop.
            if (ksession != null) {
                ksession.halt();
            }
        }
    }

    static {
        // disable twitter4j log
        // System.setProperty("twitter4j.loggerFactory",
        // "twitter4j.internal.logging.NullLoggerFactory");
    }

}
