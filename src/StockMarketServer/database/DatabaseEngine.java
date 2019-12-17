package StockMarketServer.database;

import java.io.File;
import java.sql.*;

public class DatabaseEngine {
    private static final String dbRootDirectory = "src/StockMarketServer/database/";
    private static final String JDBC_DRIVER	= "org.sqlite.JDBC";
    private static final String DATABASE_LOCATION	= "jdbc:sqlite:";
    private static String dbName = "globalStockMarket.db";
    private static Connection con = null;

    public static void start(String databaseName){
        dbName = databaseName;
        open();
//        getConnection();
    }

    /**
     * Establish JDBC connection with database
     * <p>
     * Autocommit is turned off delaying updates
     * until commit( ) is called
     */
    private static void getConnection( ) {
        try {
            con = DriverManager.getConnection(DATABASE_LOCATION + dbName);

            /*
             * Turn off AutoCommit:
             * delay updates until commit( ) called
             */
            con.setAutoCommit(false);
        }
        catch (SQLException sqle ) {
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
}
