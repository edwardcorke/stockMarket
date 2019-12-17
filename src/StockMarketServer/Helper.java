package StockMarketServer;

import StockMarketServer.configuration.Config;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Helper {
    /**
     * Maps a query (from request body) to a HashMap for ease of searching
     * @param query
     * @return Returns HashMap generated
     */
    public static Map<String, String> queryToMap(String query) {
        try {
            Map<String, String> result = new HashMap<>();
            String[] firstSplit = query.split("&");
            for(int i=0; i<firstSplit.length; i++){
                String[] secondSplit = firstSplit[i].split("=");
                if (secondSplit.length > 1) {
                    result.put(secondSplit[0], secondSplit[1]);
                } else {
                    result.put(secondSplit[0], "");
                }
            }
            return result;
        } catch (Exception e) {
            //System.out.println("Query null");
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a map to String in the query format
     * @param map
     * @return String in query format
     */
    public static String mapToQuery(Map<String, String> map) {
        Iterator mapIterator = map.entrySet().iterator();
        String query = "";
        while(mapIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) mapIterator.next();
            String key = (String)mapElement.getKey();
            String value = (String)mapElement.getValue();
            query += key + "=" + value + "&";
        }
        if(query.charAt(query.length()-1) == '&') {
            StringBuilder sb = new StringBuilder(query);
            sb.deleteCharAt(query.length()-1);
            query = sb.toString();
        }
        System.out.println("Query: "+query);
        return query;
    }

    /**
     * Converts an InputStream to string
     * @param is
     * @return string
     * @throws IOException
     */
    public static String InputStreamToString(InputStream is) throws IOException {
        String isString = "";
        while (is.available() > 0) {
            isString += (char) is.read();
        }
        return isString;
    }

    /**
     * Finds the configured content-type for a requested file, finds file in server directory and returns in HttpExchange response
     * @param t HttpExchange
     */
    public static void returnRequestedFile(HttpExchange t, String urlString, OutputStream os) {
        try {
            File basePage = new File("src/StockMarketServer" + urlString); //Write index.html to file
            t.getResponseHeaders().add("Content-type", Config.getContentType(urlString.split("\\.")[1]));
            t.sendResponseHeaders(200, basePage.length()); //Return okay code and length of file
            Files.copy(Paths.get(basePage.getAbsolutePath()), os); //Write index.html to response
            os = t.getResponseBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
