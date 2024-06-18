package com.pharmacie.pharmacie;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbFunctions {
    public Connection connect_to_db(){
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pharmacie_db", "postgres", "Jeremstar");
            if(conn!=null){
                System.out.println("Connection Established!");
            }
            else{
                System.out.println("Connection Failed!");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }

}

