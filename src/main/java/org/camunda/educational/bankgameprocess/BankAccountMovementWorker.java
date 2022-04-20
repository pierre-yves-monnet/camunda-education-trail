package org.camunda.educational.bankgameprocess;

import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.educational.systeminfoprocess.Information;
import org.camunda.educational.systeminfoprocess.SystemInformationWorker;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class BankAccountMovementWorker implements JavaDelegate {
    Logger logger = Logger.getLogger(BankAccountMovementWorker.class.getName());


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ProcessEngineServices processEngineServices = delegateExecution.getProcessEngineServices();

        Double amountAccount = getDoubleValue( delegateExecution.getVariable("amountAccountIn"));
        Double amountMovement = getDoubleValue( delegateExecution.getVariable("amountMovement"));
        Object direction = delegateExecution.getVariable("direction");

        BigDecimal amountAccountBD = new BigDecimal( amountAccount);
        if ("PLUS".equals(direction))
            amountAccountBD = amountAccountBD.add( new BigDecimal(amountMovement));
        else
            amountAccountBD = amountAccountBD.subtract( new BigDecimal(amountMovement));
        delegateExecution.setVariableLocal("amountAccountOut",amountAccountBD.doubleValue());
    }

    private Double getDoubleValue( Object valueOb) {
    if (valueOb instanceof Double)
        return (Double) valueOb;
    if (valueOb==null)
        return 0D;
    try {
        return Double.valueOf(valueOb.toString());
    } catch (Exception e) {
        logger.severe("BankAccountMovementWorker: Can't get double of ["+valueOb.toString()+"]");
    }
    return 0D;
    }

}
