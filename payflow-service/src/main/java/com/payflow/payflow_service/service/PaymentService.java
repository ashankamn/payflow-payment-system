package com.payflow.payflow_service.service;

import com.payflow.payflow_service.dto.CreatePaymentRequest;
import com.payflow.payflow_service.model.Payment;
import com.payflow.payflow_service.model.PaymentStatus;
import com.payflow.payflow_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setSenderAccount(request.senderAccount());
        payment.setReceiverAccount(request.receiverAccount());
        payment.setAmount(request.amount());
        payment.setCurrency(request.currency());
        payment.setStatus(PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(String id) {
        return paymentRepository.findById(id);
    }
}
