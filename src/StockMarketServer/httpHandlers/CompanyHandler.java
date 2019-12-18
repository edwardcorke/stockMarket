//TODO: add increase/decrease in value to companies table

package StockMarketServer.httpHandlers;

import StockMarketServer.Helper;
import StockMarketServer.database.DatabaseEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class CompanyHandler implements HttpHandler {
    private HttpExchange httpExchange;
    private URI uri;
    private String[] uriSplit;
    private String responseBody = "";
    private String requestQuery;
    private String requestMethod;
    private Map<String, String> params, keyValuePairs;
    private int reponseCode = 200;

    public void handle(HttpExchange t) throws IOException {
        this.httpExchange = t;
        this.uri = t.getRequestURI();
        this.uriSplit = uri.toString().split("/"); //getRequestURI for URL and split
        this.requestQuery = this.uri.getQuery();
        this.requestMethod = t.getRequestMethod();
        this.params = Helper.queryToMap(this.requestQuery);
        this.keyValuePairs = Helper.queryToMap(Helper.InputStreamToString(t.getRequestBody()));
//        this.idReceivedFrom = Server.handleJWT(httpExchange);

        switch (uriSplit.length) {
            case(2):
                if(requestMethod.equals("POST")) {
                    publiciseCompany();
                }
                break;
            case(3):
                if(uriSplit[3].equals("trade") && requestMethod.equals("POST")) {
                    trade();
                }
                if(requestMethod.equals("GET")) {
                    viewTrades();
                }
                break;
        }

        t.getResponseHeaders().add("Content-type", "application/json");
        t.sendResponseHeaders(reponseCode, responseBody.length());
        OutputStream os = t.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();
    }

    private void viewTrades() {
        String viewTradesStatement = "SELECT * FROM " + keyValuePairs.get("name") + "Trades";
        ResultSet viewTradeResults = DatabaseEngine.executeSQLStatement(viewTradesStatement);
        ArrayList<ArrayList<String>> trades = new ArrayList<>();
        try {
            while (viewTradeResults.next()) {
                ArrayList<String> trade = new ArrayList<>();
                trade.add(viewTradeResults.getString("valueAfterTrade"));
                trade.add(viewTradeResults.getString("timeOfTrade"));
                trades.add(trade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray tradesJSONArray = new JSONArray();
        trades.forEach(trade -> tradesJSONArray.put(new JSONObject().put("valueAfterTrade", trade.get(1)).put("timeOfTrade", trade.get(2))));
        responseBody = tradesJSONArray.toString();
    }

    private void trade() {
        //Make sure company exists
        //Make sure trader has money available
        //If so make trade and update total in companies table
    }

    private void publiciseCompany() {
        System.out.println("Publicising company");

        String addCompanyToCompaniesTableStatement = "INSERT INTO companies (name, value, startDate, description) VALUES ('" +
                keyValuePairs.get("name") +"', '" +
                keyValuePairs.get("value") + "', '" +
                keyValuePairs.get("startDate") + "', '" +
                keyValuePairs.get("description") + "')";

        DatabaseEngine.executeSQLStatement(addCompanyToCompaniesTableStatement);

        String getCIDOfCompanyPublicised = "SELECT cID FROM companies WHERE name = '" + keyValuePairs.get("name")+"'";
        ResultSet cIDResultSet = DatabaseEngine.executeSQLStatement(getCIDOfCompanyPublicised);

        try {
            String cID = cIDResultSet.getString("cID");
            String publiciseCompanyStatement = "CREATE TABLE IF NOT EXISTS '" + keyValuePairs.get("name") + "Trades' (" +
                    " 'tradeID' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    " 'cID' INTEGER DEFAULT " + cID + ", " +
                    " 'sell/buy' TEXT NOT NULL, " +
                    " 'sizeOfTrade' INTEGER NOT NULL, " +
                    " 'traderID' INTEGER NOT NULL, " +
                    " 'timeOfTrade' TIMESTAMP, " +
                    " 'valueAfterTrade' NUMERIC DEFAULT 0, " +
                    " FOREIGN KEY('traderID') REFERENCES 'traders'('tID') " +
                    " )";
            DatabaseEngine.executeSQLStatement(publiciseCompanyStatement);
        } catch (Exception e) {
            e.printStackTrace();
        }



        //TODO: add values to table
    }
}
