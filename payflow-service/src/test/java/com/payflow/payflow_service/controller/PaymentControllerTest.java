package com.payflow.payflow_service.controller;

import com.payflow.payflow_service.dto.CreatePaymentRequest;
import com.payflow.payflow_service.model.Payment;
import com.payflow.payflow_service.model.PaymentStatus;
import com.payflow.payflow_service.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        samplePayment = new Payment();
        samplePayment.setId("test-id-123");
        samplePayment.setSenderAccount("ACC-001");
        samplePayment.setReceiverAccount("ACC-002");
        samplePayment.setAmount(new BigDecimal("150.00"));
        samplePayment.setCurrency("USD");
        samplePayment.setStatus(PaymentStatus.PENDING);
        samplePayment.setCreatedAt(Instant.now());
    }

    @Test
    void createPayment_returns201Created() {
        CreatePaymentRequest request = new CreatePaymentRequest(
                "ACC-001", "ACC-002", new BigDecimal("150.00"), "USD"
        );
        when(paymentService.createPayment(any())).thenReturn(samplePayment);

        ResponseEntity<Payment> response = paymentController.createPayment(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test-id-123", response.getBody().getId());
    }

    @Test
    void getAllPayments_returnsListOfPayments() {
        when(paymentService.getAllPayments()).thenReturn(List.of(samplePayment));

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getPaymentById_returns200_whenFound() {
        when(paymentService.getPaymentById("test-id-123")).thenReturn(Optional.of(samplePayment));

        ResponseEntity<Payment> response = paymentController.getPaymentById("test-id-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test-id-123", response.getBody().getId());
    }

    @Test
    void getPaymentById_returns404_whenNotFound() {
        when(paymentService.getPaymentById("missing-id")).thenReturn(Optional.empty());

        ResponseEntity<Payment> response = paymentController.getPaymentById("missing-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
