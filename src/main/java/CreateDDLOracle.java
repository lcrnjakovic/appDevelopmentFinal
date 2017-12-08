import javax.swing.*;

/**
 * Created by lukacrnjakovic on 12/8/17.
 */
public class CreateDDLOracle extends EdgeConvertCreateDDL {
    private String databaseName;
    //this array is for determining how MySQL refers to datatypes
    private final String[] strDataType = {"VARCHAR", "BOOL", "INT", "DOUBLE"};
    private StringBuffer sb;

    public CreateDDLOracle(EdgeTable[] inputTables, EdgeField[] inputFields) {
        super(inputTables, inputFields);
        sb = new StringBuffer();
    } //CreateDDLOracle(EdgeTable[], EdgeField[])

    public CreateDDLOracle() { //default constructor with empty arg list for to allow output dir to be set before there are table and field objects

    }

    public void createDDL() {
        EdgeConvertGUI.setReadSuccess(true);
        databaseName = generateDatabaseName();
        for (int boundCount = 0; boundCount <= maxBound; boundCount++) { //process tables in order from least dependent (least number of bound tables) to most dependent
            for (int tableCount = 0; tableCount < numBoundTables.length; tableCount++) { //step through list of tables
                if (numBoundTables[tableCount] == boundCount) { //
                    sb.append("CREATE TABLE ").append(tables[tableCount].getName()).append(" (\r\n");
                    int[] nativeFields = tables[tableCount].getNativeFieldsArray();
                    int[] relatedFields = tables[tableCount].getRelatedFieldsArray();
                    boolean[] primaryKey = new boolean[nativeFields.length];
                    int numPrimaryKey = 0;
                    int numForeignKey = 0;
                    String changeFieldName = "";
                    for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) { //print out the fields
                        EdgeField currentField = getField(nativeFields[nativeFieldCount]);
                        String tableName = tables[tableCount].getName();
                        if(!currentField.getName().toUpperCase().contains(tableName.toUpperCase().substring(0,2))){
                            changeFieldName = Character.toUpperCase(tableName.charAt(0)) + tableName.substring(1).toLowerCase();
                        }
                        sb.append("\t").append(changeFieldName).append(currentField.getName()).append(" ").append(strDataType[currentField.getDataType()]);
                        if (currentField.getDataType() == 0) { //varchar
                            sb.append("(").append(currentField.getVarcharValue()).append(")"); //append varchar length in () if data type is varchar
                        }
                        if (currentField.getDisallowNull()) {
                            sb.append(" NOT NULL");
                        }
                        if (!currentField.getDefaultValue().equals("")) {
                            if (currentField.getDataType() == 1) { //boolean data type
                                sb.append(" DEFAULT ").append(convertStrBooleanToInt(currentField.getDefaultValue()));
                            } else { //any other data type
                                sb.append(" DEFAULT ").append(currentField.getDefaultValue());
                            }
                        }
                        if (currentField.getIsPrimaryKey()) {
                            primaryKey[nativeFieldCount] = true;
                            numPrimaryKey++;
                        } else {
                            primaryKey[nativeFieldCount] = false;
                        }
                        if (currentField.getFieldBound() != 0) {
                            numForeignKey++;
                        }
                        if(nativeFieldCount < (nativeFields.length-1) || numPrimaryKey > 0 || numForeignKey > 0) {
                            sb.append(",\r\n"); //end of field
                        }
                        else{
                            sb.append("\r\n");
                        }
                    }
                    if (numPrimaryKey > 0) { //table has primary key(s)
                        sb.append("CONSTRAINT ").append(tables[tableCount].getName()).append("_PK PRIMARY KEY (");
                        for (int i = 0; i < primaryKey.length; i++) {
                            if (primaryKey[i]) {
                                sb.append(changeFieldName).append(getField(nativeFields[i]).getName());
                                numPrimaryKey--;
                                if (numPrimaryKey > 0) {
                                    sb.append(", ");
                                }
                            }
                        }
                        sb.append(")");
                        if (numForeignKey > 0) {
                            sb.append(",");
                        }
                        sb.append("\r\n");
                    }
                    if (numForeignKey > 0) { //table has foreign keys
                        int currentFK = 1;
                        for (int i = 0; i < relatedFields.length; i++) {
                            if (relatedFields[i] != 0) {
                                sb.append("CONSTRAINT ").append(tables[tableCount].getName()).append("_FK").append(currentFK).append(" FOREIGN KEY(").append(changeFieldName).append(getField(nativeFields[i]).getName()).append(") REFERENCES ").append(getTable(getField(nativeFields[i]).getTableBound()).getName()).append("(").append(getField(relatedFields[i]).getName()).append(")");
                                if (currentFK < numForeignKey) {
                                    sb.append(",\r\n");
                                }
                                currentFK++;
                            }
                        }
                        sb.append("\r\n");
                    }
                    sb.append(");\r\n\r\n"); //end of table
                }
            }
        }
    }

    private int convertStrBooleanToInt(String input) { //MySQL uses '1' and '0' for boolean types
        if (input.equals("true")) {
            return 1;
        } else {
            return 0;
        }
    }

    private String generateDatabaseName() { //prompts user for database name
        String dbNameDefault = "OracleDB";
        //String databaseName = "";

        do {
            databaseName = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter the database name:",
                    "Database Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    dbNameDefault);
            if (databaseName == null) {
                EdgeConvertGUI.setReadSuccess(false);
                return "";
            }
            if (databaseName.equals("")) {
                JOptionPane.showMessageDialog(null, "You must select a name for your database.");
            }
        } while (databaseName.equals(""));
        return databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getProductName() {
        return "Oracle";
    }

    public String getSQLString() {
        createDDL();
        return sb.toString();
    }
}
