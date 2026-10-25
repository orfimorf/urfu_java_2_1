package ru.lapushkin.MySecondTestAppSpringBoot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    /**
     * Уникальный идентификатор сообщения
     */
    private String uid;

    /**
     * Уникальный идентификатор операции
     */
    private String operationUid;

    /**
     * Время создания сообщения
     */
    private String systemTime;

    /**
     * Результат обработки сообщения: «success» или «failed»
     */
    private Codes code;

    /**
     * Годовая премия сотрудника
     */
    private Double annualBonus;

    /**
     * Квартальная премия сотрудника (начисляется только управленцам)
     */
    private Double quarterlyBonus;

    /**
     * Код ошибки: «UnsupportedCodeException», «ValidationException», «UnknownException»
     */
    private ErrorCodes errorCode;

    /**
     * Сообщение об ошибке
     */
    private ErrorMessages errorMessage;

}
