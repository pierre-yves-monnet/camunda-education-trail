package org.camunda.educational.systeminfoprocess;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;

import java.io.Serializable;
import java.util.List;

public class Information implements Serializable {
    public int nbActiveProcess;
    public int nbActiveUserTask;

    public void setNbActiveProcess(int nbActiveProcess) {
        this.nbActiveProcess = nbActiveProcess;
    }

    public void setNbActiveUserTask(int nbActiveUserTask) {
        this.nbActiveUserTask = nbActiveUserTask;
    }

    public int getNbActiveProcess() {
        return nbActiveProcess;
    }

    public int getNbActiveUserTask() {
        return nbActiveUserTask;
    }

    /**
     * return information on system
     *
     * @param repositoryService repository service
     * @param taskService       access task Service
     */
    public static Information getInformation(RepositoryService repositoryService,
                                             TaskService taskService) {
        Information information = new Information();
        List<ProcessDefinition> listProcesses = repositoryService.createProcessDefinitionQuery().active().list();
        information.nbActiveProcess = listProcesses.size();

        List<Task> listTasks = taskService.createTaskQuery().active().list();
        information.nbActiveUserTask = listTasks.size();
        return information;
    }

}
