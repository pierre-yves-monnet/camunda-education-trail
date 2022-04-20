package org.camunda.educational.dmnrisk;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

@ExtendWith(ProcessEngineExtension.class)

public class DmnItemConditionTest {

    @Test
    @Deployment(resources = {"risk-level.dmn"})
    public void OldAgeInovative() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("ItemCondition")
                .variables(withVariables("age", 70, "state", "CO", "car", "Ford"))
                .evaluate();
        // 10 (base)+21 (old age)+7 (inovative state)+22(Colorado)=60
        Assertions.assertEquals(60, (Integer) decisionResult.getSingleEntry());

    }
    @Test
    @Deployment(resources = {"risk-level.dmn"})
    public void inovative() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("ItemCondition")
                .variables(withVariables("age", 20, "state", "OR", "car", "Ford"))
                .evaluate();
        // 10 (base)+7 (inovative state)
        Assertions.assertEquals(17, (Integer) decisionResult.getSingleEntry());
    }
    @Test
    @Deployment(resources = {"risk-level.dmn"})
    public void middleAge() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("ItemCondition")
                .variables(withVariables("age", 40, "state", "WA", "car", "Ford"))
                .evaluate();
        // 10 (base)+40 (middle age) + 3 (normal)
        Assertions.assertEquals(53, (Integer) decisionResult.getSingleEntry());
    }
    @Test
    @Deployment(resources = {"risk-level.dmn"})
    public void nissan() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("ItemCondition")
                .variables(withVariables("age", 40, "state", "AZ", "car", "Nissan"))
                .evaluate();
        // 10 (base)+40 (middle age) + 3 (normal) + 22 (CA, CO,AZ) + 14 (nissan)
        Assertions.assertEquals(89, (Integer) decisionResult.getSingleEntry());
    }
}
