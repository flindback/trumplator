package server.helpers;

import server.entities.TwitterToken;

import java.io.*;
import java.util.Base64;
import java.util.Random;

public class APIHelper {
    private static Random random = new Random();
    private static String alphanumericals = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String extraCharacters = ".? !#-_:;,()";

    public static TwitterToken readToken() {
        TwitterToken token = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("conf/access.tok"));
            token = (TwitterToken) ois.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return token;
    }

    public static void writeToken(TwitterToken token) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("conf/access.tok"));
            oos.writeObject(token);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String getTwitterKey(Key key) {
        String ret = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("conf/twitter.key"), "ISO-8859-1"));
            String input;

            while ((input = br.readLine()) != null) {
                String strIdent = input.split(":")[0];
                if (key == Key.API_key && strIdent.equals("API key"))
                    ret = input.split(":")[1];
                else if (key == Key.API_secret_key && strIdent.equals("API secret key"))
                    ret = input.split(":")[1];
                else if (key == Key.Access_token && strIdent.equals("Access token"))
                    ret = input.split(":")[1];
                else if (key == Key.Access_token_secret && strIdent.equals("Access token secret"))
                    ret = input.split(":")[1];
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ret;
    }

    public static String getApiKey(String file) {
        String ret = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(String.format("conf/%s.key", file)), "ISO-8859-1"));
            ret = br.readLine();
        } catch (Exception e) {
            System.out.println("getApiKey error: " + e);
        }
        return ret;
    }

    public static String generateNonce() {
        byte[] nonce = new byte[32];
        random.nextBytes(nonce);
        String ret = Base64.getEncoder().encodeToString(nonce);
        return cleanString(ret, alphanumericals);

    }

    public static String cleanString(String input, String filter) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            CharSequence cs = input.substring(i, i + 1);
            if (filter.contains(cs))
                sb.append(cs);
        }
        return sb.toString();
    }

    public static String cleanTweet(String tweet) {
        return cleanString(tweet, alphanumericals + extraCharacters);
    }

    public enum Key {
        API_key, API_secret_key, Access_token, Access_token_secret
    }
}