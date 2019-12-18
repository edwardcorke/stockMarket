package StockMarketServer.httpHandlers;

import StockMarketServer.Helper;
import StockMarketServer.database.DatabaseEngine;
import StockMarketServer.database.UserEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.ResultSet;
import java.util.Map;

public class UserHandler implements HttpHandler {
    private HttpExchange httpExchange;
    private URI uri;
    private String[] uriSplit;
    private String responseBody = "";
    private String requestMethod;
    private String requestQuery;
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
                if (requestMethod.equals("POST")) {
                    System.out.println("Creating user");
                    createUser();
                }
                break;
            case(3):
                if (uriSplit[2].contains("login") && requestMethod.equals("POST")) {
                        System.out.println("Log in");
                        attemptLogin();
                    }
                break;
        }

        t.getResponseHeaders().add("Content-type", "application/json");
        t.sendResponseHeaders(reponseCode, responseBody.length());
        OutputStream os = t.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();
    }

    private void createUser() {
        String findUserStatement = "SELECT * FROM traders WHERE username = \'" + keyValuePairs.get("username")+"\'";
        ResultSet userSearchResult = DatabaseEngine.executeSQLStatement(findUserStatement);
        try {
            String usernameFound = userSearchResult.getString("username");
            if(usernameFound != null) {
                System.out.println("Username already used");
                reponseCode = 400;
            }
        } catch (Exception e) {
            //user does not exist
            UserEngine.createUser(
                    keyValuePairs.get("username"),
                    keyValuePairs.get("password"),
                    Float.valueOf(keyValuePairs.get("balance")));
            reponseCode = 200;
        }
    }

    /**
     * Uses username and password supplied in request body to attempt to find user, and match the password
     * Sets status code (rCode) to 200 if username was found and password matched
     * Sets status code (rCode) to 401 if username was found but password was incorrect
     * Sets status code (rCode) to 400 if username does not match any users in server
     */
    private void attemptLogin(){
        System.out.println("Attempting to log in");
        //search for trader by username
        String findUserStatement = "SELECT * FROM traders WHERE username = \'" + keyValuePairs.get("username")+"\'";
        ResultSet userSearchResult = DatabaseEngine.executeSQLStatement(findUserStatement);
            try {
                String passwordFound = userSearchResult.getString("passwordHashed");
                if (passwordFound.equals(Helper.hashString(keyValuePairs.get("password")))) {
                    System.out.println("Correct login");
                    reponseCode = 200;
                    //TODO: add JWT to header
                } else {
                    System.out.println("Incorrect details");
                    reponseCode = 401;
                }
            } catch (Exception e) {
                System.out.println("User not found");
                reponseCode = 400;
//                e.printStackTrace();
            }
    }
}
