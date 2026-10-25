package ru.lapushkin.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class AnnualBonusServiceImplTest {

    private final AnnualBonusServiceImpl service = new AnnualBonusServiceImpl();

    @Test
    void calculate() {

        // given
        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        // when
        double result = service.calculate(position, salary, bonus, workDays, Year.of(2025));

        // then
        double expected = 360493.8271604938;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateInLeapYear() {

        // given
        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        // when
        double result = service.calculate(position, salary, bonus, workDays, Year.of(2024));

        // then
        double expected = 361481.4814814815;
        assertThat(result).isCloseTo(expected, within(1e-6));
    }

    @Test
    void getDaysInYear() {
        assertThat(service.getDaysInYear(Year.of(2024))).isEqualTo(366);
        assertThat(service.getDaysInYear(Year.of(2025))).isEqualTo(365);
        assertThat(service.getDaysInYear(Year.of(2100))).isEqualTo(365);
        assertThat(service.getDaysInYear(Year.of(2000))).isEqualTo(366);
    }

    @Test
    void calculateQuarterlyForManager() {

        // given
        Positions position = Positions.TL;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        // when
        double result = service.calculateQuarterly(position, salary, bonus, workDays, Year.of(2025));

        // then
        double expected = 195267.4897119342;
        assertThat(result).isCloseTo(expected, within(1e-6));
    }

    @Test
    void calculateQuarterlyForNotManager() {

        // given
        Positions position = Positions.DEV;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        // when
        double result = service.calculateQuarterly(position, salary, bonus, workDays);

        // then
        assertThat(result).isZero();
    }
}
