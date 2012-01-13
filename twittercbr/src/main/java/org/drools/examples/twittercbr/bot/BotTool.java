package org.drools.examples.twittercbr.bot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class BotTool {
    
    private static Set<String> textSet = Collections.synchronizedSet(new HashSet<String>());

    public static void reply(Status status, String textBody) {
        
        Twitter twitter = new TwitterFactory().getInstance();
        String text = "@" + status.getUser().getScreenName() + " " + textBody;
        
        // check duplicate tweet to avoid block
        for (int i = 0; i < 10; i++) {
            if (textSet.contains(text)) {
                text += ".";
            }
        }
        if (textSet.contains(text)) {
            System.out.println("trying to post the same tweet 10 times. Stop processing");
            return;
        }
        textSet.add(text);
        
        System.out.println("replying -> " + text);
        StatusUpdate update = new StatusUpdate(text);
        update.setInReplyToStatusId(status.getId());
        try {
            twitter.updateStatus(update);
        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
