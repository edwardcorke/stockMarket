package StockMarketServer.configuration;

import java.util.HashMap;

public class Config {

    public static HashMap<String, String> contentTypes = new HashMap();
    public static String passwordHashingAlgorithm = "SHA-256";

    public static void setup(){
        contentTypes.put("html", "text/html");
        contentTypes.put("css", "text/css");
        contentTypes.put("js", "application/x-javascript");
        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("jpeg", "image/jpeg");
        contentTypes.put("gif", "image/gif");
    }

    public static String getContentType(String s) {
        if(contentTypes.containsKey(s)) {
            return contentTypes.get(s);
        } else {
            return "text/html";
        }
    }
}
