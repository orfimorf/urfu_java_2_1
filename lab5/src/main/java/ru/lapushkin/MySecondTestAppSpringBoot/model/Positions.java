package ru.lapushkin.MySecondTestAppSpringBoot.model;

import lombok.Getter;

@Getter
public enum Positions {

    DEV(2.2, false),
    HR(1.2, false),
    TL(2.6, true),
    PO(2.4, true),
    TPM(2.5, true),
    CTO(3.0, true);

    /**
     * Коэффициент должности, используемый при расчёте премии
     */
    private final double positionCoefficient;

    /**
     * Признак управленческой должности
     */
    private final boolean isManager;

    Positions(double positionCoefficient, boolean isManager) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }
}
