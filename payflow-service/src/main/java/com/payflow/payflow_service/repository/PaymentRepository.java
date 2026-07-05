package com.payflow.payflow_service.repository;

import com.payflow.payflow_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
