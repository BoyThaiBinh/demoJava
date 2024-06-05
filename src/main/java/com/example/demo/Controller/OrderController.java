package com.example.demo.Controller;

import com.example.demo.Model.CartItem;
import com.example.demo.Model.Order;
import com.example.demo.Model.Product;
import com.example.demo.Service.CartService;
import com.example.demo.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @GetMapping("/checkout")
    public String checkout() {
        return "/carts/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@Valid Order order, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/carts";
        }
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/carts"; // Redirect if cart is empty
        }
        orderService.createOrder(order, cartItems);
        return "redirect:/order/confirmation";
    }
    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "carts/order-confirmation";
    }
}