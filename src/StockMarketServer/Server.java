package StockMarketServer;

import StockMarketServer.configuration.Config;
import StockMarketServer.httpHandlers.BaseHandler;
import com.sun.net.httpserver.HttpServer;
//import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * REST Web Server that handles a basic Auction System of: Auctions, Bids and Users
 */
public class Server {
//
//    public static HashMap<Integer, User> users = new HashMap();
//    public static HashMap<Integer, Auction> auctions = new HashMap();
//    //protected static HashMap<Integer, Bid> bids = new HashMap();
//
//    //Keep track of greatest ID reached for users, auctions and bids
//    public static Integer userIDReached = 0;
//    public static Integer auctionIDReached = 0;
//    public static Integer bidIDReached = 0;
//
//    public static String JWTSecret = "SCC311";
//    public static String JWTAlgorithm = "HmacSHA256";
//
//    public static String passwordHashSecret = "DistributedSystems";
//    public static String passwordHashingAlgorithm = "SHA-256";

//    private static Jedis redis;

    public static void main(String[] args) {
        Config.setup();

        try {
            //HTTP Handlers
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", new BaseHandler());                 //Catches any other URLs

            //Returns javascript when /scripts.js is requested
            server.createContext("/scripts.js", exchange -> {
                exchange.getResponseHeaders().add("Content-type", "text/javascript"); //So HTML is read correctly, and not as text
                OutputStream os = exchange.getResponseBody();
                File returnFile = new File("client/scripts.js"); //Write scripts.js to file
                exchange.sendResponseHeaders(200, returnFile.length()); //Return okay code and length of file
                Files.copy(Paths.get(returnFile.getAbsolutePath()), os); //Write index.html to response
                os = exchange.getResponseBody();
                os.close();
            });

            //Returns CSS when /styles.css is requested
            server.createContext("/styles.css", exchange -> {
                System.out.println("URI: "+exchange.getRequestURI().toString());
                exchange.getResponseHeaders().add("Content-type", "text/css"); //So HTML is read correctly, and not as text
                OutputStream os = exchange.getResponseBody();
                File returnFile = new File("src/StockMarketServer/client/styles.css"); //Write scripts.js to file
                exchange.sendResponseHeaders(200, returnFile.length()); //Return okay code and length of file
                Files.copy(Paths.get(returnFile.getAbsolutePath()), os); //Write index.html to response
                os = exchange.getResponseBody();
                os.close();
            });

//            //Returns CSS when /styles.css is requested
//            server.createContext("/.gif", exchange -> {
//                exchange.getResponseHeaders().add("Content-type", "image/gif"); //So HTML is read correctly, and not as text
//                OutputStream os = exchange.getResponseBody();
//                File returnFile = new File("src/StockMarketServer/client/images/404.gif"); //Write scripts.js to file
//                exchange.sendResponseHeaders(200, returnFile.length()); //Return okay code and length of file
//                Files.copy(Paths.get(returnFile.getAbsolutePath()), os); //Write index.html to response
//                os = exchange.getResponseBody();
//                os.close();
//                System.out.println(".gif requested");
//            });

            server.start();
            System.out.println("Server running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}