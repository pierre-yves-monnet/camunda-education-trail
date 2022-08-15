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

/**
 * Create a list of variables with different type (String, Double, File, Json)
 */
public class SetupAllVariables implements JavaDelegate {


    Logger logger = Logger.getLogger(SetupAllVariables.class.getName());

    public static final String OVEN_BRAND = "ovenBrand";
    public static final String FILE_VARIABLE = "fileVariable";
    public static final String KITCHEN_TEMPERATURE = "kitchenTemperature";
    public static final String SYSTEM_INFORMATION = "systemInformation";
    public static final String COUNTRY_POPULATION = "countryPopulation";
    public static final String COUNTRY_LIST = "countryList";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        delegateExecution.setVariable(OVEN_BRAND, "Fagor");
        delegateExecution.setVariable(KITCHEN_TEMPERATURE, Double.valueOf(24.5));

        // File
        String currentPath = new java.io.File(".").getCanonicalPath();

        FileValue typedFileValue = Variables
                .fileValue("ReadMe.md")
                .file(new File("README.md"))
                .mimeType("text/plain")
                .encoding("UTF-8")
                .create();
        delegateExecution.setVariable(FILE_VARIABLE, typedFileValue);

        // JSON
        ProcessEngineServices processEngineServices = delegateExecution.getProcessEngineServices();
        try {
            delegateExecution.setVariable(SYSTEM_INFORMATION,
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
        delegateExecution.setVariable(COUNTRY_POPULATION, map);

        List<String> list = map.keySet().stream().collect(Collectors.toList());
        delegateExecution.setVariable(COUNTRY_LIST, list);

    }
}

