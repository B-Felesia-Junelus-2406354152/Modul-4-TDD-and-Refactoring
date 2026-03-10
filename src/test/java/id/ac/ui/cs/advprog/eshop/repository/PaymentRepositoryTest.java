package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment1;
    Payment payment2;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("bankName", "BCA");
        paymentData1.put("referenceCode", "1234567890");
        payment1 = new Payment("payment-1", "BANK_TRANSFER", paymentData1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "Mandiri");
        paymentData2.put("referenceCode", "0987654321");
        payment2 = new Payment("payment-2", "BANK_TRANSFER", paymentData2);
    }

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(payment1);

        Payment savedPayment = paymentRepository.findById(payment1.getId());
        assertNotNull(savedPayment);
        assertEquals(payment1.getId(), savedPayment.getId());
        assertEquals(payment1.getMethod(), savedPayment.getMethod());
        assertEquals(payment1.getPaymentData(), savedPayment.getPaymentData());
    }

    @Test
    void testSaveUpdate() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        Map<String, String> newPaymentData = new HashMap<>();
        newPaymentData.put("bankName", "BNI");
        newPaymentData.put("referenceCode", "55555");
        Payment payment2Updated = new Payment("payment-2", "BANK_TRANSFER", newPaymentData, "SUCCESS");

        Payment result = paymentRepository.save(payment2Updated);

        assertEquals("payment-2", result.getId());
        assertEquals("SUCCESS", result.getStatus());

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());

        assertEquals("SUCCESS", allPayments.get(1).getStatus());
        assertEquals("BNI", allPayments.get(1).getPaymentData().get("bankName"));
    }

    @Test
    void testFindByIdIfFound() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        Payment result = paymentRepository.findById(payment2.getId());
        assertNotNull(result);
        assertEquals(payment2.getId(), result.getId());
    }

    @Test
    void testFindByIdIfNotFound() {
        paymentRepository.save(payment1);

        Payment result = paymentRepository.findById("invalid-id");
        assertNull(result);
    }

    @Test
    void testFindAllIfEmpty() {
        List<Payment> result = paymentRepository.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllIfPopulated() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> result = paymentRepository.findAll();
        assertEquals(2, result.size());
        assertEquals(payment1.getId(), result.get(0).getId());
        assertEquals(payment2.getId(), result.get(1).getId());
    }
}