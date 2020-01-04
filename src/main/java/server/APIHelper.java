package server;


import java.io.*;
import java.util.Random;

public class APIHelper {
    private static Random random = new Random();

    static TwitterToken readToken() {
        TwitterToken token = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("conf/access.tok"));
            token = (TwitterToken) ois.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return token;
    }

    static void writeToken(TwitterToken token) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream((new FileOutputStream("conf/access.tok")));
            oos.writeObject(token);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static String getKey(Key key) {
        String ret = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("conf/twitter.key"), "ISO-8859-1"));
            String input;

            while( ( input = br.readLine() ) != null ) {
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

    public static String generateNonce() {
        StringBuilder sb = new StringBuilder();
        int character;
        for (int i = 0; i < 32; i++) {
            character = random.nextInt(62);
            if (character < 26)
                sb.append((char) (character + 65));
            else if (character < 52)
                sb.append((char) (character + 71));
            else
                sb.append(character - 52);
        }
        return sb.toString();
    }

    public enum Key {
        API_key, API_secret_key, Access_token, Access_token_secret
    }
}