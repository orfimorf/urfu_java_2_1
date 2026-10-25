package ru.lapushkin.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Request;

@Slf4j
@Service
public class SendRequestService {

    private final String service2Url;

    public SendRequestService(@Value("${service2.url}") String service2Url) {
        this.service2Url = service2Url;
    }

    public void send(Request request) {
        log.info("Отправка request в Сервис 2 ({}): {}", service2Url, request);

        HttpEntity<Request> httpEntity = new HttpEntity<>(request);

        new RestTemplate().exchange(service2Url,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                });
    }
}
