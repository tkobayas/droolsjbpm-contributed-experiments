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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;

import org.drools.ClockType;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.event.rule.DebugKnowledgeAgentEventListener;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionPseudoClock;

import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * TwitterCBR
 */
public class TwitterCBRGuvnorOffline {
    public static final boolean disableLog = true;
    
	private static KnowledgeAgent kagent;


    /**
     * Main method
     */
    public static void main(String[] args) throws TwitterException, IOException{
//        if( args.length == 0 ) {
//            System.out.println("Please provide the rules file name to load.");
//            System.exit( 0 );
//        }
    	
    	startKnowledgeAgent();
        
        // Get a knowledge base from knowledge agent
        final KnowledgeBase kbase = kagent.getKnowledgeBase();
        
        final KnowledgeSessionConfiguration ksconf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        ksconf.setOption( ClockTypeOption.get( ClockType.PSEUDO_CLOCK.getId() ) );
        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession( ksconf, null );
		System.out.println(ksession);
        
        // Gets the stream entry point 
        final WorkingMemoryEntryPoint ep = ksession.getWorkingMemoryEntryPoint( "twitter" );
        System.out.println(ep);
        
        new Thread( new Runnable() {
            @Override
            public void run() {
                // Starts to fire rules in Drools Fusion
                ksession.fireUntilHalt();
            }
        }).start();

        // reads the status stream and feeds it to the engine 
        feedEvents( ksession,
                    ep );
        
        ksession.halt();
    }
    
    /**
     * Feed events from the stream to the engine
     * @param ksession
     * @param ep
     */
    private static void feedEvents( final StatefulKnowledgeSession ksession,
                                    final WorkingMemoryEntryPoint ep ) {
        try {
            StatusListener listener = new TwitterStatusListener( ep );
            ObjectInputStream in = new ObjectInputStream( new FileInputStream( "src/main/resources/twitterstream.dump" ) );
            SessionPseudoClock clock = ksession.getSessionClock();
            
            for( int i = 0; ; i++ ) {
                try {
                    // Read an event
                    Status st = (Status) in.readObject();
                    // Using the pseudo clock, advance the clock
                    clock.advanceTime( st.getCreatedAt().getTime() - clock.getCurrentTime(), TimeUnit.MILLISECONDS );
                    // call the listener
                    listener.onStatus( st );
                } catch( IOException ioe ) {
                    break;
                }
            }
            in.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize and start a knowledge agent
     */
    private static void startKnowledgeAgent() {
    	
		// create a knowledge agent with change set
		KnowledgeAgentConfiguration aconf = KnowledgeAgentFactory
				.newKnowledgeAgentConfiguration();
		kagent = KnowledgeAgentFactory.newKnowledgeAgent("MyAgent", aconf);
		kagent.addEventListener(new DebugKnowledgeAgentEventListener());
		kagent.applyChangeSet(ResourceFactory
				.newClassPathResource("ChangeSet.xml"));

		// start ResourceChangeScanner
//		ResourceChangeScannerConfiguration sconf = ResourceFactory
//				.getResourceChangeScannerService()
//				.newResourceChangeScannerConfiguration();
//		sconf.setProperty("drools.resource.scanner.interval", "10");
//		ResourceFactory.getResourceChangeScannerService().configure(sconf);
//		ResourceFactory.getResourceChangeScannerService().start();
//		ResourceFactory.getResourceChangeNotifierService().start();

        return;
    }
    
    private static void stopKnowledgeAgent() {
		ResourceFactory.getResourceChangeScannerService().stop();
		ResourceFactory.getResourceChangeNotifierService().stop();
		kagent.monitorResourceChangeEvents(false);
    }



    static {
        // disable twitter4j log
//        System.setProperty( "twitter4j.loggerFactory", "twitter4j.internal.logging.NullLoggerFactory" );
    }
    
}
