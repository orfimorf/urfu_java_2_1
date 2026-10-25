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
import ru.lapushkin.MySecondTestAppSpringBoot.service.AnnualBonusService;
import ru.lapushkin.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.lapushkin.MySecondTestAppSpringBoot.service.ValidationService;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final AnnualBonusService annualBonusService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        log.info("request: {}", request);

        Response response = modifyResponseService.modify(Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build());

        try {
            validationService.isValid(bindingResult);
            validationService.isSupported(request);
            addBonuses(request, response);
        } catch (ValidationFailedException e) {
            return failed(response, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST, e);
        } catch (UnsupportedCodeException e) {
            return failed(response, ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED, HttpStatus.BAD_REQUEST, e);
        } catch (Exception e) {
            return failed(response, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }

        log.info("response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void addBonuses(Request request, Response response) {
        response.setAnnualBonus(annualBonusService.calculate(
                request.getPosition(), request.getSalary(), request.getBonus(), request.getWorkDays()));
        response.setQuarterlyBonus(annualBonusService.calculateQuarterly(
                request.getPosition(), request.getSalary(), request.getBonus(), request.getWorkDays()));
    }

    private ResponseEntity<Response> failed(Response response, ErrorCodes errorCode, ErrorMessages errorMessage,
                                            HttpStatus status, Exception e) {
        log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        log.info("response: {}", response);
        return new ResponseEntity<>(response, status);
    }
}
