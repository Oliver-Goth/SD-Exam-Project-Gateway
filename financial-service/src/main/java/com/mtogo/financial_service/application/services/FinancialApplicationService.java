package com.mtogo.financial_service.application.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtogo.financial_service.application.exceptions.PaymentNotCreatedException;
import com.mtogo.financial_service.application.exceptions.PaymentNotFoundException;
import com.mtogo.financial_service.application.exceptions.PaymentStatusNotFoundException;
import com.mtogo.financial_service.application.services.helpers.CommissionCalculator;
import com.mtogo.financial_service.application.services.helpers.PaymentProviderCalculator;
import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;
import com.mtogo.financial_service.domain.port.in.ConfirmPaymentProvider;
import com.mtogo.financial_service.domain.port.in.CreatePaymentCommand;
import com.mtogo.financial_service.domain.port.in.CreatePaymentUseCase;
import com.mtogo.financial_service.domain.port.in.GetCommissionsUseCase;
import com.mtogo.financial_service.domain.port.in.GetPaymentUseCase;
import com.mtogo.financial_service.domain.port.in.PaymentStatusUseCase;
import com.mtogo.financial_service.domain.port.out.CommissionEventPublisherPort;
import com.mtogo.financial_service.domain.port.out.CommissionRepositoryPort;
import com.mtogo.financial_service.domain.port.out.PaymentEventPublisherPort;
import com.mtogo.financial_service.domain.port.out.PaymentRepositoryPort;
import com.mtogo.financial_service.infrastructure.adapters.out.messaging.events.PaymentEvent;


@Service
@Transactional
public class FinancialApplicationService implements
        CreatePaymentUseCase,
        GetPaymentUseCase,
        PaymentStatusUseCase,
        GetCommissionsUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentEventPublisherPort eventPublisherPayment;
    private final CommissionRepositoryPort commissionsRepositoryPort;
    private final CommissionEventPublisherPort eventPublisherCommission;

    public FinancialApplicationService(PaymentRepositoryPort paymentRepositoryPort,
            PaymentEventPublisherPort eventPublisherPayment,
            CommissionRepositoryPort commissionsRepositoryPort,
            CommissionEventPublisherPort eventPublisherCommission  ) {
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.eventPublisherPayment = eventPublisherPayment;
        this.commissionsRepositoryPort = commissionsRepositoryPort;
        this.eventPublisherCommission = eventPublisherCommission;
    }

    @Override
    public Payment createPayment(CreatePaymentCommand command) {

        Payment payment = new Payment(
                command.orderId,
                command.amount,
                PaymentStatus.PENDING, 
                "", 
                "" 
        );

        ConfirmPaymentProvider provider = new PaymentProviderCalculator();

        provider.processPayment(payment); 

        Payment savedPayment = paymentRepositoryPort.save(payment);

        List<Commission> commissions = CommissionCalculator.calculateFor(savedPayment);
        commissions.forEach(savedPayment::addCommission);

        paymentRepositoryPort.save(savedPayment);

        if (savedPayment == null || savedPayment.getId() == null) {
            throw new PaymentNotCreatedException(command.orderId);
        }

        if (savedPayment.getStatus() == PaymentStatus.COMPLETED) {
            eventPublisherPayment.publish(PaymentEvent.created(savedPayment));
        }

        return savedPayment;
    }

    @Override
    public Payment getPayment(Long id) {
        return paymentRepositoryPort.findByPaymentId(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public Payment getPaymentStatus(Long paymentId) {
        return paymentRepositoryPort.getStatus(paymentId)
                .orElseThrow(() -> new PaymentStatusNotFoundException(paymentId));
    }

    @Override
    public List<Commission> getCommissions(Long paymentId) {

        return commissionsRepositoryPort.findByPaymentId(paymentId);

    }

}
