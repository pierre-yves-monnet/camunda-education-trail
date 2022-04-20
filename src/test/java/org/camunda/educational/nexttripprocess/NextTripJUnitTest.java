package org.camunda.educational.nexttripprocess;

import org.camunda.bpm.engine.rest.dto.runtime.ActivityInstanceDto;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.execute;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.findId;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.jobQuery;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.taskService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

@ExtendWith(ProcessEngineExtension.class)

// see https://camunda.com/best-practices/testing-process-definitions/
public class NextTripJUnitTest {

    // https://camunda.com/best-practices/testing-process-definitions/#_mock_the_business_service_methods
    // https://github.com/camunda-community-hub/camunda-bpm-mockito#mock-listener-and-delegate-behavior
    @Mock
    private BudgetMock mockBudget;

    @BeforeEach
    public void setup() {
        //Mocks.register("Budget", mockBudget);
    }
    @AfterEach
    public void teardown() {
        Mocks.reset();
    }
    @Test
    @Deployment(resources = "next-trip.bpmn")
    public void testHappyPath() {

        // DelegateExpressions.registerJavaDelegateMock("Budget").onExecutionSetVariable("budget", 2155);

        Mocks.register("Budget", new BudgetMock());
        // CamundaMockito.autoMock("next-trip.bpmn");
        // Create a HashMap to put in variables for the process instance
        Map<String, Object> variables = withVariables(NextTripVariables.REQUESTER, "Norbert");
        // Start process with Java API and variables
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("nextTrip", variables);
        // Make assertions on the process instance
        assertThat(processInstance).isStarted();

        printWhereIsTheProcessInstance(processInstance);

        assertThat(processInstance).isWaitingAt(findId("Choose a destination"));
        variables = withVariables(NextTripVariables.PARTICIPANTS, "Pierre;Paul;Jacques",
                NextTripVariables.NEED_PLANE, Boolean.TRUE,
                NextTripVariables.NEED_AIRBNB, Boolean.TRUE,
                NextTripVariables.DATE_TRIP, "28/01/2022",
                NextTripVariables.COUNTRY, "France");
        complete(task(), variables);
        printWhereIsTheProcessInstance(processInstance);


        // execute the job
        assertThat(processInstance).isWaitingAt(findId("Build ParticipantList"));
        List<Job> jobList = jobQuery().processInstanceId(processInstance.getId()).list();
        assertThat(jobList).hasSize(1);
        Job job = jobList.get(0);
        execute(job);


        assertThat(processInstance).isWaitingAt(findId("Validation by the family"));
        List<Task> taskList = taskService().createTaskQuery().processInstanceId(processInstance.getId()).list();
        assertThat(taskList).hasSize(3);

        complete(taskList.get(0), withVariables(NextTripVariables.LIST_PARTICIPANT_VALIDATION_ACCEPTED, Boolean.TRUE,
                NextTripVariables.LIST_PARTICIPANT_VALIDATION_COMMENT, "Let's go"));
        complete(taskList.get(1), withVariables(NextTripVariables.LIST_PARTICIPANT_VALIDATION_ACCEPTED, Boolean.TRUE,
                NextTripVariables.LIST_PARTICIPANT_VALIDATION_COMMENT, "I want to be here"));
        complete(taskList.get(2), withVariables(NextTripVariables.LIST_PARTICIPANT_VALIDATION_ACCEPTED, Boolean.TRUE,
                NextTripVariables.LIST_PARTICIPANT_VALIDATION_COMMENT, "So Exited"));

        assertThat(processInstance)
                .hasPassed("ConsolidateVote")
                .hasPassed("CreateBudget")
                .hasPassed("BuyPlaneTicket")
                .hasPassed("BuyAirbnb");

        // ------ message WaitTicket
        Assert.assertEquals(1, runtimeService().createExecutionQuery()
                .processVariableValueEquals(NextTripVariables.REQUESTER, "Norbert").messageEventSubscriptionName("WaitTicket")
                .count());

        // Assert.assertEquals(2155, runtimeService().getVariable(processInstance.getId(), "budget"));
        Assert.assertEquals(13553, runtimeService().getVariable(processInstance.getId(), "budget"));

        Map<String, Object> correlationKeys = new HashMap<>();
        correlationKeys.put(NextTripVariables.REQUESTER, "Norbert");

        runtimeService().correlateMessage("WaitTicket", correlationKeys,
                withVariables("ticketId", 12354));


        Assert.assertEquals(0, runtimeService().createExecutionQuery()
                .processVariableValueEquals(NextTripVariables.REQUESTER, "Norbert")
                .messageEventSubscriptionName("WaitTicket")
                .count());

        // -------------- Check BookAGuide
        assertThat(processInstance).hasPassed("BookAGuide");

        // -------------- Second GuideAccepted
        printWhereIsTheProcessInstance(processInstance);
        Assert.assertEquals(1,  runtimeService().createExecutionQuery()
                .processVariableValueEquals(NextTripVariables.REQUESTER, "Norbert").messageEventSubscriptionName("WaitGuide")
                .count());

        runtimeService().correlateMessage("WaitGuide", correlationKeys,
                withVariables("CostGuide", 123555));

        Assert.assertEquals(0,  runtimeService().createExecutionQuery()
                .processVariableValueEquals(NextTripVariables.REQUESTER, "Norbert").messageEventSubscriptionName("WaitGuide")
                .count());

        // Task Prepare Baggage
        assertThat(processInstance).isWaitingAt(findId("Prepare luggage"));
        complete(task(), withVariables("BaggagePrepared", Boolean.TRUE));


        // Verify the process is finish
        assertThat(processInstance).isEnded();

    }


    @Test
    @Deployment(resources = "next-trip.bpmn")
    public void testRejected() {
        ProcessInstance processInstance = runtimeService()
                .createProcessInstanceByKey("nextTrip")
                .startAfterActivity("ConsolidateVote")
                .setVariables(withVariables("allAccepted", false))
                .execute();

        assertThat(processInstance).isWaitingAt(findId("Post Mortem"));

        complete(task());

        assertThat(processInstance).isEnded();
    }


    private void printWhereIsTheProcessInstance(ProcessInstance processInstance) {
        try {
            ActivityInstance activityInstance = runtimeService().getActivityInstance(processInstance.getId());
            ActivityInstanceDto result = ActivityInstanceDto.fromActivityInstance(activityInstance);
            System.out.println("activityName=[" + result.getName() + "](" + result.getActivityType() + ")");
            for (ActivityInstanceDto acti : result.getChildActivityInstances()) {
                System.out.println("activityName=[" + acti.getName() + "](" + acti.getActivityType() + ")");
            }
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
    }
}
