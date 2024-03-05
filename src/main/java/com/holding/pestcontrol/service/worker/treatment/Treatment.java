package com.holding.pestcontrol.service.worker.treatment;

import com.holding.pestcontrol.entity.Scheduling;

import java.util.List;
import java.util.Optional;

public interface Treatment {
    List<Scheduling> getAllSchedule();
}
