package StockMarketServer.httpHandlers;

import StockMarketServer.Helper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class CompanyHandler implements HttpHandler {
    private HttpExchange httpExchange;
    private URI uri;
    private String[] uriSplit;
    private String reponseBody;
    private String requestQuery;
    private Map<String, String> params, keyValuePairs;
    private int reponseCode;

    public void handle(HttpExchange t) throws IOException {
        this.httpExchange = t;
        this.uri = t.getRequestURI();
        this.uriSplit = uri.toString().split("/"); //getRequestURI for URL and split
        this.requestQuery = this.uri.getQuery();
        this.params = Helper.queryToMap(this.requestQuery);
        this.keyValuePairs = Helper.queryToMap(Helper.InputStreamToString(t.getRequestBody()));
//        this.idReceivedFrom = Server.handleJWT(httpExchange);
    }
}
