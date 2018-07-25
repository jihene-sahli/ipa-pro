/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ibrahimhammani
 */
public class AuthenticationControllerTest {

    @Autowired
    @Qualifier("pwdEncoder")
    transient private BCryptPasswordEncoder pwdEncoder;

    public AuthenticationControllerTest() {
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
     * Test of doLogin method, of class AuthenticationController.
     */
    @Test
    public void testEncodePwd() throws Exception {
        System.out.println("testEncodePwd");
        BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder(11);
        System.out.println(pwdEncoder.encode("controle_budget"));
    }

}
