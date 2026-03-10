package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"));
    }

    @Test
    void testCreateOrderPost() throws Exception {
        List<Product> products = new ArrayList<>();
        Product dummyProduct = new Product();
        dummyProduct.setProductId("dummy");
        dummyProduct.setProductName("dummy_product");
        dummyProduct.setProductQuantity(1);
        products.add(dummyProduct);

        when(orderService.createOrder(any(Order.class))).thenReturn(new Order("1", products, 1L, "Author"));
        
        mockMvc.perform(post("/order/create")
                .param("author", "Test Author"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("history"));
                
        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    @Test
    void testOrderHistoryPost() throws Exception {
        List<Order> orders = new ArrayList<>();
        when(orderService.findAllByAuthor("Test Author")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                .param("author", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("orderList"));
                
        verify(orderService, times(1)).findAllByAuthor("Test Author");
    }

    @Test
    void testPayOrderPage() throws Exception {
        List<Product> products = new ArrayList<>();
        Product dummyProduct = new Product();
        dummyProduct.setProductId("dummy");
        dummyProduct.setProductName("dummy_product");
        dummyProduct.setProductQuantity(1);
        products.add(dummyProduct);
        Order order = new Order("1", products, 1L, "Author");
        when(orderService.findById("1")).thenReturn(order);

        mockMvc.perform(get("/order/pay/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("payOrder"));
                
        verify(orderService, times(1)).findById("1");
    }

    @Test
    void testPayOrderPost() throws Exception {
        List<Product> products = new ArrayList<>();
        Product dummyProduct = new Product();
        dummyProduct.setProductId("dummy");
        dummyProduct.setProductName("dummy_product");
        dummyProduct.setProductQuantity(1);
        products.add(dummyProduct);
        when(orderService.updateStatus("1", "SUCCESS")).thenReturn(new Order("1", products, 1L, "Author"));

        mockMvc.perform(post("/order/pay/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
                
        verify(orderService, times(1)).updateStatus("1", "SUCCESS");
    }
}
