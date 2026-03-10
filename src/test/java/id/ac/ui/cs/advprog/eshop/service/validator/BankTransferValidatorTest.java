package id.ac.ui.cs.advprog.eshop.service.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BankTransferValidatorTest {
    private BankTransferValidator validator;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        validator = new BankTransferValidator();
        paymentData = new HashMap<>();
    }

    @Test
    void testSupports() {
        assertTrue(validator.supports("BANK_TRANSFER"));
        assertFalse(validator.supports("VOUCHER_CODE"));
    }

    @Test
    void testValidateSuccess() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "12345");
        assertEquals("SUCCESS", validator.validate(paymentData));
    }

    @Test
    void testValidateBankNameNullOrEmpty() {
        paymentData.put("referenceCode", "12345");
        assertEquals("REJECTED", validator.validate(paymentData)); // bankName null

        paymentData.put("bankName", "   ");
        assertEquals("REJECTED", validator.validate(paymentData)); // bankName empty/blank
    }

    @Test
    void testValidateReferenceCodeNullOrEmpty() {
        paymentData.put("bankName", "BCA");
        assertEquals("REJECTED", validator.validate(paymentData)); // referenceCode null

        paymentData.put("referenceCode", "");
        assertEquals("REJECTED", validator.validate(paymentData)); // referenceCode empty
    }
}