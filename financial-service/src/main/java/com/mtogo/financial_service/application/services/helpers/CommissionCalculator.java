package com.mtogo.financial_service.application.services.helpers;

import java.util.ArrayList;
import java.util.List;

import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.Role;

public class CommissionCalculator {

    public static List<Commission> calculateFor(Payment payment) {
        double orderAmount = payment.getAmount();
        List<Commission> commissions = new ArrayList<>();

        double mtogoFee = 0.0;
        double remaining = orderAmount;

        if (remaining > 1000) {
            mtogoFee += (remaining - 1000) * 0.03;
            remaining = 1000;
        }
        if (remaining > 500) {
            mtogoFee += (remaining - 500) * 0.04;
            remaining = 500;
        }
        if (remaining > 100) {
            mtogoFee += (remaining - 100) * 0.05;
            remaining = 100;
        }
        mtogoFee += remaining * 0.06;

        commissions.add(new Commission(payment, Role.MTOGO, mtogoFee));

        double deliveryFee = 10.0; // Flat delivery fee for the Agent
        commissions.add(new Commission(payment, Role.DELIVERYAGENT, deliveryFee));

        double restaurantFee = orderAmount - mtogoFee - deliveryFee;
        commissions.add(new Commission(payment, Role.RESTAURANT, restaurantFee));

        return commissions;
    }
}
