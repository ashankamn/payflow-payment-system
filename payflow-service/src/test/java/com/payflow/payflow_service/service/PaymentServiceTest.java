package com.payflow.payflow_service.service;

import com.payflow.payflow_service.dto.CreatePaymentRequest;
import com.payflow.payflow_service.event.PaymentEventProducer;
import com.payflow.payflow_service.model.Payment;
import com.payflow.payflow_service.model.PaymentStatus;
import com.payflow.payflow_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentService paymentService;

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
    void createPayment_savesPaymentAndPublishesEvent() {
        CreatePaymentRequest request = new CreatePaymentRequest(
                "ACC-001", "ACC-002", new BigDecimal("150.00"), "USD"
        );

        when(paymentRepository.save(any(Payment.class))).thenReturn(samplePayment);

        Payment result = paymentService.createPayment(request);

        assertNotNull(result);
        assertEquals("ACC-001", result.getSenderAccount());
        assertEquals("ACC-002", result.getReceiverAccount());
        assertEquals(PaymentStatus.PENDING, result.getStatus());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(paymentEventProducer, times(1)).publishPaymentEvent(any());
    }

    @Test
    void getPaymentById_returnsPayment_whenFound() {
        when(paymentRepository.findById("test-id-123")).thenReturn(Optional.of(samplePayment));

        Optional<Payment> result = paymentService.getPaymentById("test-id-123");

        assertTrue(result.isPresent());
        assertEquals("test-id-123", result.get().getId());
        verify(paymentRepository, times(1)).findById("test-id-123");
    }

    @Test
    void getPaymentById_returnsEmpty_whenNotFound() {
        when(paymentRepository.findById("missing-id")).thenReturn(Optional.empty());

        Optional<Payment> result = paymentService.getPaymentById("missing-id");

        assertTrue(result.isEmpty());
        verify(paymentRepository, times(1)).findById("missing-id");
    }
}
