package com.example.test_app_kai.utils;

import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.service.ParsingService;
import com.example.test_app_kai.service.VpnService;
import com.example.test_app_kai.task_service.Task;
import com.example.test_app_kai.task_service.TaskService;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;


@Slf4j
@RequiredArgsConstructor
@Service
public class Schedule {

    public final static int SECOND_5 = 5000;

    private final TaskService taskService;
    private final ParsingService parsingService;
    private final Proxy proxy;

    @Value("${enable.vpn}")
    private boolean isVPNEnabled;
    @Value("${enable.proxy}")
    private boolean isProxyEnabled;

    public void hanldeTask(List<Task> tasks) throws InterruptedException, IOException {
        VpnService vpnService = null;

        Map<String, HarvesterResult> parsingResults = new HashMap<>();

        assert vpnService != null;
        String vpnStatus = connectIfDisconnected(vpnService);
        if (vpnStatus.equals("Proxy IP in blacklist")){
            throw new IOException("Proxy IP in blacklist");
        }
        for (Task task : tasks) {
            parseOneTask(task, parsingResults);
        }
        disconnectIfConnected(vpnService);
        for (Map.Entry<String, HarvesterResult> entry : parsingResults.entrySet()) {
            Task taskToSend = null;
            for (Task taskFromMap : tasks) {
                if (entry.getKey().equals(taskFromMap.getTaskUuid()))
                    taskToSend = taskFromMap;
            }
            taskService.sendHarvestResult(entry.getKey(), entry.getValue(), taskToSend);
        }
    }

    private void parseOneTask(Task task, Map<String, HarvesterResult> result) {

        if (task.getTaskUuid() == null || task.getTaskUuid().equals("")) {
            log.info("Getted task is empty");
        } else {
            log.info("Task from RIB: {}", task);
            log.info(" Proxy enabled : " + isProxyEnabled);
            try {
                result.put(task.getTaskUuid(), parsingService.getParsedData(task));
            } catch (Exception e) {
                log.error("Exception in getParsedData. {}", e.getMessage());
            }
            log.info("Parsing finished.");
        }
    }

    public String connectIfDisconnected(VpnService vpnService) throws IOException {
        if (isVPNEnabled) {
            log.info("Try connect from VPN if disconnected");
            String status = vpnService.getVpnStatus();
            while (status.contains("Not connected") ||
                    status.contains("Status: Disconnected")) {
                vpnService.vpnStartRandom();
                try {
                    sleep(SECOND_5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                status = vpnService.getVpnStatus();

            }
            String proxyIp = null;
            try {
                proxyIp = Unirest.get("https://ifconfig.me").asString().getBody();
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
            log.info("VPN IP = {}", proxyIp);
            if (proxy.isProxyIpInBlackList(proxyIp)){
                log.warn("Proxy IP in blacklist");
                return "Proxy IP in blacklist";
            }
        }
        return "Connected";
    }

    public void disconnectIfConnected(VpnService vpnService) throws IOException, InterruptedException {
        if (isVPNEnabled) {
            log.info("Try disconnect from VPN if connected");
            String status = vpnService.getVpnStatus();
            while (status.contains("Connected") ||
                    status.contains("Connecting")) {
                vpnService.vpnStop();
                sleep(SECOND_5);
                status = vpnService.getVpnStatus();
            }
        }
    }
}
