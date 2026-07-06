package com.payflow.payflow_service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PaymentEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventProducer.class);
    private static final String TOPIC = "payment-events";

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentEvent(PaymentEvent event) {
        logger.info("Publishing payment event for paymentId={}", event.getPaymentId());

        kafkaTemplate.send(TOPIC, event.getPaymentId(), event).whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("FAILED to publish payment event for paymentId={}: {}",
                        event.getPaymentId(), ex.getMessage(), ex);
            } else {
                logger.info("SUCCESSFULLY published paymentId={} to partition={}, offset={}",
                        event.getPaymentId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
