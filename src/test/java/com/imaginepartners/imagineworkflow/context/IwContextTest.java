/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.context;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ibrahimhammani
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:hibernateConfig.xml", "classpath:activitiContext.xml", "classpath:applicationContext.xml"})
public class IwContextTest {

    public IwContextTest() {
        LogManager.getLogger(IwContextTest.class).info("iw context test constructor");
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

    @Test
    @Ignore("not now")
    public void testCheckMailServerGmail() {
        Boolean isOk = true;

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.startssl.enable", "false");

        Session session = Session.getInstance(props);
        Transport transport;
        try {
            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com",
                    587,
                    "mailsender@imaginepartners.com",
                    "mailsender2013");
            transport.close();
        } catch (NoSuchProviderException ex) {
            isOk = false;
            LogManager.getLogger(IwContext.class).error(null, ex);
        } catch (MessagingException ex) {
            isOk = false;
            LogManager.getLogger(IwContext.class).error(null, ex);
        }
        Assert.assertEquals(isOk, true);
        System.out.println("---------------- " + isOk + " ----------------------------");
    }

    @Test
    @Ignore
    public void testCheckMailServerGmail00() {
        Boolean isOk = true;

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.startssl.enable", "false");

        Session session = Session.getInstance(props);
        Transport transport;
        try {
            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail00.com",
                    587,
                    "mailsender@imaginepartners.com",
                    "mailsender2013");
            transport.close();
        } catch (NoSuchProviderException ex) {
            isOk = false;
            LogManager.getLogger(IwContext.class).error(null, ex);
        } catch (MessagingException ex) {
            isOk = false;
            LogManager.getLogger(IwContext.class).error(null, ex);
        }
        Assert.assertEquals(isOk, false);
        System.out.println("---------------- " + isOk + " ----------------------------");
    }

}
