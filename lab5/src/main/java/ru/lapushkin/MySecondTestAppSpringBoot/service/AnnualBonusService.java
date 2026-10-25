package ru.lapushkin.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.lapushkin.MySecondTestAppSpringBoot.model.Positions;

@Service
public interface AnnualBonusService {

    double calculate(Positions positions, double salary, double bonus, int workDays);

    double calculateQuarterly(Positions positions, double salary, double bonus, int workDays);

}
