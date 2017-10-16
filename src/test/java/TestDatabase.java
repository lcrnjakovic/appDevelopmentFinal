import org.junit.Test;

import java.io.*;
import java.sql.*;

import static org.junit.Assert.assertEquals;

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
    public void testScriptSyntax(){
        try{
            DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?verifyServerCertificate=false&useSSL=true","root","student");
            InputStream is = new FileInputStream("table.sql");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileAsString = sb.toString();
            System.out.println(fileAsString);
            Statement statement = null;
            ResultSet rs = statement.executeQuery(fileAsString);

        }
        catch (SQLException sql ) {
            sql.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //assertEquals("Error", connSuccess);

    }

}
