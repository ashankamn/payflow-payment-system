package com.payflow.payflow_service.service;

import com.payflow.payflow_service.dto.CreatePaymentRequest;
import com.payflow.payflow_service.event.PaymentEvent;
import com.payflow.payflow_service.event.PaymentEventProducer;
import com.payflow.payflow_service.model.Payment;
import com.payflow.payflow_service.model.PaymentStatus;
import com.payflow.payflow_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentService(PaymentRepository paymentRepository, PaymentEventProducer paymentEventProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
    }

    public Payment createPayment(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setSenderAccount(request.senderAccount());
        payment.setReceiverAccount(request.receiverAccount());
        payment.setAmount(request.amount());
        payment.setCurrency(request.currency());
        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);

        PaymentEvent event = new PaymentEvent(
                savedPayment.getId(),
                savedPayment.getSenderAccount(),
                savedPayment.getReceiverAccount(),
                savedPayment.getAmount(),
                savedPayment.getCurrency(),
                savedPayment.getStatus().name(),
                savedPayment.getCreatedAt()
        );
        paymentEventProducer.publishPaymentEvent(event);

        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(String id) {
        return paymentRepository.findById(id);
    }
}
