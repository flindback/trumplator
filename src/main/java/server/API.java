package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Base64;

import static server.helpers.APIHelper.*;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import server.helpers.APIHelper;
import server.helpers.APIHelper.Key;
import server.entities.*;

import static server.helpers.URLEncoder.encodeUTF8;
import static spark.Spark.*;

public class API {
    private String bearerToken;
    private Gson gson = new Gson();
    private boolean verbose = true;

    public static void main(String[] args) {
        API api = new API();
        api.authenticate();
        api.startServer();
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

            String paramTweeter = req.queryParams("t");
            String paramCity = req.queryParams("c");
            Tweeter tweeter = null;

            if (paramTweeter != null)
                tweeter = getUser(paramTweeter);

            if (tweeter == null || !tweeter.toString().toLowerCase().equals(paramTweeter.toLowerCase()))
                tweeter = getUser("realDonaldTrump");

            String tweet = getTweet(tweeter);

            if (verbose) System.out.println("Tweet: " + tweet);

            JsonObject jsonObj = new JsonObject();

            String translation = yodaTranslate(tweet);
            if (verbose) System.out.println("Translated: " + tweet);

            OpenWeather weather = null;
            String cityOutput = null;

            if (paramCity != null)
                weather = weather(paramCity);

            if (weather == null) {
                weather = weather();
                cityOutput = "Washington";
            } else
                cityOutput = paramCity;

            jsonObj.addProperty("tweeter", tweeter.toString());
            jsonObj.addProperty("name", tweeter.getName());
            jsonObj.addProperty("profile_image_url", tweeter.getProfileImageUrl());
            jsonObj.addProperty("banner_image_url", tweeter.getProfileBannerUrl());
            jsonObj.addProperty("tweet", tweet);
            jsonObj.addProperty("translation", translation);
            jsonObj.addProperty("city", cityOutput);
            jsonObj.addProperty("main", (weather != null ? weather.getMain() : "city not found"));
            jsonObj.addProperty("description", (weather != null ? weather.toString() : "city not found"));
            return jsonObj;
        });
    }

    public OpenWeather weather(String city) {
        OpenWeather weather = null;
        String key = getApiKey("openweather");
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=%s", city, key);
        HttpResponse request = Unirest.get(url).asJson();
        System.out.println("weather: " + request.getBody().toString());
        if (!request.getBody().toString().contains("404"))
            weather = gson.fromJson(request.getBody().toString(), OpenWeather.class);

        return weather;

    }

    public OpenWeather weather() {
        String key = getApiKey("openweather");
        String location = "5815135";
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?id=%s&APPID=%s", location, key);
        HttpResponse request = Unirest.get(url).asJson();
        OpenWeather weather = gson.fromJson(request.getBody().toString(), OpenWeather.class);
        return weather;
    }

    /*public String translate(String input) {
        String url = String.format("https://api.funtranslations.com/translate/yoda.json?text=%s", encodeUTF8(input));
        //System.out.println(url);
        HttpResponse request = Unirest.post(url).asJson();
        if (request.getBody().toString().contains("error"))
            return input + " (Translation malfunction)";
        else {
            System.out.println(request.getBody().toString());
            Translation translation = gson.fromJson(request.getBody().toString(), Translation.class);
            return translation.toString();
        }
    }*/

    public String yodaTranslate(String input) {
        String ret = null;
        try {
            String url = "https://yodish.p.rapidapi.com/yoda.json";
            String key = getApiKey("rapidapi");
            HttpResponse request = Unirest.post(url)
                    .header("x-rapidapi-host", "yodish.p.rapidapi.com")
                    .header("x-rapidapi-key", key)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .queryString("text", cleanTweet(input)).asJson();
            if (verbose && request != null)
                System.out.println("yodaTranslate (received json object): " + request.getBody().toString());
            YodaTranslation translation = gson.fromJson(request.getBody().toString(), YodaTranslation.class);
            ret = translation.toString();
        } catch (Exception e) {
            System.out.println("yodaTranslate:Error: " + e.toString());
        }
        return ret;
    }

    public void authenticate() {
        TwitterToken accessToken = readToken();
        if (accessToken == null) {
            String consumer_key = encodeUTF8(getTwitterKey(Key.API_key));
            String consumer_secret = encodeUTF8(getTwitterKey(Key.API_secret_key));
            String bearer_token = Base64.getEncoder().encodeToString(String.format("%s:%s", consumer_key, consumer_secret).getBytes());

            HttpResponse request = Unirest.post("https://api.twitter.com/oauth2/token")
                    .header("Authorization", "Basic " + bearer_token)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .header("accept", "application/json")
                    .body("grant_type=client_credentials").asJson();

            accessToken = gson.fromJson(request.getBody().toString(), TwitterToken.class);
            writeToken(accessToken);
        }
        bearerToken = accessToken.getAccessToken();
    }

    private String getTweet(Tweeter tweeter) {

        Tweet[] tweets = new Tweet[0];
        // Sometimes Twitter fails to supply tweets, so the program retries 10 times until success
        for (int i = 0; i < 10 && tweets.length < 1; i++) {
            HttpResponse request = Unirest.get(String.format("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=%s&count=100&tweet_mode=extended&exclude_replies=true&include_rts=false", tweeter.toString()))
                    .header("Authorization", "Bearer " + bearerToken).asJson();
            tweets = gson.fromJson(request.getBody().toString(), Tweet[].class);
            if (verbose) System.out.println("getTweet (number of tweets): " + tweets.length);
        }
        if (tweets.length > 0) {
            String res = tweets[0].toString();
            if (verbose) System.out.println("getTweet (first tweet): " + res);
            return res.replaceAll(" https:\\/\\/t\\.co\\/[a-zA-Z0-9]{10}", "");
        } else
            return "Twitter has failed you";
    }

    public Tweeter getUser(String user) {
        String nonce = APIHelper.generateNonce();
        if (verbose) System.out.println("Nonce: " + nonce);

        String timestamp = Long.toString(System.currentTimeMillis() / 1000);

        String signature = generateSignature(user, nonce, timestamp);
        if (verbose) System.out.println("Signature: " + signature);

        String header = generateHeaderString(nonce, timestamp, signature);
        if (verbose) System.out.println("Header: " + header);

        HttpResponse request = Unirest.get("https://api.twitter.com/1.1/users/search.json?q=" + user + "&count=1")
                .header("authorization", header).asJson();
        if (verbose) System.out.println("getUser (received json object): " + request.getBody().toString());

        Tweeter[] tweeter = gson.fromJson(request.getBody().toString(), Tweeter[].class);
        if (verbose && tweeter != null) System.out.println("getUser (number of users): " + tweeter.length);
        if (tweeter != null && tweeter.length > 0)
            return tweeter[0];
        else
            return null;
    }

    private String generateSignature(String user, String nonce, String timestamp) {
        String base_url = "https://api.twitter.com/1.1/users/search.json";
        String parameter =
                encodeUTF8("count") + "=" + encodeUTF8("1") + "&" +
                        encodeUTF8("oauth_consumer_key") + "=" + encodeUTF8(APIHelper.getTwitterKey(Key.API_key)) + "&" +
                        encodeUTF8("oauth_nonce") + "=" + encodeUTF8(nonce) + "&" +
                        encodeUTF8("oauth_signature_method") + "=" + encodeUTF8("HMAC-SHA1") + "&" +
                        encodeUTF8("oauth_timestamp") + "=" + encodeUTF8(timestamp) + "&" +
                        encodeUTF8("oauth_token") + "=" + encodeUTF8(APIHelper.getTwitterKey(Key.Access_token)) + "&" +
                        encodeUTF8("oauth_version") + "=" + encodeUTF8("1.0") + "&" +
                        encodeUTF8("q") + "=" + encodeUTF8(user);
        if (verbose) System.out.println("generateSignature:Parameter string: " + parameter);
        String signature_base = "GET&" + encodeUTF8(base_url) + "&" + encodeUTF8(parameter);
        if (verbose) System.out.println("generateSignature:Signature base: " + signature_base);
        String signing_key = encodeUTF8(APIHelper.getTwitterKey(Key.API_secret_key)) + "&" + encodeUTF8(APIHelper.getTwitterKey(Key.Access_token_secret));
        if (verbose) System.out.println("generateSignature:Signature key: " + signing_key);

        byte[] signByteArr = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, signing_key).hmac(signature_base);

        return Base64.getEncoder().encodeToString(signByteArr);
    }

    private String generateHeaderString(String nonce, String timestamp, String signature) {
        return "OAuth " +
                encodeUTF8("oauth_consumer_key") + "=\"" + encodeUTF8(APIHelper.getTwitterKey(Key.API_key)) + "\", " +
                encodeUTF8("oauth_nonce") + "=\"" + encodeUTF8(nonce) + "\", " +
                encodeUTF8("oauth_signature") + "=\"" + encodeUTF8(signature) + "\", " +
                encodeUTF8("oauth_signature_method") + "=\"" + encodeUTF8("HMAC-SHA1") + "\", " +
                encodeUTF8("oauth_timestamp") + "=\"" + encodeUTF8(timestamp) + "\", " +
                encodeUTF8("oauth_token") + "=\"" + encodeUTF8(APIHelper.getTwitterKey(Key.Access_token)) + "\", " +
                encodeUTF8("oauth_version") + "=\"" + encodeUTF8("1.0") + "\"";
    }
}