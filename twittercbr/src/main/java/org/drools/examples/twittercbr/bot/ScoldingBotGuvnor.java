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

package org.drools.examples.twittercbr.bot;

import java.io.IOException;

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
import org.drools.event.rule.DebugKnowledgeAgentEventListener;
import org.drools.event.rule.DefaultKnowledgeAgentEventListener;
import org.drools.examples.twittercbr.TwitterStatusListener;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class ScoldingBotGuvnor {

    private static KnowledgeAgent kagent;
    private static StatefulKnowledgeSession ksession;
    /**
     * Main method
     */
    public static void main(String[] args) throws TwitterException, IOException {

        // Get screenNames of followers
        Twitter twitter = new TwitterFactory().getInstance();
        long[] followersIDs = twitter.getFollowersIDs("scolding_bot", -1)
                .getIDs();
        String[] followersScreenNames = new String[followersIDs.length];
        for (int i = 0; i < followersIDs.length; i++) {
            followersScreenNames[i] = twitter.showUser(followersIDs[i])
                    .getScreenName();
        }

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

            // Connects to the twitter stream and register the listener

            StatusListener listener = new TwitterStatusListener(ep);
            TwitterStream twitterStream = new TwitterStreamFactory()
                    .getInstance();
            twitterStream.addListener(listener);
            FilterQuery query = new FilterQuery();
            query.follow(followersIDs); // to get followers tweets
            // query.track(followersScreenNames); // to get tweets which mention followers
            twitterStream.filter(query);

            // Starts to fire rules in Drools Fusion
            // ksession will be halted by KnowledgeAgentEventListener
            ksession.fireUntilHalt();

            twitterStream.cleanUp();
            ksession.dispose();

            System.out.println("next session!");
        }
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
                .newClassPathResource("ChangeSetBot.xml"));

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
            // If the changeset is updated, halt the ksession and trigger the
            // loop.
            if (ksession != null) {
                ksession.halt();
            }
        }
    }

    static {
        // disable twitter4j log
//        System.setProperty("twitter4j.loggerFactory",
//                "twitter4j.internal.logging.NullLoggerFactory");
    }

}
