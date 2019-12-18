package StockMarketServer.httpHandlers;

import StockMarketServer.Helper;
import StockMarketServer.configuration.Config;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.xml.ws.http.HTTPException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BaseHandler implements HttpHandler{
    private String urlString;
    private String[] url;
    private File basePage;
    private OutputStream os;

    /**
     * Handles HTTP requests under '/'. Therefore, this the lowest level a client can access. Used to either return index.html or 404 page not found
     * @param t HttpExcange object
     * @throws IOException
     */
    public void handle(HttpExchange t) throws IOException {
        os = t.getResponseBody();
        urlString = t.getRequestURI().toString();
        this.url =t.getRequestURI().toString().split("/"); //getRequestURI for URL and split

        System.out.println("Base URI Received: "+t.getRequestURI().toString());

        if(urlString.contains(".")) {
            Helper.returnRequestedFile(t, urlString, os);
        } else {
            if(url.length == 0 || url[url.length-1].equals("index.html")) {
                //Returns index.html
                basePage = new File("src/StockMarketServer/client/index/index.html"); //Write index.html to file
                t.getResponseHeaders().add("Content-type", "text/html");
                t.sendResponseHeaders(200, basePage.length()); //Return okay code and length of file
                Files.copy(Paths.get(basePage.getAbsolutePath()), os); //Write index.html to response
                os = t.getResponseBody();
            }
            else {
                //Returns pageNotFound.html
                basePage = new File("src/StockMarketServer/client/pageNotFound/pageNotFound.html"); //Write index.html to file
                t.sendResponseHeaders(404, basePage.length()); //Return okay code and length of file
                Files.copy(Paths.get(basePage.getAbsolutePath()), os); //Write index.html to response
                os = t.getResponseBody();
            }
        }
        os.close();
    }
}
