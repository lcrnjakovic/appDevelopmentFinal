import javax.swing.*;

abstract class EdgeConvertCreateDDL {
   String databaseName;
   static String[] products = {"MySQL"};
   EdgeTable[] tables; //master copy of EdgeTable objects
   private EdgeField[] fields; //master copy of EdgeField objects
   int[] numBoundTables;
   int maxBound;
   private StringBuffer sb;
   protected int selected;
   
   EdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
      this.tables = tables;
      this.fields = fields;
      initialize();
   } //EdgeConvertCreateDDL(EdgeTable[], EdgeField[])
   
   EdgeConvertCreateDDL() { //default constructor with empty arg list for to allow output dir to be set before there are table and field objects
      
   } //EdgeConvertCreateDDL()

   private void initialize() {
      numBoundTables = new int[tables.length];
      maxBound = 0;
      sb = new StringBuffer();

      for (int i = 0; i < tables.length; i++) { //step through list of tables
         int numBound = 0; //initialize counter for number of bound tables
         int[] relatedFields = tables[i].getRelatedFieldsArray();
         for (int relatedField : relatedFields) { //step through related fields list
            if (relatedField != 0) {
               numBound++; //count the number of non-zero related fields
            }
         }
         numBoundTables[i] = numBound;
         if (numBound > maxBound) {
            maxBound = numBound;
         }
      }
   }
   
   EdgeTable getTable(int numFigure) {
      for (EdgeTable table : tables) {
         if (numFigure == table.getNumFigure()) {
            return table;
         }
      }
      return null;
   }
   
   EdgeField getField(int numFigure) {
      for (EdgeField field : fields) {
         if (numFigure == field.getNumFigure()) {
            return field;
         }
      }
      return null;
   }

   protected String generateDatabaseName(String dbName) { //prompts user for database name
      //String databaseName = "";

       do {
           databaseName = (String) JOptionPane.showInputDialog(
                   null,
                   "Enter the database name:",
                   "Database Name",
                   JOptionPane.PLAIN_MESSAGE,
                   null,
                   null,
                   dbName);
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

   public abstract String getDatabaseName();

   public abstract String getProductName();
   
   public abstract String getSQLString();
   
   public abstract void createDDL();
   
}//EdgeConvertCreateDDL
