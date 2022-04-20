package org.camunda.educational.nexttripprocess;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ConsolidateResult implements JavaDelegate {
    Logger logger = Logger.getLogger(ConsolidateResult.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<Map<String, Object>> listParticipations = (List) execution.getVariable(NextTripVariables.LIST_PARTICIPANTS_VALIDATION);

        long rejection = listParticipations.stream()
                .filter(participation -> Boolean.FALSE.equals(participation.get(NextTripVariables.LIST_PARTICIPANT_VALIDATION_ACCEPTED)))
                .count();
        execution.setVariable(NextTripVariables.ALL_ACCEPTED, rejection == 0);
    }
}