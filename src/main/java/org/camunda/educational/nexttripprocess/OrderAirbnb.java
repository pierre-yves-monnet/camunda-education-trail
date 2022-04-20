package org.camunda.educational.nexttripprocess;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class OrderAirbnb implements JavaDelegate {
    Logger logger = Logger.getLogger(OrderAirbnb.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("Buy Airbnb");
    }
}

