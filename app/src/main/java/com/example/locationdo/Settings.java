package com.example.locationdo;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Names: Jonas, Weston, Grant, Mike
 * Course: CIT368-01
 * Assignment: Group Part 2
 * Date: 4/28/2019
 * Purpose: Sets the thread policy and sets up the connection for the database.
 * Assumptions: Min SDK 23, Target SDK 28
 *              Database is fully operational
 *
 */

public class Settings {

    private static Settings settings;

    public static float DEFAULT_ZOOM = 7f;

    public int userid = -1;
    /**
     * Sets the thread policy
     *
     */
    private Settings() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
    /**
     * Sets the up the database connection string
     *
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String DB_URL = "jdbc:jtds:sqlserver://3.87.197.166:1433/LocationDo;user=LocationDo;password=CitSsd!";
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        Connection con = DriverManager.getConnection(DB_URL);
        return con;
    }
}