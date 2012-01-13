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

import org.drools.ClockType;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.agent.impl.PrintStreamSystemEventListener;
import org.drools.conf.EventProcessingOption;
import org.drools.event.rule.DebugKnowledgeAgentEventListener;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * TwitterCBRGuvnor :
 * Get resources from Guvnor via ChangeSet
 */
public class TwitterCBRGuvnor {

    private static KnowledgeAgent kagent;

    /**
     * Main method
     */
    public static void main(String[] args) throws TwitterException, IOException {

        // CAUTION!! This program keeps using one knowledge session.
        // Hence you wouldn't get a knowledge base update even if
        // ResourceChangeScanner is enabled.

        startKnowledgeAgent();

        // Get a knowledge base from knowledge agent
        final KnowledgeBase kbase = kagent.getKnowledgeBase();

        // Creates a knowledge session
        final StatefulKnowledgeSession ksession = createKnowledgeSession(kbase);

        // Gets the stream entry point
        final WorkingMemoryEntryPoint ep = ksession
                .getWorkingMemoryEntryPoint("twitter");

        // Connects to the twitter stream and register the listener
        StatusListener listener = new TwitterStatusListener(ep);
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        twitterStream.sample();

        // Starts to fire rules in Drools Fusion
        ksession.fireUntilHalt();
    }

    /**
     * Initialize and start a knowledge agent
     */
    private static void startKnowledgeAgent() {

        KnowledgeBaseConfiguration kbaseConf = KnowledgeBaseFactory
                .newKnowledgeBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConf);
        kagent = KnowledgeAgentFactory.newKnowledgeAgent("MyAgent", kbase);
        kagent.applyChangeSet(ResourceFactory
                .newClassPathResource("ChangeSet.xml"));

        return;
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

    static {
        // disable twitter4j log
//        System.setProperty("twitter4j.loggerFactory",
//                "twitter4j.internal.logging.NullLoggerFactory");
    }
}
