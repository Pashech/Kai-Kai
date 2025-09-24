package com.example.test_app_kai.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
public class Proxy {

    @Value("${enable.proxy}")
    private boolean isProxyEnabled;
    @Value("#{'${proxy.black.list}'.split(',')}")
    private List<String> proxyBlackList;

    public String getProxyInfo(WebClient webClient) {
        if (!isProxyEnabled) {
            return null;
        }

        String proxyIp = webClient.get()
                .uri("https://api.ipify.org?format=text")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("PROXY IP: {}", proxyIp);

        ProxyInfo proxyInfo = webClient.get()
                .uri("http://ip-api.com/json/").retrieve().bodyToMono(ProxyInfo.class).block();

        return "Proxy IP = " + proxyIp + "  Proxy Country = " + proxyInfo.getCountry() + " " + proxyInfo.getRegion() + " " + proxyInfo.getCity();

    }

    public boolean isProxyIpInBlackList(String proxyIp)  {
        boolean isBlacklisted = proxyBlackList.contains(proxyIp);
        if (isBlacklisted) {
            log.warn("INVALID IP FOR REQUEST{}", proxyIp);
            return true;
        }
        return false;
    }
}


