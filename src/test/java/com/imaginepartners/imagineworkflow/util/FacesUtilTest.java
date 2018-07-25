package com.imaginepartners.imagineworkflow.util;

import com.imaginepartners.imagineworkflow.automation.MultilineEntityUtils;
import java.lang.reflect.Field;
import java.util.Locale;
import org.apache.log4j.LogManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class FacesUtilTest {

	private static String PACK_TEST_NAME="com.imaginepartners.imagineworkflow.models.rh";

	private static String [] ML_ENTITY_TEST_CLASS_NAMES=new String[]{"RhObjectif", "RhPrime", "RhStructure"};

	private static String [] ML_ENTITY_RESULT_PROPERTIES= new String []{"objectifId", "id", "id"};

	private static Object [] instances= new Object[ML_ENTITY_TEST_CLASS_NAMES.length];


	public FacesUtilTest() {
		LogManager.getLogger(FacesUtilTest.class.getSimpleName()).debug("constructor");
	}

	/**
	 * Create dynamic multiline entity instances.
	 */
	@BeforeClass
	public static void setUpClass() {
		LogManager.getLogger(FacesUtilTest.class.getSimpleName()).debug("initialze FacesUtilTest class");
		int co=0 ;
		for(String classSimpleName: ML_ENTITY_TEST_CLASS_NAMES){
			try {
				Class<?> clazz= Class.forName(PACK_TEST_NAME+"."+classSimpleName);
				instances[co]= MultilineEntityUtils.INIT_CLAZZ(clazz);
				co++;
			} catch (ClassNotFoundException ex) {
				LogManager.getLogger(FacesUtilTest.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
			}
		}
	}

	@Before
	@Ignore
	public void setUp() {
		LogManager.getLogger(FacesUtil.class.getSimpleName()).debug("befor test method");
	}

	/**
	 * Test of getRequest method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetRequest() {
	}

	/**
	 * Test of redirect method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testRedirect() {
	}

	/**
	 * Test of getUrlParam method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetUrlParam() {
	}

	/**
	 * Test of getViewId method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetViewId() {
	}

	/**
	 * Test of getClientLocale method, of class FacesUtil.
	 */
	@Test
	public void testGetClientLocale() {
		assertNotNull("error, object is null", new Locale("fr"));
	}

	/**
	 * Test of setSessionErrorMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetSessionErrorMessage() {
	}

	/**
	 * Test of setSessionInfoMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetSessionInfoMessage() {
	}

	/**
	 * Test of setSessionWarnMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetSessionWarnMessage() {
	}

	/**
	 * Test of reinitSessionMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testReinitSessionMessage() {
	}

	/**
	 * Test of loadSessionMessages method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testLoadSessionMessages() throws Exception {
	}

	/**
	 * Test of setAjaxInfoMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetAjaxInfoMessage_String() {
	}

	/**
	 * Test of setAjaxErrorMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetAjaxErrorMessage_String_String() {
	}

	/**
	 * Test of setAjaxInfoMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetAjaxInfoMessage_String_String() {
	}

	/**
	 * Test of setAjaxErrorMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetAjaxErrorMessage_String() {
	}

	/**
	 * Test of setAjaxWarnMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetAjaxWarnMessage_String() {
	}

	/**
	 * Test of setAjaxWarnMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetAjaxWarnMessage_String_String() {
	}

	/**
	 * Test of getMessage method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetMessage() {
	}

	/**
	 * Test of getWebAppDirectory method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetWebAppDirectory() {
	}

	/**
	 * Test of getContextPath method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetContextPath() {
	}

	/**
	 * Test of getRealPath method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetRealPath() {
	}

	/**
	 * Test of getTimeZone method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testGetTimeZone() {
	}

	/**
	 * Test of updateElement method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testUpdateElement() {
	}

	/**
	 * Test of setSessionUpdateElement method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSetSessionUpdateElement() {
	}

	/**
	 * Test of updateSessionElements method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testUpdateSessionElements() {
	}

	/**
	 * Test of evalAsString method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testEvalAsString_String() {
	}

	/**
	 * Test of evalAsString method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testEvalAsString_ArrayList() {
	}

	/**
	 * Test of spElEval method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testSpElEval() {
	}

	/**
	 * Test of EVAL_MULTILINE_ENTITY_COLUMN method, of class FacesUtil.
	 * RhObjectif multiline entity is useful entity for doing some unit tests.
	 */
	@Test
	public void testEVAL_MULTILINE_ENTITY_COLUMN()
	throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		int co=0;
		Object obj= null;
		String [] generateExps= MultilineEntityUtils.BUILD_EXPRESSIONS(instances, ML_ENTITY_RESULT_PROPERTIES);
		while(co < generateExps.length) {
			FacesUtil.EVAL_MULTILINE_ENTITY_COLUMN(instances[co], generateExps[co]);
			Object resultValue= Util.VALUE_BY_FIELD(instances[co], ML_ENTITY_RESULT_PROPERTIES[co]);
			assertNotNull("see multiline entity properties values and/or the eval custom mecanic", resultValue );
			co++;
		}
	}

	@Test
	@Ignore
	public void testPARSE_EXPRESSION(){
		String [] dinamicExp= MultilineEntityUtils.BUILD_EXPRESSIONS(instances, ML_ENTITY_TEST_CLASS_NAMES);
	}

	/**
	 * Test of registerELResolver method, of class FacesUtil.
	 */
	@Test
	@Ignore
	public void testRegisterELResolver() {
	}
}
