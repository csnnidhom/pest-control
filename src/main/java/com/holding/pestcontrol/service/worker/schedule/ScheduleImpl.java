package com.holding.pestcontrol.service.worker.schedule;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.entity.Worker;
import com.holding.pestcontrol.repository.*;
import com.holding.pestcontrol.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleImpl implements Schedule {

    private final ValidationService validateService;

    private final ClientRepository clientRepository;

    private final SchedulingRepository schedulingRepository;

    private final UserRepository userRepository;

    private final WorkerRepository workerRepository;

    private final ServiceTreatmenSlipRepository serviceTreatmenSlipRepository;

    @Override
    public List<Scheduling> getAllSchedule() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        Worker worker = (Worker) workerRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Detail Client Not Found"));

        return schedulingRepository.findAllByWorker(worker);
    }

    @Override
    public List<Scheduling> searchSchedule(String companyName, LocalDate startDate, LocalDate endDate) {

        Specification<Scheduling> specification = Specification.where(null);

        if (companyName != null && !companyName.isEmpty()){
            specification = specification.and(ScheduleSpecification.companyName(companyName));
        }

        if (startDate != null && endDate != null){
            specification = specification.and(ScheduleSpecification.dateWorking(startDate, endDate));
        }

        return schedulingRepository.findAll(specification);
    }


}
