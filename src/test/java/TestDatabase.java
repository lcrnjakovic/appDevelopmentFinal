import org.junit.Test;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by lukacrnjakovic on 10/15/17.
 */
public class TestDatabase {

    /**
     * Testing connection to the database
     */
    @Test
    public void testDBConnection(){
        String connSuccess;
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

    /**
     * Testing mysqlcheck output
     * Currently testing valid sql file
     * In order to test empty or invalid files, swap filename and switch boolean values in asserts at the bottom
     */
    @Test
    public void testCreateStatementSyntax(){
        String outputForTesting;
        Connection conn;
        try{
            File script = new File("table.sql");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?verifyServerCertificate=false&useSSL=true","root","student");
            if(!script.exists()){
                outputForTesting = "error: File not found.";
            }
            else if(script.length() < 1){
                outputForTesting = "error: Empty file.";
            }
            else{
                String fileAsString = new Scanner(script).useDelimiter("\\Z").next();
                Statement statement = conn.createStatement();
                Scanner scanner = new Scanner(fileAsString);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    statement.executeUpdate(line);
                }
                scanner.close();
                StringBuilder output = new StringBuilder();
                Process p;
                try {
                    p = Runtime.getRuntime().exec("/usr/local/mysql/bin/mysqlcheck -u root -pstudent -c mydb");
                    p.waitFor();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine())!= null) {
                        output.append(line).append("\n");
                    }

                } catch (Exception e) {
                    outputForTesting = e.toString();
                }
                outputForTesting = output.toString();
            }
        }
        catch (SQLException | FileNotFoundException sql ) {
            outputForTesting = sql.toString();
        }

        // switching the booleans tests for errors and vice versa
        assertEquals(outputForTesting.contains("error"), false);
        assertEquals(outputForTesting.contains("student") || outputForTesting.contains("courses") || outputForTesting.contains("faculty"), true);

    }

}
