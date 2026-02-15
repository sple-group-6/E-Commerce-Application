package com.app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Payment;
import com.app.entites.Product;
import com.app.entites.Address;
import com.app.entites.Membership;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.AddressDTO;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderItemDTO;
import com.app.payloads.OrderResponse;
import com.app.repositories.AddressRepo;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.MembershipRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	public UserRepo userRepo;

	@Autowired
	public CartRepo cartRepo;

	@Autowired
	public OrderRepo orderRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	public OrderItemRepo orderItemRepo;

	@Autowired
	public CartItemRepo cartItemRepo;

	@Autowired
	public UserService userService;

	@Autowired
	public CartService cartService;

	@Autowired
	public AddressRepo addressRepo;

	@Autowired
	public MembershipRepo membershipRepo;

	@Autowired
	public ModelMapper modelMapper;

	private Membership validateMembershipCode(String membershipCode) {
		if (membershipCode == null || membershipCode.isBlank()) {
			return null;
		}
		return membershipRepo.findByMembershipCode(membershipCode)
				.orElseThrow(() -> new APIException("Invalid membership code: " + membershipCode));
	}

	@Override
	public OrderDTO placeOrder(String email, Long cartId, String paymentMethod, String membershipCode) {

		Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}

		Membership membership = validateMembershipCode(membershipCode);

		Order order = new Order();

		order.setEmail(email);
		order.setOrderDate(LocalDate.now());
		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);

		payment = paymentRepo.save(payment);

		order.setPayment(payment);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();
		double totalAmount = 0.0;

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());

			if (membership != null) {
				double originalPrice = cartItem.getProduct().getPrice();
				double memberDiscountedPrice = originalPrice - (originalPrice * membership.getDiscountPercentage() / 100.0);
				orderItem.setOrderedProductPrice(memberDiscountedPrice);
				orderItem.setDiscount(membership.getDiscountPercentage());
			} else {
				orderItem.setOrderedProductPrice(cartItem.getProductPrice());
				orderItem.setDiscount(cartItem.getDiscount());
			}

			totalAmount += orderItem.getOrderedProductPrice() * orderItem.getQuantity();
			orderItem.setOrder(order);
			orderItems.add(orderItem);
		}

		order.setTotalAmount(totalAmount);
		Order savedOrder = orderRepo.save(order);

		orderItems.forEach(item -> item.setOrder(savedOrder));
		orderItems = orderItemRepo.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
	}

	@Override
	public OrderDTO placeOrderWithCOD(String email, Long cartId, AddressDTO deliveryAddress, String membershipCode) {
		if (deliveryAddress == null) {
			throw new APIException("Delivery address is required for COD payment");
		}

		Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}

		Membership membership = validateMembershipCode(membershipCode);

		Order order = new Order();

		order.setEmail(email);
		order.setOrderDate(LocalDate.now());
		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod("Cash On Delivery");

		payment = paymentRepo.save(payment);

		order.setPayment(payment);

		// Save or reuse delivery address
		Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
				deliveryAddress.getCountry(), deliveryAddress.getState(), deliveryAddress.getCity(),
				deliveryAddress.getPincode(), deliveryAddress.getStreet(), deliveryAddress.getBuildingName());

		if (address == null) {
			address = new Address(deliveryAddress.getCountry(), deliveryAddress.getState(),
					deliveryAddress.getCity(), deliveryAddress.getPincode(),
					deliveryAddress.getStreet(), deliveryAddress.getBuildingName());
			address = addressRepo.save(address);
		}

		order.setDeliveryAddress(address);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();
		double totalAmount = 0.0;

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());

			if (membership != null) {
				double originalPrice = cartItem.getProduct().getPrice();
				double memberDiscountedPrice = originalPrice - (originalPrice * membership.getDiscountPercentage() / 100.0);
				orderItem.setOrderedProductPrice(memberDiscountedPrice);
				orderItem.setDiscount(membership.getDiscountPercentage());
			} else {
				orderItem.setOrderedProductPrice(cartItem.getProductPrice());
				orderItem.setDiscount(cartItem.getDiscount());
			}

			totalAmount += orderItem.getOrderedProductPrice() * orderItem.getQuantity();
			orderItem.setOrder(order);
			orderItems.add(orderItem);
		}

		order.setTotalAmount(totalAmount);
		Order savedOrder = orderRepo.save(order);

		orderItems.forEach(item -> item.setOrder(savedOrder));
		orderItems = orderItemRepo.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
	}

	@Override
	public List<OrderDTO> getOrdersByUser(String email) {
		List<Order> orders = orderRepo.findAllByEmail(email);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the user with email: " + email);
		}

		return orderDTOs;
	}

	@Override
	public OrderDTO getOrder(String email, Long orderId) {

		Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return modelMapper.map(order, OrderDTO.class);
	}

	@Override
	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Order> pageOrders = orderRepo.findAll(pageDetails);

		List<Order> orders = pageOrders.getContent();

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
		
		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the users");
		}

		OrderResponse orderResponse = new OrderResponse();
		
		orderResponse.setContent(orderDTOs);
		orderResponse.setPageNumber(pageOrders.getNumber());
		orderResponse.setPageSize(pageOrders.getSize());
		orderResponse.setTotalElements(pageOrders.getTotalElements());
		orderResponse.setTotalPages(pageOrders.getTotalPages());
		orderResponse.setLastPage(pageOrders.isLast());
		
		return orderResponse;
	}

	@Override
	public OrderDTO updateOrder(String email, Long orderId, String orderStatus) {

		Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		order.setOrderStatus(orderStatus);

		return modelMapper.map(order, OrderDTO.class);
	}

}
