package org.camunda.educational.bankgameprocess;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.educational.systeminfoprocess.Information;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Update variables created by the setup.
 * Worker check that variables exists, and if yes, then it update it. If the variables does not exist, it throw an BpmnException
 */
public class UpdateAllVariables implements JavaDelegate {
    public static final String MISSING_VARIABLE = "MissingVariable";
    Logger logger = Logger.getLogger(SetupAllVariables.class.getName());


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        if (delegateExecution.getVariable(SetupAllVariables.OVEN_BRAND) == null)
            throw new BpmnError(MISSING_VARIABLE, "Variable [" + SetupAllVariables.OVEN_BRAND + "] is missing");
        delegateExecution.setVariable(SetupAllVariables.OVEN_BRAND, "Brand");

        if (delegateExecution.getVariable(SetupAllVariables.KITCHEN_TEMPERATURE) == null)
            throw new BpmnError(MISSING_VARIABLE, "Variable [" + SetupAllVariables.KITCHEN_TEMPERATURE + "] is missing");

        delegateExecution.setVariable(SetupAllVariables.KITCHEN_TEMPERATURE, Double.valueOf(70));

        // File
        if (delegateExecution.getVariable(SetupAllVariables.FILE_VARIABLE) == null)
            throw new BpmnError(MISSING_VARIABLE, "Variable [" + SetupAllVariables.FILE_VARIABLE + "] is missing");

        String currentPath = new java.io.File(".").getCanonicalPath();

        FileValue typedFileValue = Variables
                .fileValue("ReadMe.md")
                .file(new File("pom.xml"))
                .mimeType("text/plain")
                .encoding("UTF-8")
                .create();
        delegateExecution.setVariable(SetupAllVariables.FILE_VARIABLE, typedFileValue);

        // JSON
        if (delegateExecution.getVariable(SetupAllVariables.SYSTEM_INFORMATION) == null)
            throw new BpmnError(MISSING_VARIABLE, "Variable [" + SetupAllVariables.SYSTEM_INFORMATION + "] is missing");

        try {
            ObjectValue systemInformationValue = delegateExecution.getVariableTyped(SetupAllVariables.SYSTEM_INFORMATION);
            Information information = systemInformationValue.getValue(Information.class);
            information.nbActiveProcess = -information.nbActiveProcess;
            information.nbActiveUserTask = -information.nbActiveUserTask;
            delegateExecution.setVariable(SetupAllVariables.SYSTEM_INFORMATION,
                    Variables.objectValue(information)
                            .serializationDataFormat(Variables.SerializationDataFormats.JSON)
                            .create());
        } catch (Exception e) {
            logger.severe("Can't update a Json Variable");
            throw new BpmnError(MISSING_VARIABLE, "Can't update [" + SetupAllVariables.SYSTEM_INFORMATION + "] is missing");

        }
        // A MAP
        if (delegateExecution.getVariable(SetupAllVariables.COUNTRY_POPULATION) == null)
            throw new BpmnError(MISSING_VARIABLE, "Variable [" + SetupAllVariables.COUNTRY_POPULATION + "] is missing");

        Map<String, Object> map = (Map) delegateExecution.getVariable(SetupAllVariables.COUNTRY_POPULATION);
        map.put("Italy", 40);
        delegateExecution.setVariable(SetupAllVariables.COUNTRY_POPULATION, map);

        // A MAP
        if (delegateExecution.getVariable(SetupAllVariables.COUNTRY_LIST) == null)
            throw new BpmnError(MISSING_VARIABLE, "Variable [" + SetupAllVariables.COUNTRY_LIST + "] is missing");

        List<String> listCountry = (List) delegateExecution.getVariable(SetupAllVariables.COUNTRY_LIST);
        listCountry.add("Italy");

        delegateExecution.setVariable(SetupAllVariables.COUNTRY_LIST, listCountry);

    }
}