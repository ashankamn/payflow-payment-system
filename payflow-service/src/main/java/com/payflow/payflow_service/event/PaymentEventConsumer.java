package com.payflow.payflow_service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PaymentEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventConsumer.class);

    @KafkaListener(topics = "payment-events", groupId = "payflow-group")
    public void consumePaymentEvent(PaymentEvent event) {
        logger.info("Received payment event: paymentId={}, sender={}, receiver={}, amount={} {}, status={}",
                event.getPaymentId(), event.getSenderAccount(), event.getReceiverAccount(),
                event.getAmount(), event.getCurrency(), event.getStatus());

        // Simulate downstream processing (fraud check, notification, ledger update, etc.)
        logger.info("Processing payment asynchronously... paymentId={}", event.getPaymentId());
    }
}
