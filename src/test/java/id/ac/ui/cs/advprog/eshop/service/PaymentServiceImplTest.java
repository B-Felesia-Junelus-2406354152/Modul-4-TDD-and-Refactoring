package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.validator.PaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentValidator validator;

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<PaymentValidator> validators = Collections.singletonList(validator);
        paymentService = new PaymentServiceImpl(paymentRepository, orderService, validators);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        products.add(product);
        order = new Order("order-1", products, 1708560000L, "Author");

        paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "1234567890");
    }

    @Test
    void testAddPaymentWithSupportedMethodAndUserDefinedId() {
        paymentData.put("paymentId", "custom-payment-id");

        when(validator.supports("BANK_TRANSFER")).thenReturn(true);
        when(validator.validate(paymentData)).thenReturn("SUCCESS");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertEquals("custom-payment-id", result.getId());
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("order-1", result.getPaymentData().get("orderId"));
        verify(paymentRepository, times(1)).save(result);
    }

    @Test
    void testAddPaymentWithNoDefinedIdGeneratesUuid() {
        when(validator.supports("BANK_TRANSFER")).thenReturn(true);
        when(validator.validate(paymentData)).thenReturn("REJECTED");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertNotNull(result.getId());
        assertFalse(result.getId().isEmpty());
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentUnsupportedMethod() {
        when(validator.supports("UNKNOWN")).thenReturn(false);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "UNKNOWN", paymentData);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testSetStatusSuccessSyncsOrderToSuccess() {
        paymentData.put("orderId", "order-1");
        Payment payment = new Payment("payment-1", "BANK_TRANSFER", paymentData, "PENDING");

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderService.findById("order-1")).thenReturn(order);

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        verify(orderService, times(1)).updateStatus("order-1", "SUCCESS");
    }

    @Test
    void testSetStatusRejectedSyncsOrderToFailed() {
        paymentData.put("orderId", "order-1");
        Payment payment = new Payment("payment-1", "BANK_TRANSFER", paymentData, "PENDING");

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderService.findById("order-1")).thenReturn(order);

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        verify(orderService, times(1)).updateStatus("order-1", "FAILED");
    }

    @Test
    void testSetStatusWithNoOrderId() {
        Payment payment = new Payment("payment-1", "BANK_TRANSFER", paymentData, "PENDING");
        when(paymentRepository.save(payment)).thenReturn(payment);

        paymentService.setStatus(payment, "SUCCESS");
        verify(orderService, never()).findById(anyString());
    }

    @Test
    void testSetStatusWithOrderNotFound() {
        paymentData.put("orderId", "invalid-order");
        Payment payment = new Payment("payment-1", "BANK_TRANSFER", paymentData, "PENDING");

        when(paymentRepository.save(payment)).thenReturn(payment);
        when(orderService.findById("invalid-order")).thenReturn(null);

        paymentService.setStatus(payment, "SUCCESS");
        verify(orderService, times(1)).findById("invalid-order");
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment("payment-1", "BANK_TRANSFER", paymentData);
        when(paymentRepository.findById("payment-1")).thenReturn(payment);

        Payment result = paymentService.getPayment("payment-1");
        assertEquals(payment, result);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("payment-1", "BANK_TRANSFER", paymentData));
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
    }
}