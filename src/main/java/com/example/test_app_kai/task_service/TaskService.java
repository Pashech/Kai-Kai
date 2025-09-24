package com.example.test_app_kai.task_service;

import com.example.test_app_kai.output_data.HarvesterResult;

public interface TaskService {

    Task getTaskByName(String taskName, int serverNumber);
    void sendHarvestResult(String taskId, HarvesterResult harvestResult, Task task);
}
