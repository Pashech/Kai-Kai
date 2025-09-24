package com.example.test_app_kai.client_service.impl;

import com.example.test_app_kai.client_service.TrainClientService;
import com.example.test_app_kai.request_data.TrainRequest;
import com.example.test_app_kai.response_data.TrainResponseData;
import com.example.test_app_kai.utils.Proxy;
import com.example.test_app_kai.utils.UserAgentRandomizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static com.example.test_app_kai.constants.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainClientServiceImpl implements TrainClientService {

    private final WebClient webClient;
    private final Proxy proxy;
    private final String userAgent = UserAgentRandomizer.getRandomUserAgent();

    @Override
    public TrainResponseData clientTrainRequest(TrainRequest trainRequest) throws Exception {

        log.info(proxy.getProxyInfo(webClient));

            return webClient.post()
                    .uri(API_URL)
                    .bodyValue(trainRequest)
                    .header(ROUTE_PREFIX_HEADER, ROUTE_PREFIX_HEADER_VALUE)
                    .header(USER_AGENT, userAgent)
                    .retrieve()
                    .bodyToMono(TrainResponseData.class)
                    .block();

    }
}
