package server;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Base64;

import static server.APIHelper.*;

import server.APIHelper.Key;

import static spark.Spark.*;

public class API {
    private String bearerToken;
    private Gson gson = new Gson();

    public API() {

    }

    public static void main(String[] args) {
        API api = new API();
        api.authenticate();
        api.startServer();
    }

    private void startServer() {

        port(5000);

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
        });

        get("/", (req, res) -> {
            return getTweet();
        });
    }

    public void authenticate() {
        TwitterToken accessToken = readToken();
        if (accessToken == null) {
            String consumer_key = URLEncoder.encodeUTF8(getKey(Key.API_key));
            String consumer_secret = URLEncoder.encodeUTF8(getKey(Key.API_secret_key));
            String bearer_token = Base64.getEncoder().encodeToString(String.format("%s:%s", consumer_key, consumer_secret).getBytes());

            HttpResponse request = Unirest.post("https://api.twitter.com/oauth2/token")
                    .header("Authorization", "Basic " + bearer_token)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .header("accept", "application/json")
                    .body("grant_type=client_credentials").asJson();

            System.out.println(request.getStatusText());
            accessToken = gson.fromJson(request.getBody().toString(), TwitterToken.class);
            writeToken(accessToken);
        }
        bearerToken = accessToken.getAccessToken();
    }

    private String getTweet() {
        HttpResponse request = Unirest.get("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=realDonaldTrump&count=1&tweet_mode=extended")
                .header("Authorization" , "Bearer " + bearerToken).asJson();
        Tweet[] tweets = gson.fromJson(request.getBody().toString(), Tweet[].class);
        return tweets[0].toString();
    }
}
