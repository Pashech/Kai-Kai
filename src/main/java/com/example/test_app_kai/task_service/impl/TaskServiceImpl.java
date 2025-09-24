package com.example.test_app_kai.task_service.impl;

import com.example.test_app_kai.dao.JdbcTemplateTaskDaoImpl;
import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.task_service.Task;
import com.example.test_app_kai.task_service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import static com.example.test_app_kai.constants.Constants.*;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final JdbcTemplateTaskDaoImpl jdbcTemplateTaskDao;

    @Override
    public Task getTaskByName(String taskName, int serverNumber) {

        final Task task;
        try {
            task = jdbcTemplateTaskDao.getByHarvesterName(taskName, serverNumber);
        } catch (SQLNonTransientConnectionException e) {
            throw new RuntimeException(e);
        }

        return task;
    }

    @Override
    public void sendHarvestResult(String taskId, HarvesterResult harvestResult, Task task) {
        if(harvestResult.getTickets() != null) {
            jdbcTemplateTaskDao.sendHarvestResultMessage(taskId, harvestResult.getHarvestMessage(), harvestResult.getResultCode());
            log.info("Send short data for task: {}", taskId);
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            java.util.Date currentDate = new Date();
            LocalDate today = LocalDate.now();
            LocalDate dateFromTask = LocalDate.parse(task.getDepartureDate());
            long dayFrom = ChronoUnit.DAYS.between(today, dateFromTask);
            String jsonConfig =
                    "{\"date\":\"" + formatter.format(currentDate) + "\",\"route\":\"" + task.getDepartureStation() + "-" + task.getArrivalStation() +
                            "\",\"crawler_type\":\"" + task.getCrawlerType() + "\",\"plugin_type\":\"external\",\"from\":" + dayFrom + ",\"depth\":" + 1 +
                            ",\"task_uuid\":\"" + task.getTaskUuid() + "\"}\n";


            if((harvestResult.getResultCode() == SUCCESS_CODE || harvestResult.getResultCode() == SUCCESS_EMPTY_LIST_CODE) && harvestResult.getHarvestMessage() != null) {
                String md5;
                log.info("Insert short data");
                try {
                    md5 = jdbcTemplateTaskDao.sendShortDataInfo(harvestResult, jsonConfig);
                    if (jdbcTemplateTaskDao.getShortDataStatus(md5)) {
                        log.info("Short data inserted");
                    }

                    log.info("Insert harvested data");
                    jdbcTemplateTaskDao.sendHarvesterData(harvestResult, md5);
                    log.info("Harvested data inserted");

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        } else {
            jdbcTemplateTaskDao.sendHarvestResultMessage(taskId, harvestResult.getHarvestMessage(), harvestResult.getResultCode());
        }
    }
}