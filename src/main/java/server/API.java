package server;

import static spark.Spark.*;

public class API {

    public static void main(String[] args) {
        port(5000);
        get("/", (req, res) -> "Hello World");
    }
}
