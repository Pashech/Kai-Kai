package com.example.test_app_kai.client_service.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static com.example.test_app_kai.constants.Constants.*;

@Configuration
@Getter
public class WebClientConfig {

    private final ProxyProviderService proxyProviderService;

    @Value("${proxy.list}")
    public String dataSet;
    private final List<String> result = new ArrayList<>();
    @Value("${enable.proxy}")
    public boolean isProxyEnabled;

    public WebClientConfig(ProxyProviderService proxyProviderService) {
        this.proxyProviderService = proxyProviderService;
    }

    @Bean
    public WebClient webClient(ExchangeStrategies exchangeStrategies) {
        proxyProviderService.fillProxyList();
        java.net.Proxy proxy = proxyProviderService.getRandomProxy();

        HttpClient httpClient = HttpClient.create();
        if (proxy != null && proxy.address() instanceof InetSocketAddress addr) {
            httpClient = httpClient.proxy(spec -> spec
                    .type(ProxyProvider.Proxy.HTTP)
                    .host(addr.getHostString())
                    .port(addr.getPort()));
        }

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON)
                .defaultHeader(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                .defaultHeader(X_DOMAIN_HEADER, X_DOMAIN_HEADER_VALUE)
                .defaultHeader(COOKIE_HEADER, COOKIE_HEADER_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ExchangeStrategies exchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }
}
