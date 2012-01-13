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
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.examples.twittercbr.TwitterStatusListener;
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

public class ScoldingBot {
    public static final boolean disableLog = true;

    /**
     * Main method
     */
    public static void main(String[] args) throws TwitterException, IOException{
        
        // Get screenNames of followers
        Twitter twitter = new TwitterFactory().getInstance();
        long[] followersIDs = twitter.getFollowersIDs("scolding_bot", -1).getIDs();
        String[] followersScreenNames = new String[followersIDs.length];
        for (int i = 0; i < followersIDs.length; i++) {
            followersScreenNames[i] = twitter.showUser(followersIDs[i]).getScreenName();
        }
        
        
        // Creates a knowledge base
        final KnowledgeBase kbase = createKnowledgeBase( "twitterBot2.drl" );
        
        // Creates a knowledge session
        final StatefulKnowledgeSession ksession = createKnowledgeSession( kbase );
        
        // Gets the stream entry point 
        final WorkingMemoryEntryPoint ep = ksession.getWorkingMemoryEntryPoint( "twitter" );
        
        // Connects to the twitter stream and register the listener 

        StatusListener listener = new TwitterStatusListener( ep );
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener( listener );
        FilterQuery query = new FilterQuery();
        query.follow(followersIDs);
        //query.track(followersScreenNames);
        twitterStream.filter(query);
        
        // Starts to fire rules in Drools Fusion
        ksession.fireUntilHalt();
    }

    /**
     * Creates a Drools KnowledgeBase and adds the given rules file into it
     */
    private static KnowledgeBase createKnowledgeBase( final String rulesFile ) {
        // Parses and compiles the rules file
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add( ResourceFactory.newClassPathResource( rulesFile ), ResourceType.DRL );
        
        // Configures the Stream mode
        KnowledgeBaseConfiguration conf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        conf.setOption( EventProcessingOption.STREAM );
        
        // Creates the knowledge base and adds the rules
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase( conf );
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );
        return kbase;
    }

    /**
     * Creates a Drools Stateful Knowledge Session
     */
    private static StatefulKnowledgeSession createKnowledgeSession( final KnowledgeBase kbase ) {
        final KnowledgeSessionConfiguration ksconf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        ksconf.setOption( ClockTypeOption.get( ClockType.REALTIME_CLOCK.getId() ) );
        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession( ksconf, null );
        return ksession;
    }

    static {
        // disable twitter4j log
//        System.setProperty( "twitter4j.loggerFactory", "twitter4j.internal.logging.NullLoggerFactory" );
    }
    
}
