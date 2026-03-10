package id.ac.ui.cs.advprog.eshop.service.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class VoucherCodeValidatorTest {
    private VoucherCodeValidator validator;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        validator = new VoucherCodeValidator();
        paymentData = new HashMap<>();
    }

    @Test
    void testSupports() {
        assertTrue(validator.supports("VOUCHER_CODE"));
        assertFalse(validator.supports("BANK_TRANSFER"));
    }

    @Test
    void testValidateSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        assertEquals("SUCCESS", validator.validate(paymentData));
    }

    @Test
    void testValidateNull() {
        assertEquals("REJECTED", validator.validate(paymentData)); // voucherCode tidak ada
    }

    @Test
    void testValidateWrongLength() {
        paymentData.put("voucherCode", "ESHOP123");
        assertEquals("REJECTED", validator.validate(paymentData));
    }

    @Test
    void testValidateNotStartsWithEshop() {
        paymentData.put("voucherCode", "KODE-1234ABC5678"); // 16 char, but not ESHOP
        assertEquals("REJECTED", validator.validate(paymentData));
    }

    @Test
    void testValidateWrongDigitCount() {
        paymentData.put("voucherCode", "ESHOP1234ABC567A"); // 16 char, starts with ESHOP, but 7 digits
        assertEquals("REJECTED", validator.validate(paymentData));
    }
}
