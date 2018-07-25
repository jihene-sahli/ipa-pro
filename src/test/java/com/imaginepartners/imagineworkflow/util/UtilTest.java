/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.util;

import java.io.File;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author ibrahimhammani
 */
public class UtilTest {

    public UtilTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Ignore of getRequest method, of class Util.
     */
    @Ignore
    public void testGetRequest() {
        System.out.println("getRequest");
        HttpServletRequest expResult = null;
        HttpServletRequest result = FacesUtil.getRequest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of redirect method, of class Util.
     */
    @Ignore
    public void testRedirect() {
        System.out.println("redirect");
        String url = "";
        FacesUtil.redirect(url);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of getUrlParam method, of class Util.
     */
    @Ignore
    public void testGetUrlParam() {
        System.out.println("getUrlParam");
        String param = "";
        String expResult = "";
        String result = FacesUtil.getUrlParam(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of getViewId method, of class Util.
     */
    @Ignore
    public void testGetViewId() {
        System.out.println("getViewId");
        String expResult = "";
        String result = FacesUtil.getViewId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of getClientLocale method, of class Util.
     */
    @Ignore
    public void testGetClientLocale() {
        System.out.println("getClientLocale");
        Locale expResult = null;
        Locale result = FacesUtil.getClientLocale();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of setSessionErrorMessage method, of class Util.
     */
    @Ignore
    public void testSetSessionErrorMessage() {
        System.out.println("setSessionErrorMessage");
        String exMessage = "";
        FacesUtil.setSessionErrorMessage(exMessage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of setSessionInfoMessage method, of class Util.
     */
    @Ignore
    public void testSetSessionInfoMessage() {
        System.out.println("setSessionInfoMessage");
        String exMessage = "";
        FacesUtil.setSessionInfoMessage(exMessage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of reinitSessionMessage method, of class Util.
     */
    @Ignore
    public void testReinitSessionMessage() {
        System.out.println("reinitSessionMessage");
        FacesUtil.reinitSessionMessage();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of loadSessionMessages method, of class Util.
     */
    @Ignore
    public void testLoadSessionMessages() throws Exception {
        System.out.println("loadSessionMessages");
        boolean update = false;
        FacesUtil.loadSessionMessages(update);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of setAjaxInfoMessage method, of class Util.
     */
    @Ignore
    public void testSetAjaxInfoMessage_String() {
        System.out.println("setAjaxInfoMessage");
        String exMessage = "";
        FacesUtil.setAjaxInfoMessage(exMessage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of setAjaxErrorMessage method, of class Util.
     */
    @Ignore
    public void testSetAjaxErrorMessage_String_String() {
        System.out.println("setAjaxErrorMessage");
        String elementId = "";
        String exMessage = "";
        FacesUtil.setAjaxErrorMessage(elementId, exMessage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of setAjaxInfoMessage method, of class Util.
     */
    @Ignore
    public void testSetAjaxInfoMessage_String_String() {
        System.out.println("setAjaxInfoMessage");
        String elementId = "";
        String exMessage = "";
        FacesUtil.setAjaxInfoMessage(elementId, exMessage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of setAjaxErrorMessage method, of class Util.
     */
    @Ignore
    public void testSetAjaxErrorMessage_String() {
        System.out.println("setAjaxErrorMessage");
        String exMessage = "";
        FacesUtil.setAjaxErrorMessage(exMessage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of getMessage method, of class Util.
     */
    @Ignore
    public void testGetMessage() {
        System.out.println("getMessage");
        String key = "";
        String[] param = null;
        String expResult = "";
        String result = FacesUtil.getMessage(key, param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Ignore of getWebAppDirectory method, of class Util.
     */
    @Ignore
    public void testGetWebAppDirectory() {
        System.out.println("getWebAppDirectory");
        String expResult = "/home/ibrahimhammani/Documents/projets-netbeans/imagineworkflow-2.0-root/imagineworkflow-web-app/";

        String result = "/home/ibrahimhammani/Documents/projets-netbeans/imagineworkflow-2.0-root/imagineworkflow-web-app/target";

        result = result.substring(0, result.lastIndexOf(File.separator) + 1);
        assertEquals(expResult, result);
    }

    @Ignore
    public void printTimeZone() {
        System.setProperty("user.timezone", "EST");
        System.out.println(System.getProperty("user.timezone"));
    }

}
