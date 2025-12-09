package com.mtogo.financial_service.application.services.helpers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.Role;

public class CommissionCalculator {

   
    public static List<Commission> calculateFor(Payment payment) {
        
        double orderAmount = payment.getAmount();
        List<Commission> commissions = new ArrayList<>();

        double mtogoFee = calculateMToGoFee(orderAmount);
        commissions.add(new Commission(payment, Role.MTOGO, mtogoFee));

        double deliveryFee = calculateDeliveryAgentCommission(payment, orderAmount);
        commissions.add(new Commission(payment, Role.DELIVERYAGENT, deliveryFee));

        double restaurantFee = orderAmount - mtogoFee - deliveryFee;
        commissions.add(new Commission(payment, Role.RESTAURANT, restaurantFee));

        return commissions;
        
    }

    private static double calculateMToGoFee(double orderAmount) {
        double fee = 0.0;
        double remaining = orderAmount;

        if (remaining > 1000) {
            fee += (remaining - 1000) * 0.03;
            remaining = 1000;
        }
        if (remaining > 500) {
            fee += (remaining - 500) * 0.04;
            remaining = 500;
        }
        if (remaining > 100) {
            fee += (remaining - 100) * 0.05;
            remaining = 100;
        }
        fee += remaining * 0.06;

        return fee;
    }

   
    public static double calculateDeliveryAgentCommission(Payment payment, double amount) {
        double bonusAmountCommission = 0.0;

        LocalDateTime createdAt = payment.getCreatedAt();
        int hour = createdAt.getHour();
        Month month = createdAt.getMonth();
        int day = createdAt.getDayOfMonth();

    
        if (hour >= 20) {
            bonusAmountCommission += amount * 0.10;
        } else {
            bonusAmountCommission += amount * 0.05; 
        }
       
        if ((month == Month.JANUARY && day == 1) || (month == Month.DECEMBER && day == 25)) {
            bonusAmountCommission += amount * 0.05; 
        }

        return bonusAmountCommission;
    }
}

