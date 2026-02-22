package com.app.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.app.entites.Bank;
import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Discount;
import com.app.entites.DiscountType;
import com.app.entites.Order;
import com.app.entites.Payment;
import com.app.entites.PaymentBank;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.OrderDTO;
import com.app.repositories.BankRepo;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.DiscountRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentBankRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.UserRepo;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private PaymentBankRepo paymentBankRepo;

    @Mock
    private BankRepo bankRepo;

    @Mock
    private DiscountRepo discountRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private CartItemRepo cartItemRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private static final String EMAIL = "user@test.com";
    private static final Long CART_ID = 1L;

    private Cart buildCart(
        double originalPrice,
        double specialPrice,
        int quantity
    ) {
        Product product = new Product();
        product.setProductId(10L);
        product.setPrice(originalPrice);
        product.setSpecialPrice(specialPrice);
        product.setQuantity(quantity + 5);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setProductPrice(specialPrice);
        cartItem.setDiscount(0.0);

        Cart cart = new Cart();
        cart.setCartId(CART_ID);
        cart.setTotalPrice(specialPrice * quantity);
        cart.setCartItems(List.of(cartItem));

        return cart;
    }

    private Discount buildDiscount(DiscountType type, long value) {
        Discount d = new Discount();
        d.setCode("PROMO");
        d.setName("Promo");
        d.setValue(value);
        d.setDiscountType(type);
        return d;
    }

    private void stubSaves(Order savedOrder) {
        Payment savedPayment = new Payment();
        savedPayment.setPaymentId(1L);

        when(paymentRepo.save(any(Payment.class))).thenReturn(savedPayment);
        when(orderRepo.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepo.saveAll(any())).thenReturn(List.of());
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenAnswer(
            inv -> {
                Order o = inv.getArgument(0);
                OrderDTO dto = new OrderDTO();
                dto.setTotalAmount(o.getTotalAmount());
                dto.setEmail(o.getEmail());
                return dto;
            }
        );
    }

    @Test
    void placeOrder_withPercentageDiscountCode_appliesDiscountOnRawTotal() {
        Cart cart = buildCart(100.0, 90.0, 2);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );
        when(discountRepo.findByCode("PROMO")).thenReturn(
            Optional.of(buildDiscount(DiscountType.PERCENTAGE, 10))
        );

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(
            Order.class
        );
        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "COD", null, "PROMO");

        verify(orderRepo).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalAmount()).isEqualTo(180.0);
    }

    @Test
    void placeOrder_withPercentageDiscountCode_doesNotUseSpecialPrice() {
        Cart cart = buildCart(100.0, 90.0, 2);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );
        when(discountRepo.findByCode("PROMO")).thenReturn(
            Optional.of(buildDiscount(DiscountType.PERCENTAGE, 50))
        );

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(
            Order.class
        );
        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "COD", null, "PROMO");

        verify(orderRepo).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalAmount())
            .isEqualTo(100.0)
            .isNotEqualTo(90.0);
    }

    @Test
    void placeOrder_withValueDiscountCode_subtractsFixedAmountFromRawTotal() {
        Cart cart = buildCart(100.0, 90.0, 2);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );
        when(discountRepo.findByCode("PROMO")).thenReturn(
            Optional.of(buildDiscount(DiscountType.VALUE, 30))
        );

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(
            Order.class
        );
        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "COD", null, "PROMO");

        verify(orderRepo).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalAmount()).isEqualTo(170.0);
    }

    @Test
    void placeOrder_withValueDiscountLargerThanTotal_clampsTotalToZero() {
        Cart cart = buildCart(100.0, 90.0, 1);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );
        when(discountRepo.findByCode("PROMO")).thenReturn(
            Optional.of(buildDiscount(DiscountType.VALUE, 999))
        );

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(
            Order.class
        );
        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "COD", null, "PROMO");

        verify(orderRepo).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalAmount()).isEqualTo(0.0);
    }

    @Test
    void placeOrder_withInvalidDiscountCode_throwsAPIException() {
        Cart cart = buildCart(100.0, 90.0, 1);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );
        when(discountRepo.findByCode("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            orderService.placeOrder(EMAIL, CART_ID, "COD", null, "INVALID")
        )
            .isInstanceOf(APIException.class)
            .hasMessage("Invalid discount code: INVALID");
    }

    @Test
    void placeOrder_withNoDiscountCode_usesCartSpecialPriceTotal() {
        Cart cart = buildCart(100.0, 90.0, 2);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(
            Order.class
        );
        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "COD", null, null);

        verify(orderRepo).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalAmount()).isEqualTo(180.0);
        verifyNoInteractions(discountRepo);
    }

    @Test
    void placeOrder_withBankTransferAndValidBankId_savesPaymentBank() {
        Cart cart = buildCart(100.0, 90.0, 1);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );

        Bank bank = new Bank(2L, "BCA", "014", 2500L);
        when(bankRepo.findById(2L)).thenReturn(Optional.of(bank));

        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "BANK_TRANSFER", 2L, null);

        ArgumentCaptor<PaymentBank> captor = ArgumentCaptor.forClass(
            PaymentBank.class
        );
        verify(paymentBankRepo).save(captor.capture());
        assertThat(captor.getValue().getBank()).isEqualTo(bank);
    }

    @Test
    void placeOrder_withBankTransferAndInvalidBankId_throwsResourceNotFoundException() {
        Cart cart = buildCart(100.0, 90.0, 1);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );
        when(bankRepo.findById(99L)).thenReturn(Optional.empty());

        Payment savedPayment = new Payment();
        when(paymentRepo.save(any(Payment.class))).thenReturn(savedPayment);
        when(orderRepo.save(any(Order.class))).thenReturn(new Order());

        assertThatThrownBy(() ->
            orderService.placeOrder(EMAIL, CART_ID, "BANK_TRANSFER", 99L, null)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(paymentBankRepo, never()).save(any());
    }

    @Test
    void placeOrder_withNonBankPayment_doesNotSavePaymentBank() {
        Cart cart = buildCart(100.0, 90.0, 1);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );

        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "COD", null, null);

        verifyNoInteractions(bankRepo);
        verify(paymentBankRepo, never()).save(any());
    }

    @Test
    void placeOrder_withBankTransferAndPromoCode_appliesPromoBankTransfer() {
        Cart cart = buildCart(100.0, 90.0, 2);
        when(cartRepo.findCartByEmailAndCartId(EMAIL, CART_ID)).thenReturn(
            cart
        );
        when(discountRepo.findByCode("PROMO")).thenReturn(
            Optional.of(buildDiscount(DiscountType.PERCENTAGE, 20))
        );

        Bank bank = new Bank(1L, "Mandiri", "008", 3000L);
        when(bankRepo.findById(1L)).thenReturn(Optional.of(bank));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(
            Order.class
        );
        Order savedOrder = new Order();
        stubSaves(savedOrder);

        orderService.placeOrder(EMAIL, CART_ID, "BANK_TRANSFER", 1L, "PROMO");

        verify(orderRepo).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalAmount()).isEqualTo(160.0);
        verify(paymentBankRepo).save(any(PaymentBank.class));
    }
}
