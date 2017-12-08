import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukacrnjakovic on 11/10/17.
 */
public class MainTester {
    public static void main(String[] args) throws Exception {
        File fileInput;
        String input1 = "";
        String input2 = "";
        switch(args[0]){
            case "-h": {
                System.out.println("Use -n command line argument before providing a test object " +
                    "and -f before providing a file containing test objects. Two test objects are required!");
            }
                break;
            case "-n": {
                input1 = args[1];
                input2 = args[2];
            }
                break;
            case "-f": {
                if(new File(args[1]).exists()){
                    fileInput = new File(args[1]);
                    try{
                        FileReader fr = new FileReader(fileInput);
                        BufferedReader br = new BufferedReader(fr);
                        String line;
                        List<String> lines = new ArrayList<>();
                        while((line = br.readLine()) != null){
                            lines.add(line);
                        }
                        input1 = lines.get(0);
                        input2 = lines.get(1);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    System.out.println("Please select valid file.");
                }
            }
                break;
            default: {
                input1 = "1|testStyle1";
                input2 = "2|testStyle2";
            }
                break;
        }
        EdgeFieldTest edgeFieldTest = new EdgeFieldTest();
        edgeFieldTest.setUp(input1);
        EdgeTableTest edgeTableTest = new EdgeTableTest();
        edgeTableTest.setUp(input2);

    }
}
