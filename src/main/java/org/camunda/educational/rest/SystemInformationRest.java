package org.camunda.educational.rest;


import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.educational.systeminfoprocess.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("application")
public class SystemInformationRest {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    /**
     * URL localhost:8080/application/api/application/info
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/api/application/info", produces = "application/json")
    public Information getApplicationInformation() {
        Information information = Information.getInformation(repositoryService, taskService);
        return information;
    }

    }
