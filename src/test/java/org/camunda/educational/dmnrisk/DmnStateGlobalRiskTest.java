package org.camunda.educational.dmnrisk;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

@ExtendWith(ProcessEngineExtension.class)

public class DmnStateGlobalRiskTest {

    @Test
    @Deployment(resources={"risk-level.dmn"})
    public void testCalifornia() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("StateGlobalRisk")
                .variables(withVariables("state", "CA", "city", "Los Angeles"))
                .evaluate();
        Assertions.assertEquals("red", decisionResult.getSingleEntry().toString());

         decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("StateGlobalRisk")
                .variables(withVariables("state", "CA", "city", "San Francisco"))
                .evaluate();
        Assertions.assertEquals("orange", decisionResult.getSingleEntry().toString());

        decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("StateGlobalRisk")
                .variables(withVariables("state", "CA", "city", "Sacramento"))
                .evaluate();
        Assertions.assertEquals("yellow", decisionResult.getSingleEntry().toString());
    }
    @Test
    @Deployment(resources={"risk-level.dmn"})
    public void testColorado() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("StateGlobalRisk")
                .variables(withVariables("state", "CO", "city", "Denver"))
                .evaluate();
        Assertions.assertEquals("orange", decisionResult.getSingleEntry().toString());
    }
}
