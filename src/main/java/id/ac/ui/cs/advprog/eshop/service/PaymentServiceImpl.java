package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.validator.PaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final List<PaymentValidator> validators;

    // Gunakan Constructor Injection
    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderService orderService,
                              List<PaymentValidator> validators) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.validators = validators; // Spring otomatis mengumpulkan semua kelas yang mengimplementasikan PaymentValidator
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        // 1. OCP & SRP diterapkan di sini. Service tidak perlu tahu detail validasi.
        String status = PaymentStatus.REJECTED.getValue();
        for (PaymentValidator validator : validators) {
            if (validator.supports(method)) {
                status = validator.validate(paymentData);
                break;
            }
        }

        paymentData.put("orderId", order.getId());

        String paymentId = paymentData.get("paymentId");
        if (paymentId == null || paymentId.trim().isEmpty()) {
            paymentId = UUID.randomUUID().toString();
        }

        Payment payment = new Payment(paymentId, method, paymentData, status);
        Payment savedPayment = paymentRepository.save(payment);
        
        // As requested by business logic, we must sync the order status immediately 
        // if the payment status is SUCCESS or REJECTED
        syncOrderStatus(savedPayment, status);
        
        return savedPayment;
    }

    @Override
    public Payment setStatus(Payment payment, String paymentStatus) {
        payment.setStatus(paymentStatus);
        Payment savedPayment = paymentRepository.save(payment);

        // Ekstraksi logika sinkronisasi Order agar lebih mudah dibaca (SRP)
        syncOrderStatus(savedPayment, paymentStatus);

        return savedPayment;
    }

    private void syncOrderStatus(Payment payment, String paymentStatus) {
        String orderId = payment.getPaymentData().get("orderId");
        if (orderId == null) return;

        Order order = orderService.findById(orderId);
        if (order != null) {
            if (PaymentStatus.SUCCESS.getValue().equals(paymentStatus)) {
                orderService.updateStatus(order.getId(), "SUCCESS");
            } else if (PaymentStatus.REJECTED.getValue().equals(paymentStatus)) {
                orderService.updateStatus(order.getId(), "FAILED");
            }
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}