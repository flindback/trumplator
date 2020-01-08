package server;

public class YodaTranslation {
    private Contents contents;

    public String toString() {
        return contents.translated;
    }

    public class Contents {
        private String translated;
    }
}
