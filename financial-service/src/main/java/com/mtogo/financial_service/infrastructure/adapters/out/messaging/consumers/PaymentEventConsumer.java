/* 
package com.mtogo.financial_service.infrastructure.adapters.out.messaging.consumers;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mtogo.financial_service.infrastructure.adapters.out.messaging.events.PaymentEvent;

@Component
public class PaymentEventConsumer {

    final private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "payment.queue")
    public void receiveMessage(PaymentEvent event) {
        System.out.println("Received payment event: " + event);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
*/