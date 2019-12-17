package server;

import twitter4j.auth.AccessToken;

import java.io.*;

public class FileHelper {
    static AccessToken readToken() {
        AccessToken token = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("conf/access.tok"));
            token = (AccessToken) ois.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return token;
    }

    static void writeToken(AccessToken token) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream((new FileOutputStream("conf/access.tok")));
            oos.writeObject(token);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static String[] getApiKeys() {
        String[] keys = new String[2];
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("conf/twitter.key"), "ISO-8859-1"));
            String input;

            for (int i = 0; i < 3; i++) {
                input = br.readLine();
                if (i == 1)
                    keys[0] = input.split(":")[1];
                else if (i == 2)
                    keys[1] = input.split(":")[1];
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return keys;
    }
}
