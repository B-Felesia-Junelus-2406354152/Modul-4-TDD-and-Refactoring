package id.ac.ui.cs.advprog.eshop.service.validator;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BankTransferValidator implements PaymentValidator {
    @Override
    public boolean supports(String method) {
        return "BANK_TRANSFER".equals(method);
    }

    @Override
    public String validate(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String refCode = paymentData.get("referenceCode");

        if (bankName != null && !bankName.trim().isEmpty() &&
                refCode != null && !refCode.trim().isEmpty()) {
            return PaymentStatus.SUCCESS.getValue();
        }
        return PaymentStatus.REJECTED.getValue();
    }
}
