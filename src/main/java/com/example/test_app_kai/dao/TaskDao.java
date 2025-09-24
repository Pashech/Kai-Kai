package com.example.test_app_kai.dao;

import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.task_service.Task;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

public interface TaskDao {

    Task getByHarvesterName(String name, int serverNumber) throws SQLNonTransientConnectionException;
    String sendShortDataInfo(HarvesterResult harvestResult, String jsonConfig) throws SQLException;
    boolean getShortDataStatus(String md5);
}
