package ru.lapushkin.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Request;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Systems;

@Slf4j
@Service
@Order(1)
public class ModifySystemNameRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        // Сервис 1 выступает ERP-системой, поэтому request уходит дальше от её имени
        request.setSystemName(Systems.ERP);
        log.info("request после изменения systemName: {}", request);
    }
}
