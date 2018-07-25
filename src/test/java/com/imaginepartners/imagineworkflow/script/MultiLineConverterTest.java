/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.script;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imaginepartners.imagineworkflow.models.IwInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.LogManager;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.openqa.selenium.io.IOUtils;

/**
 *
 * @author ibrahimhammani
 */
public class MultiLineConverterTest {

    /**
     * Test of deploy method, of class ActivitiServiceImpl.
     */
    @Ignore
    public void testDeploy() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            InputStream file = MultiLineConverterTest.class.getResourceAsStream("form96_8.json");
            String str = IOUtils.readFully(file);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

            List<IwInput> iwInputList = objectMapper.readValue(str, new TypeReference<List<IwInput>>() {
            });

            for (IwInput input : iwInputList) {
                if ("multiline".equals(input.getComponent())) {
                    ArrayList<LinkedHashMap<String, Object>> multilinerows = new ArrayList<LinkedHashMap<String, Object>>();
                    ArrayList<LinkedHashMap<String, Object>> multilinecols = new ArrayList<LinkedHashMap<String, Object>>();

                    for (String opt : input.getOptions()) {
                        //multilinecols.add(e)
                    }
                }
            }
            LogManager.getLogger(MultiLineConverterTest.class.getName()).info(iwInputList);
        } catch (IOException ex) {
            fail(ex.getLocalizedMessage());
        }
    }

}
