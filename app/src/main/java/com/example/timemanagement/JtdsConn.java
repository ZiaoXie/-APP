package com.example.timemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 谢星宇 on 2017/6/7.
 */

public class JtdsConn {
    static String dbURL = "jdbc:jtds:sqlserver://172.18.52.65:1433;DatabaseName=SWX";
    static String user = "sa";
    static String password = "123456";
    static Connection conn;
    static Statement sql;
    static ResultSet rs;

    public static Statement Conn(){
        try {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn= DriverManager.getConnection(dbURL,user,password);
            sql=conn.createStatement();
            return sql;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }


    }

    public static ResultSet Query(String Sql){
        try {

            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn= DriverManager.getConnection(dbURL,user,password);

            sql=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs=sql.executeQuery(Sql);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    public static Statement Execute(String Sql){
        try {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn= DriverManager.getConnection(dbURL,user,password);
            sql=conn.createStatement();
            sql.execute(Sql);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sql;
    }

    public static void close(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    conn.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
