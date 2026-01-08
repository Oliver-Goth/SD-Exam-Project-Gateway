import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Order confirmed event is published"

    label "order_confirmed"

    input {
        triggeredBy("confirmOrder()")
    }

    outputMessage {
        sentTo("order.exchange")
        body(
            orderId: 8,
            status: "CONFIRMED"
        )
        headers {
            header("contentType", applicationJson())
            header("routingKey", "order.confirmed")
        }
    }
}
