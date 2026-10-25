package ru.lapushkin.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Request;

@Service
public interface ModifyRequestService {

    void modify(Request request);

}
