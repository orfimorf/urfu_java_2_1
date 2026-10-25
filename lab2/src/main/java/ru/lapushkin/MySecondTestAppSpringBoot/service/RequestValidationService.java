package ru.lapushkin.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.lapushkin.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.lapushkin.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Request;

@Service
public class RequestValidationService implements ValidationService {

    private static final String UNSUPPORTED_UID = "123";

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult.getFieldError().toString());
        }
    }

    @Override
    public void isSupported(Request request) throws UnsupportedCodeException {
        if (UNSUPPORTED_UID.equals(request.getUid())) {
            throw new UnsupportedCodeException("uid " + UNSUPPORTED_UID + " не поддерживается");
        }
    }
}
