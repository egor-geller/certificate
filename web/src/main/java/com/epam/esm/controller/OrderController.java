package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.controller.hateoas.model.HateoasModel;
import com.epam.esm.controller.hateoas.model.ListHateoasModel;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SavedOrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.SavedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class containing public REST API endpoints related to {@link Order} entity.
 *
 * @author Egor Geller
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final SavedOrderService savedOrderService;
    private final PaginationContext paginationContext;
    private final HateoasProvider<OrderDto> modelHateoasProvider;
    private final ListHateoasProvider<OrderDto> listHateoasProvider;

    /**
     * Instantiates a new Order controller.
     *
     * @param orderService         {@link OrderService} order service
     * @param paginationContext    {@link PaginationContext} pagination context
     * @param modelHateoasProvider {@link HateoasProvider} model hateoas provider
     * @param listHateoasProvider  {@link ListHateoasProvider} list hateoas provider
     */
    @Autowired
    public OrderController(OrderService orderService,
                           SavedOrderService savedOrderService,
                           PaginationContext paginationContext,
                           HateoasProvider<OrderDto> modelHateoasProvider,
                           ListHateoasProvider<OrderDto> listHateoasProvider) {
        this.orderService = orderService;
        this.savedOrderService = savedOrderService;
        this.paginationContext = paginationContext;
        this.modelHateoasProvider = modelHateoasProvider;
        this.listHateoasProvider = listHateoasProvider;
    }

    /**
     * Gets all orders.
     *
     * @param page     {@code Integer} current page, not required, by default 1
     * @param pageSize {@code Integer} number of objects to display in a page, not required, by default 10
     * @return JSON {@link ResponseEntity} object that contains list {@link ListHateoasModel} of {@link OrderDto}
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ListHateoasModel<OrderDto>> getAllOrders(@RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) Integer pageSize) {
        List<OrderDto> orderDtoList = orderService.findAllOrdersService(paginationContext.createPagination(page, pageSize));
        Long count = orderService.count();
        List<SavedOrderDto> savedOrderServiceAll = savedOrderService.findAll(paginationContext.createPagination(page, pageSize));
        List<OrderDto> orderDtos = getOrdersWithCorrectPrice(orderDtoList, savedOrderServiceAll);
        return createListPagination(orderDtos, count);
    }

    /**
     * Gets order by id.
     *
     * @param id id of the {@link Order} entity
     * @return JSON {@link ResponseEntity} with {@link HateoasModel} object that contains {@link OrderDto} object
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<HateoasModel<OrderDto>> getOrderById(@PathVariable("id") Long id) {
        OrderDto orderDto = orderService.findOrderByIdService(id);
        List<SavedOrderDto> savedOrderDtos = savedOrderService.findByOrderId(id);
        List<Certificate> collect = savedOrderDtos
                .stream()
                .map(SavedOrderDto::getCertificate)
                .collect(Collectors.toList());
        orderDto.setCertificateList(collect);
        return createModelPagination(orderDto, HttpStatus.OK);
    }

    /**
     * Gets orders of user by its id.
     *
     * @param page     {@code Integer} current page, not required, by default 1
     * @param pageSize {@code Integer} number of objects to display in a page, not required, by default 10
     * @param id       id of the {@link User} entity
     * @return JSON {@link ResponseEntity} object that contains list {@link ListHateoasModel} of {@link OrderDto}
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ListHateoasModel<OrderDto>> getOrdersOfUserByItsId(@RequestParam(required = false) Integer page,
                                                                             @RequestParam(required = false) Integer pageSize,
                                                                             @PathVariable("id") Long id) {
        List<OrderDto> orderDtoList = orderService.findByOrderByUserService(id, paginationContext.createPagination(page, pageSize));
        Long count = orderService.countByUser(id);
        List<SavedOrderDto> savedOrderDtoByUser = savedOrderService.findByUserId(id);
        List<Certificate> certificateList = savedOrderDtoByUser
                .stream()
                .map(SavedOrderDto::getCertificate)
                .collect(Collectors.toList());
        List<OrderDto> newOrderDtos = orderDtoList
                .stream()
                .peek(orderDto -> orderDto.setCertificateList(certificateList))
                .collect(Collectors.toList());
        return createListPagination(newOrderDtos, count);
    }

    /**
     * Create new {@link Order}.
     *
     * @param orderDto {@link OrderDto} instance
     * @return JSON {@link ResponseEntity} object with {@link HateoasModel} that contains created {@link OrderDto} object
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<HateoasModel<OrderDto>> makeOrder(@RequestBody OrderDto orderDto) {
        OrderDto checkout = orderService.makeOrderService(orderDto);
        return createModelPagination(checkout, HttpStatus.CREATED);
    }

    private ResponseEntity<HateoasModel<OrderDto>> createModelPagination(OrderDto orderDto, HttpStatus status) {
        HateoasModel<OrderDto> model = new HateoasModel<>(orderDto);
        HateoasModel<OrderDto> build = model.build(modelHateoasProvider, orderDto, 1L);
        return new ResponseEntity<>(build, status);
    }

    private ResponseEntity<ListHateoasModel<OrderDto>> createListPagination(List<OrderDto> orderDtoList,
                                                                            Long count) {
        ListHateoasModel<OrderDto> model = new ListHateoasModel<>(orderDtoList);
        ListHateoasModel<OrderDto> build = model.build(listHateoasProvider, orderDtoList, count);

        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    private List<OrderDto> getOrdersWithCorrectPrice(List<OrderDto> orderDtoList, List<SavedOrderDto> savedOrderServiceAll) {
        return orderDtoList.stream().map(orderDto -> {
            List<Certificate> certificateList = orderDto.getCertificateList().stream().map(certificate -> {
                long certificateId = certificate.getId();
                Optional<BigDecimal> opPrice = savedOrderServiceAll
                        .stream()
                        .filter(savedOrderDto -> savedOrderDto.getCertificate().getId() == certificateId)
                        .map(SavedOrderDto::getCost)
                        .findFirst();
                if (opPrice.isEmpty()) {
                    return certificate;
                }
                certificate.setPrice(opPrice.get());
                return certificate;
            }).collect(Collectors.toList());
            orderDto.setCertificateList(certificateList);
            return orderDto;
        }).collect(Collectors.toList());
    }
}
