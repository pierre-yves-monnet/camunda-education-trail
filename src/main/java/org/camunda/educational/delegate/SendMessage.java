package org.camunda.educational.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class SendMessage implements JavaDelegate {
    Logger logger = Logger.getLogger(FootPrintWorker.class.getName());


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try {
            String urlMessage = getStringValue("urlMessage", "http://localhost:8080/engine-rest/message", delegateExecution);

            String messageName = getStringValue("messageName", null, delegateExecution);
            String businessKey = getStringValue("businessKey", null, delegateExecution);
            if ("#businessKey".equals(businessKey))
                businessKey=delegateExecution.getProcessBusinessKey();
            String correlationVariableList = getStringValue("correlationVariables", null, delegateExecution);

            String variableList = getStringValue("variables", null, delegateExecution);


            // Send Message via the API
            sendMessageViaAPI(messageName,businessKey,correlationVariableList, variableList, delegateExecution);
            // send message via HTTP

        } catch (Exception e) {
            logger.severe("SendMessage: We got an exception ! Send a BPMN Error " + e.getMessage());
        }
    }



private  class MessageStatus {
        ProcessInstance processInstance;
        boolean status=true;
    MessageCorrelationResult correlationResult;
}

    /**
     *
     * @param messageName
     * @param businessKey
     * @param correlationVariableList
     * @param variableList
     * @param delegateExecution
     */
    private MessageStatus sendMessageViaAPI(String messageName,
                                   String businessKey,
                                   String correlationVariableList,
                                   String variableList,
                                   DelegateExecution delegateExecution) {
        MessageStatus messageStatus = new MessageStatus();
        RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        MessageCorrelationBuilder messageCorrelation = runtimeService.createMessageCorrelation(messageName);
        if (businessKey != null)
            messageCorrelation = messageCorrelation.processInstanceBusinessKey(businessKey);
        if (correlationVariableList != null) {
            Map<String, Object> variables = extractVariable(correlationVariableList, delegateExecution);
            messageCorrelation = messageCorrelation.processInstanceVariablesEqual(variables);
        }
        if (variableList != null) {
            Map<String, Object> variables = extractVariable(variableList, delegateExecution);
            for (Map.Entry<String,Object> value : variables.entrySet()) {
                messageCorrelation = messageCorrelation.setVariable(value.getKey(), value.getValue());
            }
        }

        // first, start to start the message
            try {
                messageStatus.correlationResult = messageCorrelation.correlateWithResult();
            } catch (Exception e) {
                messageStatus.status = false;
                logger.severe("SendMessage: Error during sending message "+e.toString());
            }
            return messageStatus;
    }
    /**
     *
     * @param urlMessage
     * @param messageName
     * @param businessKey
     * @param variableList
     * @param delegateExecution
     * @throws Exception
     */
    private void sendMessageViaHttp(String urlMessage,
                                    String messageName,
                                    String businessKey,
                                    String variableList,
                                    DelegateExecution delegateExecution)
            throws Exception {

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(urlMessage);

        post.addHeader("content-type", "application/json");

        Map jsonMessage = new HashMap();
        jsonMessage.put("messageName", messageName);

        if (businessKey != null)
            jsonMessage.put("businessKey", businessKey);

        if (variableList != null) {
            Map variables = extractVariable(variableList, delegateExecution);
            jsonMessage.put("processVariables", variables);

        }
        ObjectMapper mapper = new ObjectMapper();
        StringEntity params = new StringEntity(mapper.writeValueAsString(jsonMessage));
        post.setEntity(params);
        HttpResponse response = httpClient.execute(post);

    }
    /**
     *
     * @param variableList
     * @param delegateExecution
     * @return
     */
    private Map<String, Object> extractVariable(String variableList, DelegateExecution delegateExecution) {
        Map variables = new HashMap<>();
        StringTokenizer stVariable = new StringTokenizer(variableList, ",");
        while (stVariable.hasMoreTokens()) {
            StringTokenizer stOneVariable = new StringTokenizer(stVariable.nextToken(), "=");
            String name = (stOneVariable.nextToken());
            String value = (stOneVariable.hasMoreTokens() ? stOneVariable.nextToken() : null);
            if (value == null) {
                variables.put(name, getValue(name, null, delegateExecution));
            } else
                variables.put(name, getValue(name, value, delegateExecution));
        }
        return variables;
    }
    /**
     *
     * @param name
     * @param defaultValue
     * @param delegateExecution
     * @return
     */
    private String getStringValue(String name, String defaultValue, DelegateExecution delegateExecution) {
        Object value = delegateExecution.getVariable(name);
        return value == null ? defaultValue : value.toString();
    }

    /**
     *
     * @param name
     * @param defaultValue
     * @param delegateExecution
     * @return
     */
    private Object getValue(String name, Object defaultValue, DelegateExecution delegateExecution) {
        try {
            return delegateExecution.getVariableTyped(name).getValue();
        } catch(Exception e) {
            return defaultValue;
        }
    }
}
