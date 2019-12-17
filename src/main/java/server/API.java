package server;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.util.List;

import static server.FileHelper.*;
import static spark.Spark.*;

public class API {
    private Twitter twitter = TwitterFactory.getSingleton();

    public static void main(String[] args) {
        API api = new API();
        api.authenticate();
        api.startServer();
    }

    private void startServer() {
        port(5000);
        get("/", (req, res) -> {
            List<Status> statuses = twitter.getUserTimeline("realDonaldTrump");

            return statuses.get(0).toString();
        });
    }

    public void authenticate() {
        String[] apiKeys = getApiKeys();
        twitter.setOAuthConsumer(apiKeys[0], apiKeys[1]);
        AccessToken token = readToken();
        if (token == null) {
            try {

                RequestToken requestToken = twitter.getOAuthRequestToken();
                AccessToken accessToken = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
                    String pin = br.readLine();
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken();
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                writeToken(accessToken);

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            twitter.setOAuthAccessToken(token);
        }
    }
}
