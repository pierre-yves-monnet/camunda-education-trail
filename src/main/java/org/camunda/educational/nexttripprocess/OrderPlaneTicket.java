package org.camunda.educational.nexttripprocess;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class OrderPlaneTicket implements JavaDelegate {
    Logger logger = Logger.getLogger(Budget.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("Buy plane ticket");
    }
}
