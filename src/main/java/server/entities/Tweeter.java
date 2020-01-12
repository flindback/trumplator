package server.entities;

public class Tweeter {
    private String id;
    private String name;
    private String screen_name;
    private String profile_image_url;
    private String profile_banner_url;

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profile_image_url;
    }

    public String getProfileBannerUrl() {
        return profile_banner_url;
    }

    public String toString() {
        return screen_name;
    }
}
