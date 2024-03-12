package com.holding.pestcontrol.service;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.entity.Worker;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class SpecificationSearch {


    public static Specification<Scheduling> findAllScheduleByWorkerAuthentication(String companyName,LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) -> {

            String authentication = SecurityContextHolder.getContext().getAuthentication().getName();

            Join<Scheduling, Worker> workerJoin = root.join("worker", JoinType.INNER);
            Join<Worker, User> userJoin = workerJoin.join("user", JoinType.INNER);

            if (companyName == null){
                if (startDate == null || endDate == null) {
                    LocalDate startDateNow = LocalDate.now();
                    LocalDate endDateNow = LocalDate.now();

                    return criteriaBuilder.and(
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDateNow, endDateNow)
                    );
                }else {
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDate, endDate)
                    );
                }

            } else {
                if (startDate == null || endDate == null) {
                    LocalDate startDateNow = LocalDate.now();
                    LocalDate endDateNow = LocalDate.now();

                    return criteriaBuilder.and(
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDateNow, endDateNow)
                    );
                }else {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("namaPerusahaan")), "%" + companyName.toLowerCase() + "%"),
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDate, endDate)
                    );
                }
            }
        };
    }

//    public static Specification<Scheduling> findAllScheduleByWorkerName(String workerName, String namaPerusahaan){
//        return (root, query, criteriaBuilder) -> {
//            Join<Scheduling, Worker> workerJoin = root.join("worker", JoinType.INNER);
//            Join<Scheduling, Client> clientJoin = root.join("client", JoinType.INNER);
//            return criteriaBuilder.like(criteriaBuilder.lower(workerJoin.get("namaLengkap")), "%" + workerName.toLowerCase() + "%");
//        };
//    }

    public static Specification<Scheduling> dateWorking(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("dateWorking"), startDate, endDate);
    }

    public static Specification<Scheduling> companyNameSchedule(String companyName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("namaPerusahaan")), "%" + companyName.toLowerCase() + "%");
    }

    public static Specification<Client> companyNameClient(String companyName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("namaPerusahaan")), "%" + companyName.toLowerCase() + "%");
    }

    public static Specification<Client> companyAddress(String companyAddress){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("alamat")), "%" + companyAddress.toLowerCase() + "%");
    }

    public static Specification<Client> companyEmail(String companyEmail){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("user")), companyEmail.toLowerCase() + "%");
    }

    public static Specification<Worker> workerFullname(String workerFullname){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("namaLengkap")), workerFullname.toLowerCase() + "%");
    }

    public static Specification<Worker> workerAddress(String workerAddress){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("alamat")), workerAddress.toLowerCase() + "%");
    }

    public static Specification<Worker> workerEmail(String workerEmail){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("user")), workerEmail.toLowerCase() + "%");
    }

}
