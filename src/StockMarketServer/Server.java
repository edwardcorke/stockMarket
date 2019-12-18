package StockMarketServer;

import StockMarketServer.configuration.Config;
import StockMarketServer.database.DatabaseEngine;
import StockMarketServer.httpHandlers.BaseHandler;
import StockMarketServer.httpHandlers.CompanyHandler;
import StockMarketServer.httpHandlers.UserHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/**
 * REST Web Server that handles a basic Auction System of: Auctions, Bids and Users
 */
public class Server {

    public static void main(String[] args) {
        Config.setup();
        DatabaseEngine.start("globalStockMarket.db");

        try {
            //HTTP Handlers
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", new BaseHandler());                 //Catches any other URLs
            server.createContext("/user", new UserHandler());                 //Catches any other URLs
            server.createContext("/company", new CompanyHandler());                 //Catches any other URLs

            server.start();
            System.out.println("Server running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}