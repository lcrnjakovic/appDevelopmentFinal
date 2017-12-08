import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EdgeConnectorTest {
        EdgeConnector testObj;

	@Before
	public void setUp() throws Exception {
        testObj = new EdgeConnector("1|2|3|testStyle1|testStyle2");
        runner();
	}
	
	public void runner() {
        testGetNumConnector();
        testGetEndPoint1();
        testGetEndPoint2();
        testGetEndStyle1();
        testGetEndStyle2();
        //testGetIsEP1Field();
        testGetIsEP2Field();
        testGetIsEP1Table();
        //testGetIsEP2Table();
        //testSetIsEP1Field();
        //testSetIsEP2Field();
        //testSetIsEP1Table(); 
        //testSetIsEP2Table();

	}

	@Test
	public void testGetNumConnector() {
		assertEquals("numConnector was intialized to 1 so it should be 1",1,testObj.getNumConnector());
	}

	@Test
	public void testGetEndPoint1() {
		assertEquals("EndPoint1 was intialized to 2",2,testObj.getEndPoint1());
	}

	@Test
	public void testGetEndPoint2() {
		assertEquals("EndPoint2 was intialized as 3",3,testObj.getEndPoint2());
	}

	@Test
	public void testGetEndStyle1() {
		assertEquals("endStyle1 was intilized to \"testStyle1\"","testStyle1",testObj.getEndStyle1());
	}

	@Test
	public void testGetEndStyle2() {
		assertEquals("endStyle1 was intilized to \"testStyle1\"","testStyle2",testObj.getEndStyle2());
	}

	@Test
	public void testGetIsEP1Field() {
		assertEquals("isEP1Field should be false",false,testObj.getIsEP1Field());
	}

	@Test
	public void testGetIsEP2Field() {
		assertEquals("IsEP2Field should be false",false,testObj.getIsEP2Field());
	}

	@Test
	public void testGetIsEP1Table() {
		assertEquals("isEP1Table should be false",false,testObj.getIsEP1Table());
	}

	@Test
	public void testGetIsEP2Table() {
		assertEquals("isEP2Table should be false",false,testObj.getIsEP2Table());
	}

	@Test
	public void testSetIsEP1Field() {
		testObj.setIsEP1Field(true);
                assertEquals("isEP1Filed should be what you set it to",true,testObj.getIsEP1Field());
	}

	@Test
	public void testSetIsEP2Field() {
		testObj.setIsEP2Field(true);
                assertEquals("isEP2Filed should be what you set it to",true,testObj.getIsEP2Field());
	}

	@Test
	public void testSetIsEP1Table() {
		testObj.setIsEP1Table(true);
                assertEquals("isEp1Table should be what you set it to",true,testObj.getIsEP1Table());
	}

	@Test
	public void testSetIsEP2Table() {
		testObj.setIsEP2Table(true);
                assertEquals("isEp2Table should be what you set it to",true,testObj.getIsEP2Table());
	}

}
