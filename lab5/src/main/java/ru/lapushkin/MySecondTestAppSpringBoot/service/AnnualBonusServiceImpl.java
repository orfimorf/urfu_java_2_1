package ru.lapushkin.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

@Slf4j
@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    private static final int QUARTERS_IN_YEAR = 4;

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        return calculate(positions, salary, bonus, workDays, Year.now());
    }

    @Override
    public double calculateQuarterly(Positions positions, double salary, double bonus, int workDays) {
        return calculateQuarterly(positions, salary, bonus, workDays, Year.now());
    }

    double calculate(Positions positions, double salary, double bonus, int workDays, Year year) {
        return salary * bonus * getDaysInYear(year) * positions.getPositionCoefficient() / workDays;
    }

    double calculateQuarterly(Positions positions, double salary, double bonus, int workDays, Year year) {
        if (!positions.isManager()) {
            log.info("Квартальная премия не начисляется: должность {} не является управленческой", positions);
            return 0;
        }
        return calculate(positions, salary, bonus, workDays, year) / QUARTERS_IN_YEAR;
    }

    int getDaysInYear(Year year) {
        return year.isLeap() ? 366 : 365;
    }
}
