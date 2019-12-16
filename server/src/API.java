import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import java.util.List;

import static spark.Spark.*;

public class API {

    public static void main(String[] args) {

        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("Ynnpw8tVOtV4UX8WQ0iWUYb9O", "PFmbLqmpuSCVdAjHG7QFUtEMBR5ZbeXC25U43iYQvtcyozlUDn");
        try {
            RequestToken requestToken = twitter.getOAuthRequestToken();
            System.out.println(requestToken);

            List<Status> statuses = twitter.getHomeTimeline();
            for (Status s: statuses) {
                System.out.println(s.toString());
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        port(5000);
        get("/", (req, res) -> "He  llo World");
    }
}
