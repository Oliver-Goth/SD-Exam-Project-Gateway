package com.mtogo.fulfillment.infrastructure.adapter.in.web;

import com.example.AuditLogger;
import com.example.RequestLogger;
import com.example.TimeIt;
import com.mtogo.fulfillment.domain.model.FulfillmentOrder;
import com.mtogo.fulfillment.domain.port.in.MarkFulfillmentPreparedUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fulfillments")
public class FulfillmentController {

    private final MarkFulfillmentPreparedUseCase markPreparedUseCase;
    private static final Logger log = LoggerFactory.getLogger(FulfillmentController.class);

    public FulfillmentController(MarkFulfillmentPreparedUseCase markPreparedUseCase) {
        this.markPreparedUseCase = markPreparedUseCase;
    }


   @PostMapping("/orders/{orderId}/prepared")
   public ResponseEntity<Void> markPrepared(@PathVariable("orderId") Long orderId) {
       String actorId = String.valueOf(orderId);

       markPreparedUseCase.markPrepared(
               RequestLogger.log(
                       actorId,
                       "POST",
                       "/orders/{orderId}/prepared",
                       202,

                       () -> TimeIt.info(log, "Mark Prepared", () -> {
                           markPreparedUseCase.markPrepared(orderId);

                           AuditLogger.log(
                                   "MARK_PREPARED",
                                   actorId,
                                   "order prepared:" + orderId,
                                   "203.0.113.7"
                           );

                           return null;
                       })
               )
       );

       return ResponseEntity.accepted().build();
   }



}
