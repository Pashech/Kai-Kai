package com.example.test_app_kai.command_line;

import com.example.test_app_kai.TestAppKaiApplication;
import com.example.test_app_kai.task_service.Task;
import com.example.test_app_kai.task_service.TaskService;
import com.example.test_app_kai.utils.FilesAndFolderChecker;
import com.example.test_app_kai.utils.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.test_app_kai.constants.Constants.SECOND_1;
import static com.example.test_app_kai.constants.Constants.SECOND_55;
import static java.lang.Thread.sleep;


@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    public final String HARVESTER_NAME = "KAI";
    private static String jarCatalog;
    @Value("${harvester.version}")
    private String harvesterVersion;
    @Value("${proxy.list}")
    private String proxyList;

    private final TaskService taskService;
    private final Schedule schedule;

    static {
        try {
            jarCatalog =
                    TestAppKaiApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            log.info("Identify program catalog is failed");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.setProperty("currentDate", dateFormat.format(new Date()));
        //		log = LogManager.getLogger();
    }

    @Override
    public void run(String... args) throws InterruptedException {

        log.info("Harvester started");
        FilesAndFolderChecker.directoryCheck();
        int taskCount = 1;
        log.info("Harvester version => {}", harvesterVersion);
        log.info("HARVESTER_NAME: {}", HARVESTER_NAME);
        log.info("VPN_PROVIDER: {}", System.getenv("VPN_PROVIDER"));
        log.info("SERVER_NUMBER: {}", System.getenv("SERVER_NUMBER"));
        log.info("Proxy list: {}", proxyList);

        int sleepCount;

        while (true) {
            List<Task> tasks = new ArrayList<>();
            try {
                for (int i = 0; i < taskCount; i++) {
                    log.info("PROXY FILE PATH : {}", proxyList);
                    Task taskByHarvesterName =
                            taskService.getTaskByName(HARVESTER_NAME.toUpperCase(), 0);
                    if (Objects.nonNull(taskByHarvesterName.getTaskUuid())) {
                        tasks.add(taskByHarvesterName);
                    } else {
                        log.info("Task from RIB is null");
                    }
                }
            } catch (Exception e) {
                log.error("Exception in get tasks, Exc: {}", e.getMessage());
            }

            if (!tasks.stream().allMatch(task -> null == task.getTaskUuid())) {
                try {
                    schedule.hanldeTask(tasks);
                }catch (IOException | InterruptedException e) {
                    log.error("Exception in hanldeTask, Exc: {}", e.getMessage());
                }

                sleepCount = SECOND_1;
            } else {
                sleepCount = SECOND_55;
            }
            sleep(sleepCount);
        }
    }
}
