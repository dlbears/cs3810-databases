package org.yourcompany.project;

import java.sql.*;
/**
 * Hello world!
 *
 * 1. SELECT *
 *    FROM orders
 *    WHERE customerid = 14;
 * 
 * 2. SELECT *
 *    FROM orders
 *    WHERE customerid = ?;
 * 
 * 3. INSERT INTO customer(id, company, lastname, firstname, email, businessphone)
 *    VALUES (?, ?, ?, ?, ?, ?);
 * 
 */
public class App 
{
    private static String dbUrl = "jdbc:postgresql://localpg:5432/postgres?user=postgres&password=example&ssl=false";
    
    private static Connection activeConn;

    private static Connection connect() throws SQLException
    {
        return  DriverManager.getConnection(App.dbUrl); 
    }

    private static int q3(Connection c, int ID, String Company, String LastName, String FirstName, String Email, String BusinessPhone) throws SQLException {
            String sql = "INSERT INTO customer(id, company, lastname, firstname, email, businessphone) VALUES (?, ?, ?, ?, ?, ?);";
            
            PreparedStatement insertionStmt = c.prepareStatement(sql);

            insertionStmt.setInt(1, ID);
            insertionStmt.setString(2, Company);
            insertionStmt.setString(3, LastName);
            insertionStmt.setString(4, FirstName);
            insertionStmt.setString(5, Email);
            insertionStmt.setString(6, BusinessPhone);

            return insertionStmt.executeUpdate();
        }
    
    private static ResultSet q1(Connection c) throws SQLException {
        String sql = "SELECT * FROM orders WHERE customerid = 14;";
        Statement query = c.createStatement();

        return query.executeQuery(sql);


    }

    private static ResultSet q2(Connection c, int ID) throws SQLException {
        String sql = "SELECT * FROM orders WHERE customerid = ?;";
        PreparedStatement queryStmt = c.prepareStatement(sql);

        queryStmt.setInt(1, ID);

        return queryStmt.executeQuery();

    }

    private static ResultSet getCustomers(Connection c) throws SQLException {
        String sql = "SELECT * FROM customer";
        Statement query = c.createStatement();
        return query.executeQuery(sql);
    }

    private static void display(ResultSet[] S) throws SQLException {
        for (ResultSet s: S) {
            ResultSetMetaData metas = s.getMetaData();
            int columns = metas.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                System.out.print(metas.getColumnName(i) + " ");
            }
            System.out.println("");
            while (s.next()) {
                for (int i = 1; i <= columns; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = s.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            } 
            System.out.println("");
            
        }
    }

    private static void runHW(Connection c) throws SQLException
    {
        ResultSet res[] = new ResultSet[4];

        res[0] = App.q1(c);
        res[1] = App.q2(c, 14);
        res[2] = App.getCustomers(c);
        App.q3(c, 15, "QBC Specialties", "Oren", "Sam", "s.oren@qbcco.com", "(561) 989-0989");
        App.q3(c, 16, "JBL Specialties", "Reno", "Mas", "m.reno@jbl.com", "(445) 895-0215");
        res[3] = App.getCustomers(c);

        App.display(res);

    
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        
        try {
            Connection ac = App.connect();
            App.runHW(ac);
        } catch (SQLException e) {
            System.out.println("HW Exception");
            e.printStackTrace(System.err);
            System.err.println("SQLState: " +
                ((SQLException)e).getSQLState());

            System.err.println("Error Code: " +
                ((SQLException)e).getErrorCode());

            System.err.println("Message: " + e.getMessage());

            Throwable t = e.getCause();
            while(t != null) {
                System.out.println("Cause: " + t);
                t = t.getCause();
            }
        }
    }
}
