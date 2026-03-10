package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp(){
        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePaymentSuccess() {
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", this.paymentData);

        assertEquals("payment-1", payment.id);
        assertEquals("VOUCHER_CODE", payment.method);
        assertEquals("PENDING", payment.status);
        assertEquals(this.paymentData, payment.paymentData);
    }

    @Test
    void testCreatePaymentWithEmptyPaymentData() {
        Map<String, String> emptyData = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-1", "VOUCHER_CODE", emptyData);
        });
    }

    @Test
    void testCreatePaymentWithStatusSuccess() {
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", this.paymentData, "SUCCESS");
        assertEquals("SUCCESS", payment.status);
    }

    @Test
    void testCreatePaymentWithStatusRejected() {
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", this.paymentData, "REJECTED");
        assertEquals("REJECTED", payment.status);
    }

    @Test
    void testCreatePaymentWithStatusInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-1", "VOUCHER_CODE", this.paymentData, "UNKNOWN_STATUS");
        });
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", this.paymentData);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.status);
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", this.paymentData);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.status);
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", this.paymentData);
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("UNKNOWN_STATUS");
        });
    }
}