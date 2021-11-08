package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaginationContext paginationContext;

    @Autowired
    public OrderController(OrderService orderService, PaginationContext paginationContext) {
        this.orderService = orderService;
        this.paginationContext = paginationContext;
    }

    @GetMapping
    public ResponseEntity<Collection<OrderDto>> getAllOrders(@RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) Integer pageSize) {
        List<OrderDto> orderDtoList = orderService.findAllOrdersService(paginationContext.createPagination(page, pageSize));
        List<OrderDto> response = new ArrayList<>();
        orderDtoList.forEach(order -> {
            order.add(linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel());
            response.add(order);
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) {
        OrderDto orderDto = orderService.findOrderByIdService(id);
        if (orderDto != null) {
            orderDto.add(linkTo(
                    methodOn(OrderController.class)
                            .getAllOrders(paginationContext.getStartPage(), paginationContext.getLengthOfContext()))
                    .withSelfRel()
            );
        }
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Collection<OrderDto>> getOrdersOfUserByItsId(@PathVariable("id") Long id) {
        List<OrderDto> orderDtoList = orderService.findByOrderByUserService(id, paginationContext);
        List<OrderDto> response = new ArrayList<>();
        if (orderDtoList != null) {
            orderDtoList.forEach(order -> {
                order.add(linkTo(methodOn(OrderController.class).getOrderById(id)).withSelfRel());
                response.add(order);
            });
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDto> makeOrder(@RequestBody OrderDto orderDto) {
        OrderDto checkout = orderService.makeOrderService(orderDto);
        if (checkout != null) {
            checkout.add(linkTo(
                    methodOn(OrderController.class)
                            .getOrderById(orderDto.getId()))
                    .withSelfRel()
            );
        }
        return new ResponseEntity<>(checkout, HttpStatus.OK);
    }
}
