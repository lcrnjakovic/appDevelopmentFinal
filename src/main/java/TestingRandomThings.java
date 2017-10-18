import java.io.*;
import java.sql.*;

/**
 * Created by lukacrnjakovic on 10/16/17.
 */
public class TestingRandomThings {
    public static void main(String[] args){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?verifyServerCertificate=false&useSSL=true","root","student");
            Statement statement = conn.createStatement();
            statement.executeUpdate("drop table if exists foo");
            statement.executeUpdate("create table foo(bar int, bas varchar(25))");

        }
        catch (SQLException sql ) {
            sql.printStackTrace();
        }
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec("/usr/local/mysql/bin/mysqlcheck -u root -pstudent -c mydb");
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(output.toString());


    }
}
