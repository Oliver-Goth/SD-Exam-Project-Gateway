package com.mtogo.financial_service.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import com.mtogo.financial_service.infrastructure.adapters.out.messaging.consumers.PaymentEventConsumer;

/*

Summary of the flow

- Producer sends a message to payment.exchange with routing key payment.routingKey.

- Exchange routes the message to payment.queue.

- SimpleMessageListenerContainer detects the new message.

- MessageListenerAdapter wraps your receiver and calls receiveMessage.

- Your receiver maps the event to CreatePaymentCommand and calls the Payment service.

*/

@Configuration
public class RabbitMqConfig {

    /*************************************************************************************************************************/
    // PAYMENT QUEUE AND EXCHANGE CONFIGURATION
    /*************************************************************************************************************************/


    // Queue, exchange, and routing key for payment events
    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PAYMENT_ROUTING_KEY = "payment.routingKey";

    // Creates the payment queue
    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    // Creates the payment exchange. Exchanges in RabbitMQ are like routers: they receive messages and send them to queues based on routing keys.
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    // Connects the queue to the exchange with a routing key (payment.routingKey).
    // Only messages sent to the exchange with this routing key will go to this queue.
    @Bean
    public Binding binding(Queue paymentQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue).to(paymentExchange).with(PAYMENT_ROUTING_KEY);
    }

    /* 
    // Listener container for payment events
    @Bean
    public SimpleMessageListenerContainer paymentContainer(ConnectionFactory connectionFactory,
                                                        MessageListenerAdapter listenerAdapterPayment) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PAYMENT_QUEUE);
        container.setMessageListener(listenerAdapterPayment);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterPayment(PaymentEventConsumer receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    } */

    /*************************************************************************************************************************/
    // ORDERS QUEUE AND EXCHANGE CONFIGURATION
    /*************************************************************************************************************************/

    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.confirmed";

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
