package org.camunda.educational.bankgameprocess;

import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.educational.systeminfoprocess.Information;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SetupAllVariables implements JavaDelegate {
    Logger logger = Logger.getLogger(SetupAllVariables.class.getName());


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        delegateExecution.setVariable("ovenBrand", "Fagor");
        delegateExecution.setVariable("kitchenTemperature", Double.valueOf(24.5));

        // File
        String currentPath = new java.io.File(".").getCanonicalPath();

        FileValue typedFileValue = Variables
                .fileValue("ReadMe.md")
                .file(new File("README.md"))
                .mimeType("text/plain")
                .encoding("UTF-8")
                .create();
        delegateExecution.setVariable("fileVariable", typedFileValue);

        // JSON
        ProcessEngineServices processEngineServices = delegateExecution.getProcessEngineServices();
        try {
            delegateExecution.setVariable("SystemInformation",
                    Variables.objectValue(Information.getInformation(processEngineServices.getRepositoryService(),
                                    processEngineServices.getTaskService()))
                            .serializationDataFormat(Variables.SerializationDataFormats.JSON)
                            .create());
        } catch (Exception e) {
            logger.severe("Can't update a Json Variable");
        }
        // A MAP
        Map<String, Object> map = new HashMap<>();
        map.put("France", 60);
        map.put("Germany", 80);
        map.put("USA", 250);
        delegateExecution.setVariable("countryPopulation", map);

        List<String> list = map.keySet().stream().collect(Collectors.toList());
        delegateExecution.setVariable("countryList", list);

    }
}

