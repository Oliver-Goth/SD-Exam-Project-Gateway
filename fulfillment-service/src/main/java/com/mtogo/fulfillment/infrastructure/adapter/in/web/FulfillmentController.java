package com.mtogo.fulfillment.infrastructure.adapter.in.web;

import com.mtogo.fulfillment.domain.port.in.MarkFulfillmentPreparedUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fulfillments")
public class FulfillmentController {

    private final MarkFulfillmentPreparedUseCase markPreparedUseCase;

    public FulfillmentController(MarkFulfillmentPreparedUseCase markPreparedUseCase) {
        this.markPreparedUseCase = markPreparedUseCase;
    }

    @PostMapping("/orders/{orderId}/prepared")
    public ResponseEntity<Void> markPrepared(@PathVariable("orderId") Long orderId) {
        markPreparedUseCase.markPrepared(orderId);
        return ResponseEntity.accepted().build();
    }
}
