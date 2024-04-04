package com.holding.pestcontrol.service;

import com.holding.pestcontrol.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class SpecificationSearch {

    public static Specification<Scheduling> findAllScheduleByWorkerAuthentication(String companyName, LocalDate startDate, LocalDate endDate){
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
                            criteriaBuilder.like(root.get("client").get("namaPerusahaan"), "%" + companyName.toLowerCase() + "%"),
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDateNow, endDateNow)
                    );
                }else {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("client").get("namaPerusahaan"), "%" + companyName.toLowerCase() + "%"),
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDate, endDate)
                    );
                }
            }
        };
    }

    public static Specification<ServiceTreatmentSlip> findAllServicetreatmentByWorkerAuthentication(String companyName,LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) -> {

            String authentication = SecurityContextHolder.getContext().getAuthentication().getName();

            Join<ServiceTreatmentSlip, Scheduling> serviceTreatmentSlipSchedulingJoin = root.join("scheduling", JoinType.INNER);
            Join<Scheduling, Worker> workerJoin = serviceTreatmentSlipSchedulingJoin.join("worker", JoinType.INNER);
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
                            criteriaBuilder.like(root.get("client").get("namaPerusahaan"), "%" + companyName.toLowerCase() + "%"),
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDateNow, endDateNow)
                    );
                }else {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("scheduling").get("client").get("namaPerusahaan"), "%" + companyName.toLowerCase() + "%"),
                            criteriaBuilder.equal(userJoin.get("email"), authentication),
                            criteriaBuilder.between(root.get("dateWorking"), startDate, endDate)
                    );
                }
            }
        };
    }

    public static Specification<ServiceTreatmentSlip> findAllServicetreatmentByClientAuthentication(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) -> {

            String authentication = SecurityContextHolder.getContext().getAuthentication().getName();

            Join<ServiceTreatmentSlip, Scheduling> serviceTreatmentSlipSchedulingJoin = root.join("scheduling", JoinType.INNER);
            Join<Scheduling, Client> clientJoin = serviceTreatmentSlipSchedulingJoin.join("client", JoinType.INNER);
            Join<Client, User> userJoin = clientJoin.join("user", JoinType.INNER);

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

        };
    }

    public static Specification<Scheduling> dateWorkingScheduling(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("dateWorking"), startDate, endDate);
    }

    public static Specification<ServiceTreatmentSlip> dateWorkingTreatment(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("dateWorking"), startDate, endDate);
    }

    public static Specification<Scheduling> companyNameSchedule(String companyName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("namaPerusahaan")), "%" + companyName.toLowerCase() + "%");
    }

    public static Specification<ServiceTreatmentSlip> companyNameTreatment(String companyName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("scheduling").get("client").get("namaPerusahaan")), "%" + companyName.toLowerCase() + "%");
    }

    public static Specification<Client> companyNameClient(String companyName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("namaPerusahaan")), "%" + companyName.toLowerCase() + "%");
    }
//    public static Specification<Client> findAllClientStatusTrue() {
//        return (root, query, criteriaBuilder) -> {
//            Join<Client, User> userJoin = root.join("user");
//            return criteriaBuilder.isTrue(userJoin.get("status"));
//        };
//    }
//
//    public static Specification<Worker> findAllWorkerStatusTrue() {
//        return (root, query, criteriaBuilder) -> {
//            Join<Client, Worker> userJoin = root.join("user");
//            return criteriaBuilder.isTrue(userJoin.get("status"));
//        };
//    }

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
