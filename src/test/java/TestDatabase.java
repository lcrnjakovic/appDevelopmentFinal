import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by lukacrnjakovic on 10/15/17.
 */
public class TestDatabase {

    @Test
    public void testScriptSyntax(){
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

}
