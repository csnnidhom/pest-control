package com.holding.pestcontrol.service.worker.schedule;

import com.holding.pestcontrol.entity.Scheduling;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface Schedule {
    List<Scheduling> getAllSchedule();

    List<Scheduling> searchSchedule(String companyName, LocalDate startDate, LocalDate endDate);
}
