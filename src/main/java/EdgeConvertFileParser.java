import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class EdgeConvertFileParser {
   //private String filename = "test.edg";
   private final File parseFile;
   private BufferedReader br;
   private String currentLine;
   private final ArrayList<EdgeTable> alTables;
   private final ArrayList<EdgeField> alFields;
   private final ArrayList<EdgeConnector> alConnectors;
   private EdgeTable[] tables;
   private EdgeField[] fields;
   private EdgeConnector[] connectors;
   private boolean isEntity, isAttribute, isUnderlined = false;
   private int numFigure;
   private int numConnector;
   //private int numNativeRelatedFields;
   //private int numLine;
   private static final String EDGE_ID = "EDGE Diagram File"; //first line of .edg files should be this
   static final String SAVE_ID = "# EdgeConvert Save File"; //first line of save files should be this
   static final String DELIM = "|";
   private static final String XML_ID = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //first line of .xml files should be this

   EdgeConvertFileParser(File constructorFile) {
      numFigure = 0;
      numConnector = 0;
      alTables = new ArrayList<>();
      alFields = new ArrayList<>();
      alConnectors = new ArrayList<>();
      isEntity = false;
      isAttribute = false;
      parseFile = constructorFile;
      //numLine = 0;
      this.openFile(parseFile);
   }

   /*
    * while loop cita iz filea
    * cita dio po dio
    * samo style bitan
    * ako ima relation, breaka
    * bitni samo entity i attribute
    */
   private void parseEdgeFile() throws IOException {
      while ((currentLine = br.readLine()) != null) {
         currentLine = currentLine.trim();
         if (currentLine.startsWith("Figure ")) { //this is the start of a Figure entry
            numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Figure number
            currentLine = br.readLine().trim(); // this should be "{"
            currentLine = br.readLine().trim();
            if (!currentLine.startsWith("Style")) { // this is to weed out other Figures, like Labels
               continue;
            } else {
               String style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""));
               if (style.startsWith("Relation")) { //presence of Relations implies lack of normalization
                  JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains relations.  Please resolve them and try again.");
                  EdgeConvertGUI.setReadSuccess(false);
                  break;
               }
               if (style.startsWith("Entity")) {
                  isEntity = true;
               }
               if (style.startsWith("Attribute")) {
                  isAttribute = true;
               }
               if (!(isEntity || isAttribute)) { //these are the only Figures we're interested in
                  continue;
               }
               currentLine = br.readLine().trim(); //this should be Text
               String text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")).replaceAll(" ", "");
               if (text.equals("")) {
                  JOptionPane.showMessageDialog(null, "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
                  EdgeConvertGUI.setReadSuccess(false);
                  break;
               }
               int escape = text.indexOf("\\");
               if (escape > 0) { //Edge denotes a line break as "\line", disregard anything after a backslash
                  text = text.substring(0, escape);
               }

               do { //advance to end of record, look for whether the text is underlined
                  currentLine = br.readLine().trim();
                  if (currentLine.startsWith("TypeUnderl")) {
                     isUnderlined = true;
                  }
               } while (!currentLine.equals("}")); // this is the end of a Figure entry

               if (isEntity) { //create a new EdgeTable object and add it to the alTables ArrayList
                  if (isTableDup(text)) {
                     JOptionPane.showMessageDialog(null, "There are multiple tables called " + text + " in this diagram.\nPlease rename all but one of them and try again.");
                     EdgeConvertGUI.setReadSuccess(false);
                     break;
                  }
                  alTables.add(new EdgeTable(numFigure + DELIM + text));
               }
               if (isAttribute) { //create a new EdgeField object and add it to the alFields ArrayList
                  EdgeField tempField = new EdgeField(numFigure + DELIM + text);
                  tempField.setIsPrimaryKey(isUnderlined);
                  alFields.add(tempField);
               }
               //reset flags
               isEntity = false;
               isAttribute = false;
               isUnderlined = false;
            }
         } // if("Figure")
         if (currentLine.startsWith("Connector ")) { //this is the start of a Connector entry
            numConnector = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Connector number
            currentLine = br.readLine().trim(); // this should be "{"
            currentLine = br.readLine().trim(); // not interested in Style
            currentLine = br.readLine().trim(); // Figure1
            int endPoint1 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
            currentLine = br.readLine().trim(); // Figure2
            int endPoint2 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
            currentLine = br.readLine().trim(); // not interested in EndPoint1
            currentLine = br.readLine().trim(); // not interested in EndPoint2
            currentLine = br.readLine().trim(); // not interested in SuppressEnd1
            currentLine = br.readLine().trim(); // not interested in SuppressEnd2
            currentLine = br.readLine().trim(); // End1
            String endStyle1 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""));
            currentLine = br.readLine().trim(); // End2
            String endStyle2 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""));
            do { //advance to end of record
               currentLine = br.readLine().trim();
            } while (!currentLine.equals("}")); // this is the end of a Connector entry

            alConnectors.add(new EdgeConnector(numConnector + DELIM + endPoint1 + DELIM + endPoint2 + DELIM + endStyle1 + DELIM + endStyle2));
         } // if("Connector")
      } // while()
   } // parseEdgeFile()

   private void resolveConnectors() { //Identify nature of Connector endpoints
      int endPoint1, endPoint2;
      int fieldIndex, table1Index = 0, table2Index = 0;
      for (EdgeConnector connector : connectors) {
         endPoint1 = connector.getEndPoint1();
         endPoint2 = connector.getEndPoint2();
         fieldIndex = -1;
         for (int fIndex = 0; fIndex < fields.length; fIndex++) { //search fields array for endpoints
            if (endPoint1 == fields[fIndex].getNumFigure()) { //found endPoint1 in fields array
               connector.setIsEP1Field(true); //set appropriate flag
               fieldIndex = fIndex; //identify which element of the fields array that endPoint1 was found in
            }
            if (endPoint2 == fields[fIndex].getNumFigure()) { //found endPoint2 in fields array
               connector.setIsEP2Field(true); //set appropriate flag
               fieldIndex = fIndex; //identify which element of the fields array that endPoint2 was found in
            }
         }
         for (int tIndex = 0; tIndex < tables.length; tIndex++) { //search tables array for endpoints
            if (endPoint1 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
               connector.setIsEP1Table(true); //set appropriate flag
               table1Index = tIndex; //identify which element of the tables array that endPoint1 was found in
            }
            if (endPoint2 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
               connector.setIsEP2Table(true); //set appropriate flag
               table2Index = tIndex; //identify which element of the tables array that endPoint2 was found in
            }
         }

            if (connector.getIsEP1Field() && connector.getIsEP2Field()) { //both endpoints are fields, implies lack of normalization
                JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains composite attributes. Please resolve them and try again.");
                EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                break; //stop processing list of Connectors
            }

            if (connector.getIsEP1Table() && connector.getIsEP2Table()) { //both endpoints are tables
                if ((connector.getEndStyle1().contains("many")) &&
                        (connector.getEndStyle2().contains("many"))) { //the connector represents a many-many relationship, implies lack of normalization
                    JOptionPane.showMessageDialog(null, "There is a many-many relationship between tables\n\"" + tables[table1Index].getName() + "\" and \"" + tables[table2Index].getName() + "\"" + "\nPlease resolve this and try again.");
                    EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                    break; //stop processing list of Connectors
                } else { //add Figure number to each table's list of related tables
                    tables[table1Index].addRelatedTable(tables[table2Index].getNumFigure());
                    tables[table2Index].addRelatedTable(tables[table1Index].getNumFigure());
                    continue; //next Connector
                }
            }

         if (fieldIndex >= 0 && fields[fieldIndex].getTableID() == 0) { //field has not been assigned to a table yet
            if (connector.getIsEP1Table()) { //endpoint1 is the table
               tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
               fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); //tell the field what table it belongs to
            } else { //endpoint2 is the table
               tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
               fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); //tell the field what table it belongs to
            }
         } else if (fieldIndex >= 0) { //field has already been assigned to a table
            JOptionPane.showMessageDialog(null, "The attribute " + fields[fieldIndex].getName() + " is connected to multiple tables.\nPlease resolve this and try again.");
            EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
            break; //stop processing list of Connectors
         }
      } // connectors for() loop
   } // resolveConnectors()

   private void parseSaveFile() throws IOException {
      StringTokenizer stTables, stNatFields, stRelFields, stField;
      // String stNatRelFields;
      EdgeTable tempTable;
      EdgeField tempField;
      currentLine = br.readLine();
      currentLine = br.readLine(); //this should be "Table: "
      while (currentLine.startsWith("Table: ")) {
         numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Table number
         currentLine = br.readLine(); //this should be "{"
         currentLine = br.readLine(); //this should be "TableName"
         String tableName = currentLine.substring(currentLine.indexOf(" ") + 1);
         tempTable = new EdgeTable(numFigure + DELIM + tableName);

         currentLine = br.readLine(); //this should be the NativeFields list
         stNatFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
         int numFields = stNatFields.countTokens();
         for (int i = 0; i < numFields; i++) {
            tempTable.addNativeField(Integer.parseInt(stNatFields.nextToken()));
         }

         currentLine = br.readLine(); //this should be the RelatedTables list
         stTables = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
         int numTables = stTables.countTokens();
         for (int i = 0; i < numTables; i++) {

            tempTable.addRelatedTable(Integer.parseInt(stTables.nextToken()));
         }
         tempTable.makeArrays();

         currentLine = br.readLine(); //this should be the RelatedFields list
         stRelFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
         numFields = stRelFields.countTokens();

         for (int i = 0; i < numFields; i++) {
            tempTable.setRelatedField(i, Integer.parseInt(stRelFields.nextToken()));
         }

         alTables.add(tempTable);
         currentLine = br.readLine(); //this should be "}"
         currentLine = br.readLine(); //this should be "\n"
         currentLine = br.readLine(); //this should be either the next "Table: ", #Fields#
      }
      while ((currentLine = br.readLine()) != null) {
         stField = new StringTokenizer(currentLine, DELIM);
         numFigure = Integer.parseInt(stField.nextToken());
         String fieldName = stField.nextToken();
         tempField = new EdgeField(numFigure + DELIM + fieldName);
         tempField.setTableID(Integer.parseInt(stField.nextToken()));
         tempField.setTableBound(Integer.parseInt(stField.nextToken()));
         tempField.setFieldBound(Integer.parseInt(stField.nextToken()));
         tempField.setDataType(Integer.parseInt(stField.nextToken()));
         tempField.setVarcharValue(Integer.parseInt(stField.nextToken()));
         tempField.setIsPrimaryKey(Boolean.valueOf(stField.nextToken()));
         tempField.setDisallowNull(Boolean.valueOf(stField.nextToken()));
         if (stField.hasMoreTokens()) { //Default Value may not be defined
            tempField.setDefaultValue(stField.nextToken());
         }
         alFields.add(tempField);
      }
   } // parseSaveFile()

   private void makeArrays() { //convert ArrayList objects into arrays of the appropriate Class type
      if (alTables != null) {
         tables = alTables.toArray(new EdgeTable[alTables.size()]);
      }
      if (alFields != null) {
         fields = alFields.toArray(new EdgeField[alFields.size()]);
      }
      if (alConnectors != null) {
         connectors = alConnectors.toArray(new EdgeConnector[alConnectors.size()]);
      }
   }

   private boolean isTableDup(String testTableName) {
      for (EdgeTable tempTable : alTables) {
         if (tempTable.getName().equals(testTableName)) {
            return true;
         }
      }
      return false;
   }

   EdgeTable[] getEdgeTables() {
      return tables;
   }

   EdgeField[] getEdgeFields() {
      return fields;
   }

   private void parseXMLFile(File inputFile) {
      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("table");
         int numFields = 0;
         for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

               Element eElement = (Element) nNode;
               numFigure++;
               int numTable = Integer.valueOf(eElement.getAttribute("id"));
               NodeList fields = eElement.getElementsByTagName("field");

               EdgeTable tempTable = new EdgeTable(numTable + DELIM + eElement.getAttribute("name"));
               for (int i = 0; i < fields.getLength(); i++) {
                  Element f = (Element) fields.item(i);
                  // System.out.println(numFigure);
                  EdgeField tempField = new EdgeField(numFigure + DELIM + f.getAttribute("name"));
                  System.out.println(tempField);
                  tempField.setTableID(numTable);

                  String dataType = f.getAttribute("type");
                  if (dataType.equals("VARCHAR")) {
                     tempField.setDataType(0);
                     tempField.setVarcharValue(Integer.valueOf(f.getAttribute("length")));
                  } else if (dataType.equals("BOOL")) {
                     tempField.setDataType(1);
                  } else if (dataType.equals("INTEGER")) {
                     tempField.setDataType(2);
                  } else if (dataType.equals("DOUBLE")) {
                     tempField.setDataType(3);
                  }
                  // set constraints


                  alFields.add(tempField);

                  tempTable.addNativeField(tempField.getNumFigure());

                  //EdgeConnector edgeConnector = new EdgeConnector(numFigure + DELIM + num + DELIM + numFigure + DELIM + "null" + DELIM + "null");
                  // alConnectors.add(edgeConnector);
                  numFigure++;
                  numFields++;
               }
//               for(int h = 1; h<=nList.getLength();h++){
//                  if(tempTable.getNumFigure()!=h)
//                     tempTable.addRelatedTable(h);
//               }

               if(!eElement.getAttribute("relatedTables").equals(""))
                  tempTable.addRelatedTable(Integer.valueOf(eElement.getAttribute("relatedTables")));
               tempTable.makeArrays();

               alTables.add(tempTable);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

    }

    private void openFile(File inputFile) {
        try {
            FileReader fr = new FileReader(inputFile);
            br = new BufferedReader(fr);
            //test for what kind of file we have
            //System.out.println(br.readLine());
            currentLine = br.readLine().trim();

            //numLine++;

         if (currentLine.startsWith(XML_ID)) { //the file chosen is an Edge Diagrammer file
            this.parseXMLFile(inputFile); //parse the file
            br.close();
            this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
            this.resolveConnectors(); //Identify nature of Connector endpoints
         } else {
            if (currentLine.startsWith(EDGE_ID)) { //the file chosen is an Edge Diagrammer file
               this.parseEdgeFile(); //parse the file
               br.close();
               this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
               this.resolveConnectors(); //Identify nature of Connector endpoints
            } else {
               if (currentLine.startsWith(SAVE_ID)) { //the file chosen is a Save file created by this application
                  this.parseSaveFile(); //parse the file
                  br.close();
                  this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
               } else { //the file chosen is something else
                  JOptionPane.showMessageDialog(null, "Unrecognized file format");
               }
            }
         }
      } // try

      catch (FileNotFoundException fnfe) {
         System.out.println("Cannot find \"" + inputFile.getName() + "\".");
         System.exit(0);
      } // catch FileNotFoundException
      catch (IOException ioe) {
         System.out.println(ioe.toString());
         System.exit(0);
      } // catch IOException
      catch (Exception e) {
         e.printStackTrace();
      }
   } // openFile()
} // EdgeConvertFileHandler