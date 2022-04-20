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

public class DmnDrdTest {
    @Test
    @Deployment(resources = {"risk-level.dmn"})
    public void finalCaliforniaRisk() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("FinalRiskLevel")
                .variables(withVariables("age", 70,
                        "state", "CA",
                        "car", "Ford",
                        "city", "San Francisco",
                        "amountWellFargo", 1200))
                .evaluate();
        // CA, San Francisco => "orange" on StateGlobalRisk
        // 60 points on itemCondition
        //  orange, 60, highCover : result is saphir
        Assertions.assertEquals("saphir", decisionResult.getSingleEntry());

         decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("FinalRiskLevel")
                .variables(withVariables("age", 70,
                        "state", "CA",
                        "car", "Ford",
                        "city", "San Francisco",
                        "amountWellFargo", 200))
                .evaluate();
        // CA, San Francisco => "orange" on StateGlobalRisk
        // 60 points on itemCondition
        //  orange, 60, lowCover : result is emeraude
        Assertions.assertEquals("emeraude", decisionResult.getSingleEntry());
    }

    @Test
    @Deployment(resources = {"risk-level.dmn"})
    public void finalOregonRisk() {
        DmnDecisionTableResult decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("FinalRiskLevel")
                .variables(withVariables("age", 70,
                        "state", "OR",
                        "car", "Ford",
                        "city", "Portland",
                        "amountWellFargo", 1200))
                .evaluate();
        // OR, Portland => "green" on StateGlobalRisk
        // 60 points on itemCondition
        //  orange, 60, highCover : result is platinium
        Assertions.assertEquals("platinium", decisionResult.getSingleEntry());

        decisionResult = processEngine().getDecisionService()
                .evaluateDecisionTableByKey("FinalRiskLevel")
                .variables(withVariables("age", 70,
                        "state", "OR",
                        "car", "Ford",
                        "city", "Portland",
                        "amountWellFargo", 200))
                .evaluate();
        // OR, Portland => "green" on StateGlobalRisk
        // 60 points on itemCondition
        //  orange, 60, highCover : result is septanium
        Assertions.assertEquals("septanium", decisionResult.getSingleEntry());
    }

}
