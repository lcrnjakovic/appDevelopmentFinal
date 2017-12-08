import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class EdgeFieldTest {
    EdgeField testObj;
	int numFigure;
	String name;
	
    @Before
    public void setUp(String inputString) throws Exception {
        StringTokenizer st = new StringTokenizer(inputString, "|");
      	numFigure = Integer.parseInt(st.nextToken());
      	name = st.nextToken();
        testObj = new EdgeField(inputString);
        runner();
    }

    public void runner() {
        testGetNumFigure();
        testGetName();
        testGetTableID();
        testSetTableID();
        testGetTableBound();
        testSetTableBound();
        testGetFieldBound();
        testSetFieldBound();
        testGetDisallowNull();
        testSetDisallowNull();
        testGetIsPrimaryKey();
        testSetIsPrimaryKey();
        testGetDefaultValue();
        testSetDefaultValue();
        testGetVarcharValue();
        testSetVarcharValue();
        testGetDataType();
        testSetDataType();
        testGetStrDataType();
    }

    @Test
    public void testGetNumFigure() {
        assertEquals("numFigure should be derived from inputString and it should be " + numFigure, numFigure,testObj.getNumFigure());
    }

    @Test
    public void testGetName() {
        assertEquals("name should be derived from inputString so it should be ' " + name + "'", name,testObj.getName());
    }

    @Test
    public void testGetTableID() {
        assertEquals("Table ID was intialized as 0 so should be 0",0,testObj.getTableID());
    }

    @Test
    public void testSetTableID() {
        testObj.setTableID(2);
        assertEquals("getTableID should return 2", 2,testObj.getTableID());
    }

    @Test
    public void testGetTableBound() {
        assertEquals("TableBound was intialized as 0 so should be 0",0,testObj.getTableBound());
    }

    @Test
    public void testSetTableBound() {
        testObj.setTableBound(3);
        assertEquals("getTableBound should return 3", 3,testObj.getTableBound());
    }

    @Test
    public void testGetFieldBound() {
        assertEquals("FieldBound was intialized as 0 so should be 0",0,testObj.getFieldBound());
    }

    @Test
    public void testSetFieldBound() {
        testObj.setFieldBound(4);
        assertEquals("getFieldBound should return 4", 4,testObj.getFieldBound());
    }

    @Test
    public void testGetDisallowNull() {
        assertEquals("getDisallowNull should be false",false,testObj.getDisallowNull());
    }

    @Test
    public void testSetDisallowNull() {
        testObj.setDisallowNull(true);
        assertEquals("getDisallowNull should be true",true,testObj.getDisallowNull());
    }

    @Test
    public void testGetIsPrimaryKey() {
        assertEquals("getIsPrimaryKey should be false",false,testObj.getIsPrimaryKey());
    }

    @Test
    public void testSetIsPrimaryKey() {
        testObj.setIsPrimaryKey(true);
        assertEquals("getIsPrimaryKey should be true",true,testObj.getIsPrimaryKey());
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals("getDefaultValue should be empty string.","",testObj.getDefaultValue());
    }

    @Test
    public void testSetDefaultValue() {
        testObj.setDefaultValue("Test");
        assertEquals("getDefaultValue should be equals to 'Test'","Test",testObj.getDefaultValue());
    }

    @Test
    public void testGetVarcharValue() {
        assertEquals("getVarcharValue should be 1", 1,testObj.getVarcharValue());
    }

    @Test
    public void testSetVarcharValue() {
        testObj.setVarcharValue(2);
        assertEquals("getVarcharValue should be equals to 2", 2,testObj.getVarcharValue());
    }

    @Test
    public void testGetDataType() {
        assertEquals("getDataType should be 0", 0,testObj.getDataType());
    }

    @Test
    public void testSetDataType() {
        testObj.setDataType(1);
        assertEquals("getDataType should be equals to 1", 1,testObj.getDataType());
    }

    @Test
    public void testGetStrDataType() {
        String[] strDataTypeInit = {"Varchar", "Boolean", "Integer", "Double"};
        assertEquals("strDataType not equals to init value", strDataTypeInit,testObj.getStrDataType());
    }

}
