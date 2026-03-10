package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testPaymentDetailPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"));
    }

    @Test
    void testViewPaymentDetail() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("sessionId", "1234");
        Payment payment = new Payment("pay-1", "CASH", paymentData);
        when(paymentService.getPayment("pay-1")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/pay-1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payment"))
                .andExpect(view().name("paymentDetail"));
                
        verify(paymentService, times(1)).getPayment("pay-1");
    }

    @Test
    void testAdminListPayments() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("sessionId", "1234");
        Payment payment = new Payment("pay-1", "CASH", paymentData);
        List<Payment> payments = Arrays.asList(payment);
        
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payments"))
                .andExpect(view().name("paymentAdminList"));
                
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testAdminPaymentDetail() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("sessionId", "1234");
        Payment payment = new Payment("pay-1", "CASH", paymentData);
        when(paymentService.getPayment("pay-1")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/pay-1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payment"))
                .andExpect(view().name("paymentAdminDetail"));
                
        verify(paymentService, times(1)).getPayment("pay-1");
    }

    @Test
    void testAdminSetPaymentStatus() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("sessionId", "1234");
        Payment payment = new Payment("pay-1", "CASH", paymentData);
        when(paymentService.getPayment("pay-1")).thenReturn(payment);
        when(paymentService.setStatus(any(Payment.class), anyString())).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/pay-1")
                .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
                
        verify(paymentService, times(1)).getPayment("pay-1");
        verify(paymentService, times(1)).setStatus(any(Payment.class), eq("SUCCESS"));
    }
}
