package server.entities;

public class Tweet {
    private String full_text;

    public void removeLink() {
        full_text.replaceAll(" https:\\/\\/t\\.co\\/[a-zA-Z0-9]{10}", "");
    }

    public String toString() {
        return full_text;
    }
}