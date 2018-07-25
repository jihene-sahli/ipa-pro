package com.imaginepartners.imagineworkflow.services.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Rule;

/**
 *
 * @author ibrahimhammani
 */
public class ActivitiServiceImplTest {

    private String filename = "/home/ibrahimhammani/workspace-luna/print/src/main/resources/diagrams/MyProcess.bpmn";

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    public ActivitiServiceImplTest() {
    }

    /**
     * Test of deploy method, of class ActivitiServiceImpl.
     */
    @Ignore
    public void testDeploy() {
        try {
            RepositoryService repositoryService = activitiRule.getRepositoryService();
            repositoryService.createDeployment().addInputStream("basicProcess.bpmn20.xml",
                    new FileInputStream("basicProcess.bpmn20.xml"))
                    .name("depName")
                    .category("cat")
                    .deploy();

            RuntimeService runtimeService = activitiRule.getRuntimeService();
            Map<String, Object> variableMap = new HashMap<String, Object>();
            variableMap.put("name", "Activiti");
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("basicProcess", variableMap);
            assertNotNull(processInstance.getId());
            System.out.println("id " + processInstance.getId() + " "
                    + processInstance.getProcessDefinitionId());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActivitiServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
