package com.mtogo.financial_service.domain.port.in;

import java.util.List;
import com.mtogo.financial_service.domain.model.Commission;

public interface GetCommissionsUseCase {

    List<Commission> getCommissions(Long paymentId);
    
}
