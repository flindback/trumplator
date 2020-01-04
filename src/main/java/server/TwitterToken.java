package server;

import java.io.Serializable;

public class TwitterToken implements Serializable {
    private String token_type;
    private String access_token;

    public String getAccessToken() {
        return access_token;
    }
}
