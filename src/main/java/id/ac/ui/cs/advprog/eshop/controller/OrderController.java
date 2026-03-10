package id.ac.ui.cs.advprog.eshop.controller;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author, Model model) {
        List<Product> products = new ArrayList<>();
        Product dummyProduct = new Product();
        dummyProduct.setProductId("dummy");
        dummyProduct.setProductName("dummy_product");
        dummyProduct.setProductQuantity(1);
        products.add(dummyProduct);
        Order order = new Order(String.valueOf(System.currentTimeMillis()), products, System.currentTimeMillis(), author);
        service.createOrder(order);
        return "redirect:history"; 
    }

    @GetMapping("/history")
    public String orderHistoryPage(Model model) {
        return "orderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = service.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "orderList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable("orderId") String orderId, Model model) {
        Order order = service.findById(orderId);
        model.addAttribute("order", order);
        return "payOrder"; 
    }
    
    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable("orderId") String orderId, Model model) {
        service.updateStatus(orderId, "SUCCESS");
        return "redirect:/order/history"; 
    }
}