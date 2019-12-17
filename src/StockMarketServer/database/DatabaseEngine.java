package StockMarketServer.database;

import jdk.nashorn.internal.runtime.ECMAException;

import java.io.File;
import java.sql.*;

public class DatabaseEngine {
    private static final String dbRootDirectory = "src/StockMarketServer/database/";
    private static final String JDBC_DRIVER	= "org.sqlite.JDBC";
    private static final String DATABASE_LOCATION	= "jdbc:sqlite:";
    private static String dbName = "globalStockMarket.db";
    private static Connection con = null;
    private static Statement statement = null;

    public static void start(String databaseName){
        dbName = databaseName;
        open();
        createTableTest();
    }

    /**
     * Establish JDBC connection with database
     * <p>
     * Autocommit is turned off delaying updates
     * until commit( ) is called
     */
    private static void getConnection( ) {
        try {
            con = DriverManager.getConnection(DATABASE_LOCATION + dbRootDirectory + dbName);

            /*
             * Turn off AutoCommit:
             * delay updates until commit( ) called
             */
            con.setAutoCommit(false);
            System.out.println("Connected to Database [" + dbName + "]");
        }
        catch (SQLException sqle) {
            System.out.println("Cannot connect to database");
            close( );
        }
    }

    /**
     * Opens database
     * <p>
     * Confirms database file exists and if so,
     * loads JDBC driver and establishes JDBC connection to database
     */
    private static void open() {
        File dbf = new File( dbRootDirectory + dbName );

        if (dbf.exists( ) == false) {
            System.out.println("SQLite database file [" + dbName + "] does not exist");
            System.exit( 0 );
        }
        System.out.println("Database [" + dbName + "] found");
        try {
            Class.forName( JDBC_DRIVER );
            getConnection( );
        }
        catch (ClassNotFoundException cnf ) {
            cnf.printStackTrace();
        }
    }

    /**
     * Close database
     * <p>
     * Commits any remaining updates to database and
     * closes connection
     */
    public static void close( ) {
        try {
            con.commit( ); // Commit any updates
            con.close ( );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTableTest() {
        try {
            statement = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS REGISTRATION " +
                    "(id INTEGER not NULL, " +
                    " first VARCHAR(255), " +
                    " last VARCHAR(255), " +
                    " age INTEGER)";
            statement.executeUpdate(sql);
            System.out.println("Created table");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            statement = con.createStatement();
            String sql = "INSERT INTO REGISTRATION VALUES(0, 'Edward', 'Corke', 20)";
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM REGISTRATION");
            while (rs.next()) {
                String first = rs.getString("first");
                System.out.println(first + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            con.commit(); // Commit any updates
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    Create companies table
//    CREATE TABLE IF NOT EXISTS "companies" (
//	"cID"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
//	"name"	TEXT NOT NULL,
//	"value"	NUMERIC DEFAULT 0,
//  "startDate" DATE,
//  "description" TEXT
//)

    //Create Trades Table
//    CREATE TABLE IF NOT EXISTS "companyTrades" (
//            "tID"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
//            "cID"	INTEGER,
//            "sell/buy"	TEXT NOT NULL,
//            "sizeOfTrade"	INTEGER NOT NULL,
//            "traderID" INTEGER NOT NULL,
//            "timeOfTrade" TIMESTAMP,
//    FOREIGN KEY("cID") REFERENCES "companies"("cID"),
//    FOREIGN KEY("traderID") REFERENCES "traders"("tID")
//            );

    //Create traders table
//    CREATE TABLE "traders" (
//            "tID"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
//            "username"	TEXT NOT NULL,
//            "passwordHashed"	TEXT NOT NULL,
//            "balance"	NUMERIC NOT NULL DEFAULT 0
//            );

    //Add trade
    //INSERT INTO companyTrades (cID, 'sell/buy', sizeOfTrade, traderID, timeOfTrade) VALUES (1, 'buy', 1, 3, '2019-12-17 15:14:46');

    //Add Company
    //INSERT INTO companies (name, value, startDate, description) VALUES ("Microsoft", 0, '2019-12-17', "PC Company");

    //Add Trader
//    INSERT INTO traders (username, passwordHashed, balance) VALUES ("edwardinio", "cheesyhash", 10.48);
}