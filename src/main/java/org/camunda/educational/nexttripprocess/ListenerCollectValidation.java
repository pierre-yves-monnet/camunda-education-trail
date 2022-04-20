package org.camunda.educational.nexttripprocess;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerCollectValidation implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        List<String> listParticipants = (List) delegateTask.getVariable(NextTripVariables.LIST_PARTICIPANTS);

        List<Map<String, Object>> listParticipantsValidation = (List) delegateTask.getVariable(NextTripVariables.LIST_PARTICIPANTS_VALIDATION);
        if (listParticipantsValidation == null)
            listParticipantsValidation = new ArrayList<>();

        if (listParticipants != null && listParticipantsValidation.size() < listParticipants.size()) {
            while (listParticipantsValidation.size() < listParticipants.size())
                listParticipantsValidation.add(new HashMap<>());
        }

// get current voter
        String currentVoter = (String) delegateTask.getVariable(NextTripVariables.LOCAL_ONE_PARTICIPANT);

// get the position of the voter
        int position = 0;
        if (listParticipants!=null) {
            for (int i = 0; i < listParticipants.size(); i++) {
                if (listParticipants.get(i).equals(currentVoter))
                    position = i;
            }
        }
        Map<String, Object> validation = new HashMap<>();
        validation.put(NextTripVariables.LIST_PARTICIPANT_VALIDATION_ACCEPTED, delegateTask.getVariable(NextTripVariables.LIST_PARTICIPANT_VALIDATION_ACCEPTED));
        validation.put(NextTripVariables.LIST_PARTICIPANT_VALIDATION_COMMENT, delegateTask.getVariable(NextTripVariables.LIST_PARTICIPANT_VALIDATION_COMMENT));
        validation.put(NextTripVariables.LIST_PARTICIPANT_VALIDATION_VOTER, currentVoter);
        listParticipantsValidation.set(position, validation);
        delegateTask.setVariable(NextTripVariables.LIST_PARTICIPANTS_VALIDATION, listParticipantsValidation);

    }

}
