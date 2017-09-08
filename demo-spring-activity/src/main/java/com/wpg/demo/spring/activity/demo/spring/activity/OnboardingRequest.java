package com.wpg.demo.spring.activity.demo.spring.activity;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

/**
 * @author ChangWei Li
 * @version 2017-09-08 14:11
 */
@Slf4j
public class OnboardingRequest {

    public static void main(String[] args) {
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        processEngineConfiguration
                .setJdbcUrl("jdbc:mysql://localhost/activity?useSSL=false")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setJdbcUsername("root")
                .setJdbcPassword("a767940334")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        String processName = processEngine.getName();
        String version = ProcessEngine.VERSION;

        log.info("ProcessEngine [ {} ] Version: [ {} ]", processName, version);

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("onboarding.bpmn20.xml")
                .deploy();

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        log.debug("Found process definition [ {} ] with id [ {} ]", processDefinition.getName(), processDefinition.getId());
    }

}
