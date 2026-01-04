package com.mtogo.financial_service.application.services.helpers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.Role;

public class CommissionCalculator {

    public static List<Commission> calculateCommissions(Payment payment) {
        
        double orderAmount = payment.getAmount();
        
        List<Commission> commissions = new ArrayList<>();

        double mtogoFee = calculateMTOGOFee(orderAmount);
        commissions.add(new Commission(payment, Role.MTOGO, mtogoFee));

        double deliveryFee = calculateDeliveryAgentCommission(payment);
        commissions.add(new Commission(payment, Role.DELIVERYAGENT, deliveryFee));

        double restaurantFee = orderAmount - mtogoFee - deliveryFee;
        commissions.add(new Commission(payment, Role.RESTAURANT, restaurantFee));

        return commissions;
    }

    private static double calculateMTOGOFee(double totalAmount) {
        
        double fee = 0.0;

        double amount = totalAmount;

        // Incremental fee calculation based on amount
        if (amount > 1000) {
            fee += (amount - 1000) * 0.03;
            amount = 1000;
        }
        if (amount > 500) {
            fee += (amount - 500) * 0.04;
            amount = 500;
        }
        if (amount > 100) {
            fee += (amount - 100) * 0.05;
            amount = 100;
        }

        fee += amount * 0.06;

        return fee;
    }

   
    public static double calculateDeliveryAgentCommission(Payment payment) {
        
        double bonusFee = 0.0;

        double amount = payment.getAmount();

        LocalDateTime createdAt = payment.getCreatedAt();
        int hour = createdAt.getHour();
        Month month = createdAt.getMonth();
        int day = createdAt.getDayOfMonth();

        // Time-based bonus
        if (hour >= 20) {
            bonusFee += amount * 0.10;
        } else {
            bonusFee += amount * 0.05; 
        }
       
        // Extra holiday bonus
        if ((month == Month.JANUARY && day == 1) || (month == Month.DECEMBER && day == 24)) {
            bonusFee += amount * 0.05; 
        }

        return bonusFee;
    }
}

