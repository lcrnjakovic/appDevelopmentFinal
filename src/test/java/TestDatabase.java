import org.junit.Test;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by lukacrnjakovic on 10/15/17.
 */
public class TestDatabase {

    @Test
    public void testDBConnection(){
        String connSuccess = "";
        try{
            DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?verifyServerCertificate=false&useSSL=true","root","student");
            connSuccess = "connected";
        }
        catch (SQLException sql ) {
            connSuccess = "error";
            sql.printStackTrace();
        }
        assertEquals("connected", connSuccess);

    }

    @Test
    public void testSelectSyntax(){
        Connection conn = null;
        ResultSet rs = null;
        try{
            File script = new File("table.sql");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?verifyServerCertificate=false&useSSL=true","root","student");
            assertTrue(script.exists());
            String fileAsString = new Scanner(script).useDelimiter("\\Z").next();
            System.out.println(fileAsString);
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(fileAsString);
        }
        catch (SQLException sql ) {
            assertNotNull(conn);
            assertNotNull(rs);
            sql.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateStatementSyntax(){
        Connection conn = null;
        try{
            File script = new File("table.sql");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?verifyServerCertificate=false&useSSL=true","root","student");
            assertTrue(script.exists());
            String fileAsString = new Scanner(script).useDelimiter("\\Z").next();
            System.out.println(fileAsString);
            Statement statement = conn.createStatement();
            Scanner scanner = new Scanner(fileAsString);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                statement.executeUpdate(line);
            }
            scanner.close();
        }
        catch (SQLException sql ) {
            assertNotNull(conn);
            sql.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
