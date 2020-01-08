package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        //api.translate("Hej på dig");
    }

    private void startServer() {

        port(5000);

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
        });

        get("/", (req, res) -> {
<<<<<<< HEAD
        	System.out.println("Hej!");
=======
            JsonObject jsonObj = new JsonObject();

>>>>>>> 17971910a34558e0a744c44d4742c710ee83d0f6
            String tweet = getTweet();
            System.out.println("Tweet: " + tweet);
            String translation = yodaTranslate(tweet);
            System.out.println("Translated: " + tweet);
            OpenWeather weather = weather();

            jsonObj.addProperty("tweet", tweet);
            jsonObj.addProperty("translation", translation);
            jsonObj.addProperty("main", weather.getMain());
            jsonObj.addProperty("description", weather.toString());
            return jsonObj;
        });
    }

    public OpenWeather weather() {
        String key = getApiKey("openweather");
        String location = "5815135";
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?id=%s&APPID=%s", location, key);
        HttpResponse request = Unirest.get(url).asJson();
        OpenWeather weather = gson.fromJson(request.getBody().toString(), OpenWeather.class);
        return weather;
    }

    public String translate(String input) {
        String url = String.format("https://api.funtranslations.com/translate/yoda.json?text=%s", URLEncoder.encodeUTF8(input));
        //System.out.println(url);
        HttpResponse request = Unirest.post(url).asJson();
        if (request.getBody().toString().contains("error"))
            return input + " (Translation malfunction)";
        else {
            System.out.println(request.getBody().toString());
            Translation translation = gson.fromJson(request.getBody().toString(), Translation.class);
            return translation.toString();
        }
    }

    public String yodaTranslate(String input) {
        String url = "https://yodish.p.rapidapi.com/yoda.json";
        String key = getApiKey("rapidapi");
        //System.out.println("Key: " + key);
        HttpResponse request = Unirest.post(url)
                .header("x-rapidapi-host", "yodish.p.rapidapi.com")
                .header("x-rapidapi-key", key)
                .header("content-type", "application/x-www-form-urlencoded")
                .queryString("text", input).asJson();
        YodaTranslation translation = gson.fromJson(request.getBody().toString(), YodaTranslation.class);
        return translation.toString();
    }

    public void authenticate() {
        TwitterToken accessToken = readToken();
        if (accessToken == null) {
            String consumer_key = URLEncoder.encodeUTF8(getTwitterKey(Key.API_key));
            String consumer_secret = URLEncoder.encodeUTF8(getTwitterKey(Key.API_secret_key));
            String bearer_token = Base64.getEncoder().encodeToString(String.format("%s:%s", consumer_key, consumer_secret).getBytes());

            HttpResponse request = Unirest.post("https://api.twitter.com/oauth2/token")
                    .header("Authorization", "Basic " + bearer_token)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .header("accept", "application/json")
                    .body("grant_type=client_credentials").asJson();

            //System.out.println(request.getStatusText());
            accessToken = gson.fromJson(request.getBody().toString(), TwitterToken.class);
            writeToken(accessToken);
        }
        bearerToken = accessToken.getAccessToken();
    }

    private String getTweet() {
        Tweet[] tweets = new Tweet[0];
        for (int i = 0; i < 10 && tweets.length < 1; i++) {
            HttpResponse request = Unirest.get("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=realDonaldTrump&count=100&tweet_mode=extended&exclude_replies=true&include_rts=false")
                    .header("Authorization", "Bearer " + bearerToken).asJson();
            //System.out.println(request.getStatusText());
            tweets = gson.fromJson(request.getBody().toString(), Tweet[].class);
            //System.out.println(tweets.length);
        }
        if (tweets.length > 0) {
            String res = tweets[0].toString();
            //System.out.println(res);
            return res.replaceAll(" https:\\/\\/t\\.co\\/[a-zA-Z0-9]{10}", "");
        } else
            return "Twitter has failed you";
    }
}
