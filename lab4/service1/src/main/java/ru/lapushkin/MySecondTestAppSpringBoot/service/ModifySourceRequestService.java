package ru.lapushkin.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Request;

@Slf4j
@Service
@Order(2)
public class ModifySourceRequestService implements ModifyRequestService {

    private static final String SOURCE = "Service 1";

    @Override
    public void modify(Request request) {
        request.setSource(SOURCE);
        log.info("request после изменения source: {}", request);
    }
}
