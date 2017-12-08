import java.awt.*;
import javax.swing.*;

public class HelpFrame extends JFrame {

   public HelpFrame(){
      JTabbedPane jtp = new JTabbedPane();
      
      JComponent pane1 = makeTextPanel("Welcome to DDL Generator, a specialized EDG and XML file converter! " + 
         "With this tool you can generate scripts used to create databases from EDG or XML files. " + 
         "To begin, press File->Open Edge File and select an EDG file. Your tables will be loaded into the GUI. ");
      jtp.addTab("Welcome", pane1);
      
      JComponent pane2 = makeTextPanel("After you have done changes in the database, you can also save the file " +
         "which you used to load it, by clicking the Save or Save as buttons in the File tab.");
      jtp.addTab("Saving", pane2);
      
      JComponent pane3 = makeTextPanel("To view the fields of a table, select a table from the All Tables tab " + 
         "by clicking on it. The fields will be shown in the Fields List tab. To change a specific field, " + 
         "select it by clicking on it and change its attributes (file type, varchar length, default value, " + 
         "Primary key,  Allow null) by clicking the buttons on the right. You can set the varchar length or " + 
         "if you have checked any other radio button, then set varchar length will be disabled. " + 
         "User can also set default value. Check generate tab for further instructions. ");
      jtp.addTab("Fields", pane3);
      
      JComponent pane4 = makeTextPanel("To change the relations between tables, press the Define Relations button " + 
      "on the lower left of the screen. A new screen will open in which you can select a table from the Tables " +
      "With Relations tab by clicking on it to see its fields in the Fields in Tables with Relations tab and its " + 
      "related tables in the Related Tables tab. To change the binding of a single field, select the field in the " +
      "Fields in Related Tables tab and press the Bind/Unbind Relation button. Check generate tab for further " +
      "instructions. ");
      jtp.addTab("Relations", pane4);
      
      JComponent pane5 = makeTextPanel("When you are done configuring the tables and fields, generate " +
      "the script by clicking the Create DDL button. If you haven’t set output file definition location, " +
      "the program will prompt you to browse to the desired folder using File Explorer pop-up. " +
      "Selecting the folder is enough, that location will be used to save the output. " +
      "You can set this location beforehand by clicking on Options -> Set Output File Definition Location.");
      jtp.addTab("Output", pane5);
      add(jtp);
      setLocation(100,100);
      setSize(600,400);
      setVisible(true);
   }
   
   protected JComponent makeTextPanel(String text) {
      JPanel panel = new JPanel(false);
      JTextArea textArea = new JTextArea(25, 40);
      textArea.setText(text);
      textArea.setWrapStyleWord(true);
      textArea.setLineWrap(true);
      textArea.setOpaque(false);
      textArea.setEditable(false);
      textArea.setFocusable(false);
      textArea.setBackground(UIManager.getColor("Label.background"));
      textArea.setFont(UIManager.getFont("Label.font"));
      textArea.setBorder(UIManager.getBorder("Label.border"));
      panel.add(textArea);
      return panel;
   }
}