package StockMarketServer.database;

import StockMarketServer.Helper;

public class UserEngine {
    public static void createUser(String username, String password, Float balance) {
        String hashedPassword = Helper.hashString(password);
        String createUserStatement = "INSERT INTO traders" +
                " (username, passwordHashed, balance)" +
                " VALUES (\"" + username + "\"," +
                " \"" + hashedPassword + "\"," +
                balance + ")";
        DatabaseEngine.executeSQLStatement(createUserStatement);
    }

    //TODO: delete user
    //TODO: update user
}
