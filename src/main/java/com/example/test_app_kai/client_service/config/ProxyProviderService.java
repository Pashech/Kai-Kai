package com.example.test_app_kai.client_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProxyProviderService {

    @Value("${proxy.list}")
    public String dataSet;
    private final List<String> result = new ArrayList<>();
    @Value("${enable.proxy}")
    public boolean isProxyEnabled;

    public void fillProxyList () {
        result.clear();
        final String[] split = dataSet.split(",");
        result.addAll(List.of(split));
    }

    public java.net.Proxy getRandomProxy() {
        List<String> proxies = result;
        if (!isProxyEnabled || proxies.isEmpty()) return null;

        int index = ThreadLocalRandom.current().nextInt(proxies.size());
        String[] parts = proxies.get(index).split(":");

        if (parts.length != 2) return null;

        try {
            String host = parts[0].trim();
            int port = Integer.parseInt(parts[1].trim());
            return new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(host, port));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
