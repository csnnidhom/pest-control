package com.holding.pestcontrol.service.worker.schedule;

import com.holding.pestcontrol.entity.Scheduling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Slf4j
public class ScheduleSpecification {

    public static Specification<Scheduling> companyName(String companyName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("namaPerusahaan")), "%" + companyName.toLowerCase() + "%");
    }

    public static Specification<Scheduling> dateWorking(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("dateWorking"), startDate, endDate);
    }

}
