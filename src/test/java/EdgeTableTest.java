import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class EdgeTableTest {
        EdgeTable testObj;
        private String name;
        private int numFigure;
        private int[] relatedTables, relatedFields, nativeFields;

	@Before
	public void setUp(String edgeTableParam) throws Exception {
		StringTokenizer st = new StringTokenizer(edgeTableParam, "|");
      	numFigure = Integer.parseInt(st.nextToken());
      	name = st.nextToken();
        testObj = new EdgeTable(edgeTableParam);
        runner();
	}
	
	public void runner() {
        testGetNumFigure();
        testGetName();
        testGetRelatedTablesArray();
        testGetRelatedFieldsArray();
        testGetNativeFieldsArray();
	}

	@Test
	public void testGetNumFigure() {
		assertEquals("numFigure was initialized to " + numFigure + " so it should be " + numFigure, numFigure
			,testObj.getNumFigure());
	}

	@Test
	public void testGetName() {
		assertEquals("name was initialized to " + name, name, testObj.getName());
	}

	@Test
	public void testGetRelatedTablesArray() {
		assertEquals("relatedTables was initialized to " + relatedTables, relatedTables,testObj.getRelatedTablesArray());
	}

	@Test
	public void testGetRelatedFieldsArray() {
		assertEquals("relatedFields was initialized to " + relatedFields, relatedFields, testObj.getRelatedFieldsArray());
	}	

	@Test
	public void testGetNativeFieldsArray() {
		assertEquals("nativeFields was initialized to " + nativeFields, nativeFields, testObj.getNativeFieldsArray());
	}	

}
