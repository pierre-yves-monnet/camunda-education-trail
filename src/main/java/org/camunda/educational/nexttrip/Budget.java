package org.camunda.educational.nexttrip;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class Budget implements JavaDelegate {
    Logger logger = Logger.getLogger(Budget.class.getName());


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        double budget = 0;
        try {
            String fromCountry = (String) execution.getVariable("fromCountry");
            String toCountry = (String) execution.getVariable("toCountry");
            Boolean needPlaneTicket = (Boolean) execution.getVariable("planeTicket");
            Boolean needAirbnb = (Boolean) execution.getVariable("airbnb");
            int nbParticipants = (Integer) execution.getVariable("nbParticipants");
            int nbDays = (Integer) execution.getVariable("nbDays");
            if (needPlaneTicket) {

                double oneTicket = 0;
                if (fromCountry.equals(toCountry))
                    oneTicket = 200;
                else
                    oneTicket = 1000;
                budget += oneTicket * (nbParticipants == 0 ? 1 : nbParticipants);
            }
            if (needAirbnb) {
                budget += 150 * nbDays;
            }
        } catch (Exception e) {
            logger.severe("Exception during Budger : " + e);
            budget = 1000;
        }
        execution.setVariable("budget", budget);
    }
}

