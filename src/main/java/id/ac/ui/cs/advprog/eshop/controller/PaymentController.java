package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/detail")
    public String paymentDetailPage(Model model) {
        return "paymentDetail"; // will add this in pt 6
    }

    @GetMapping("/detail/{paymentId}")
    public String viewPaymentDetail(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentDetail"; 
    }

    @GetMapping("/admin/list")
    public String adminListPayments(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "paymentAdminList"; 
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String adminPaymentDetail(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String adminSetPaymentStatus(@PathVariable("paymentId") String paymentId, 
                                        @RequestParam("status") String status) {
        Payment payment = paymentService.getPayment(paymentId);
        paymentService.setStatus(payment, status);
        return "redirect:/payment/admin/list";
    }
}
