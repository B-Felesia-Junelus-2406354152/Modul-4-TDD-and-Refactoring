package id.ac.ui.cs.advprog.eshop.service.validator;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VoucherCodeValidator implements PaymentValidator {
    @Override
    public boolean supports(String method) {
        return "VOUCHER_CODE".equals(method);
    }

    @Override
    public String validate(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
            long digitCount = voucherCode.chars().filter(Character::isDigit).count();
            if (digitCount == 8) {
                return PaymentStatus.SUCCESS.getValue();
            }
        }
        return PaymentStatus.REJECTED.getValue();
    }
}
