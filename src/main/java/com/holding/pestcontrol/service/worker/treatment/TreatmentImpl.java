package com.holding.pestcontrol.service.worker.treatment;

import com.holding.pestcontrol.dto.ReqResServiceTreatmentSlip;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreatmentImpl implements Treatment {

    private final ValidationService validate;

    private final ClientRepository clientRepository;

}
