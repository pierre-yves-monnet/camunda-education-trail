package org.camunda.educational.systeminfoprocess;

import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.educational.nexttripprocess.Budget;

import java.util.logging.Logger;

public class SystemInformationWorker implements JavaDelegate {
    Logger logger = Logger.getLogger(SystemInformationWorker.class.getName());


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ProcessEngineServices processEngineServices = delegateExecution.getProcessEngineServices();

        Information information = Information.getInformation(processEngineServices.getRepositoryService(),
                processEngineServices.getTaskService());
        ObjectValue typedInformationValue =
                Variables.objectValue(information).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        delegateExecution.setVariable("SystemInformation", typedInformationValue);


    }


}