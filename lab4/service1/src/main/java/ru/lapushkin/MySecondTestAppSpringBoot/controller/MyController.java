package ru.lapushkin.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.lapushkin.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.lapushkin.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Codes;
import ru.lapushkin.MySecondTestAppSpringBoot.model.ErrorCodes;
import ru.lapushkin.MySecondTestAppSpringBoot.model.ErrorMessages;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Request;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Response;
import ru.lapushkin.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.lapushkin.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.lapushkin.MySecondTestAppSpringBoot.service.SendRequestService;
import ru.lapushkin.MySecondTestAppSpringBoot.service.ValidationService;
import ru.lapushkin.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final List<ModifyRequestService> modifyRequestServices;
    private final SendRequestService sendRequestService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        List<ModifyRequestService> modifyRequestServices,
                        SendRequestService sendRequestService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestServices = modifyRequestServices;
        this.sendRequestService = sendRequestService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        log.info("request: {}", request);
        logElapsedTime(request.getSystemTime());

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("response создан: {}", response);

        try {
            validationService.isValid(bindingResult);
            validationService.isSupported(request);
            modifyRequestServices.forEach(service -> service.modify(request));
            sendRequestService.send(request);
        } catch (ValidationFailedException e) {
            log.error("ValidationFailedException: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("response после ошибки валидации: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("UnsupportedCodeException: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("response после неподдерживаемого кода: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("response после непредвиденной ошибки: {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        log.info("response отправлен: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void logElapsedTime(String systemTime) {
        if (systemTime == null) {
            log.warn("В request нет systemTime, время с момента отправки не вычислено");
            return;
        }
        try {
            log.info("Сервис 1 получил request через {} мс после отправки из Postman (systemTime: {})",
                    DateTimeUtil.millisSince(systemTime), systemTime);
        } catch (ParseException e) {
            log.warn("Не удалось разобрать systemTime: {}", systemTime);
        }
    }
}
