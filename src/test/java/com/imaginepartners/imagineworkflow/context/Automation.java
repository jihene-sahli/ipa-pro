package com.imaginepartners.imagineworkflow.context;

import java.io.Serializable;
import org.apache.log4j.LogManager;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:hibernateConfig.xml","classpath:activitiContext.xml","classpath:applicationContext.xml"})
public class Automation implements Serializable {

	public Automation() {
		LogManager.getLogger(Automation.class).info("test constructor");
	}

	@Test
	public void hellowWorld(){
		LogManager.getLogger(Automation.class).info("hello world test method");
	}
}
