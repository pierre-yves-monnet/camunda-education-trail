package org.camunda.milestone;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class Stamp implements JavaDelegate {
    Logger logger = Logger.getLogger(Stamp.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String stampName = (String) execution.getVariable("stampName");
        logger.info("stamp :[" + stampName + "]");

    }
}
