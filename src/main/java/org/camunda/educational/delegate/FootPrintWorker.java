package org.camunda.educational.delegate;

import camundajar.impl.scala.Int;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.educational.bankgameprocess.BankAccountMovementWorker;

import java.util.logging.Logger;

public class FootPrintWorker implements JavaDelegate {
    Logger logger = Logger.getLogger(FootPrintWorker.class.getName());


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String name= (String) delegateExecution.getVariable("name");
        Object delayObj= delegateExecution.getVariable("delayMs");
        long delayMs=0;
        try {
            if (delayObj!=null)
                delayMs = Long.parseLong(delayObj.toString());
        }
        catch(Exception e) {
            logger.severe("FootPrint: can't parse delayMs["+delayMs+"] : "+e.toString());
        }
        logger.info("Footprint: Start ["+name+"] - sleep "+delayMs+" ms");
        Thread.sleep( delayMs);
        logger.info("Footprint: End ["+name+"] ");
    }
}