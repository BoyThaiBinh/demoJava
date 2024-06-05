package com.example.demo.Service;

import com.example.demo.Model.CartItem;
import com.example.demo.Model.Order;
import com.example.demo.Model.OrderDetail;
import com.example.demo.Repository.OrderDetailRepository;
import com.example.demo.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService

//    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, CartService cartService) {
//        this.orderRepository = orderRepository;
//        this.orderDetailRepository = orderDetailRepository;
//        this.cartService = cartService;
//    }

    @Transactional
    public Order createOrder(Order createOrder, List<CartItem> cartItems) {
//        Order order = new Order();
//        order.setCustomerName(createOrder.getCustomerName());
        orderRepository.save(createOrder);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(createOrder);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        // Optionally clear the cart after order placement
        cartService.clearCart();
        return createOrder;
    }
}