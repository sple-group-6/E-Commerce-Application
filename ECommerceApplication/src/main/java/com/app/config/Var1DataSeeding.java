package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.app.entites.Category;
import com.app.entites.Membership;
import com.app.entites.Product;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.payloads.AddressDTO;
import com.app.payloads.ProductDTO;
import com.app.payloads.SellerDTO;
import com.app.payloads.SellerRequestDTO;
import com.app.payloads.UserDTO;
import com.app.payloads.ReviewRequestDTO;
import com.app.repositories.MembershipRepo;
import com.app.repositories.RoleRepo;
import com.app.repositories.UserRepo;
import com.app.services.CartService;
import com.app.services.CategoryService;
import com.app.services.OrderService;
import com.app.services.ProductService;
import com.app.services.ReviewService;
import com.app.services.SellerService;
import com.app.services.UserService;
import com.app.services.WishlistService;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Var1DataSeeding {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private MembershipRepo membershipRepo;

	@Bean
	@Order(2)
	public ApplicationRunner dataSeeder() {
		return args -> {
			seedCategories();
			seedUsers();
			seedProducts();
			seedSellers();
			seedMemberships();
			seedCartsAndOrders();
			seedWishlists();
			seedReviews();
			System.out.println("Data seeding completed.");
		};
	}

	private void seedCategories() {
		try {
			Category electronics = new Category();
			electronics.setCategoryName("Electronics");
			categoryService.createCategory(electronics);
		} catch (Exception e) {
			System.out.println("Category 'Electronics' already exists, skipping.");
		}

		try {
			Category clothing = new Category();
			clothing.setCategoryName("Clothing");
			categoryService.createCategory(clothing);
		} catch (Exception e) {
			System.out.println("Category 'Clothing' already exists, skipping.");
		}

		try {
			Category books = new Category();
			books.setCategoryName("Books");
			categoryService.createCategory(books);
		} catch (Exception e) {
			System.out.println("Category 'Books' already exists, skipping.");
		}
	}

	private void seedProducts() {
		try {
			Product laptop = new Product();
			laptop.setProductName("Laptop");
			laptop.setDescription("High performance laptop");
			laptop.setQuantity(10);
			laptop.setPrice(1000.00);
			laptop.setDiscount(10.0);
			laptop.setImage("default.png");
			productService.addProduct(1L, laptop);
		} catch (Exception e) {
			System.out.println("Product 'Laptop' already exists, skipping.");
		}

		try {
			Product phone = new Product();
			phone.setProductName("Smartphone");
			phone.setDescription("Latest model smartphone");
			phone.setQuantity(25);
			phone.setPrice(500.00);
			phone.setDiscount(5.0);
			phone.setImage("default.png");
			productService.addProduct(1L, phone);
		} catch (Exception e) {
			System.out.println("Product 'Smartphone' already exists, skipping.");
		}

		try {
			Product tshirt = new Product();
			tshirt.setProductName("T-Shirt");
			tshirt.setDescription("Cotton casual t-shirt");
			tshirt.setQuantity(50);
			tshirt.setPrice(25.00);
			tshirt.setDiscount(15.0);
			tshirt.setImage("default.png");
			productService.addProduct(2L, tshirt);
		} catch (Exception e) {
			System.out.println("Product 'T-Shirt' already exists, skipping.");
		}

		try {
			Product jeans = new Product();
			jeans.setProductName("Jeans");
			jeans.setDescription("Slim fit denim jeans");
			jeans.setQuantity(30);
			jeans.setPrice(45.00);
			jeans.setDiscount(10.0);
			jeans.setImage("default.png");
			productService.addProduct(2L, jeans);
		} catch (Exception e) {
			System.out.println("Product 'Jeans' already exists, skipping.");
		}

		try {
			Product novel = new Product();
			novel.setProductName("Novel");
			novel.setDescription("Bestselling fiction novel");
			novel.setQuantity(100);
			novel.setPrice(15.00);
			novel.setDiscount(20.0);
			novel.setImage("default.png");
			productService.addProduct(3L, novel);
		} catch (Exception e) {
			System.out.println("Product 'Novel' already exists, skipping.");
		}

		try {
			Product headphones = new Product();
			headphones.setProductName("Wireless Headphones");
			headphones.setDescription("Noise-cancelling wireless headphones");
			headphones.setQuantity(20);
			headphones.setPrice(150.00);
			headphones.setDiscount(8.0);
			headphones.setImage("default.png");
			productService.addProduct(1L, headphones);
		} catch (Exception e) {
			System.out.println("Product 'Wireless Headphones' already exists, skipping.");
		}

		try {
			Product smartwatch = new Product();
			smartwatch.setProductName("Smartwatch");
			smartwatch.setDescription("Fitness tracking smartwatch");
			smartwatch.setQuantity(15);
			smartwatch.setPrice(200.00);
			smartwatch.setDiscount(12.0);
			smartwatch.setImage("default.png");
			productService.addProduct(1L, smartwatch);
		} catch (Exception e) {
			System.out.println("Product 'Smartwatch' already exists, skipping.");
		}

		try {
			Product sneakers = new Product();
			sneakers.setProductName("Sneakers");
			sneakers.setDescription("Lightweight sports sneakers");
			sneakers.setQuantity(40);
			sneakers.setPrice(80.00);
			sneakers.setDiscount(10.0);
			sneakers.setImage("default.png");
			productService.addProduct(2L, sneakers);
		} catch (Exception e) {
			System.out.println("Product 'Sneakers' already exists, skipping.");
		}

		try {
			Product jacket = new Product();
			jacket.setProductName("Winter Jacket");
			jacket.setDescription("Warm insulated winter jacket");
			jacket.setQuantity(25);
			jacket.setPrice(120.00);
			jacket.setDiscount(15.0);
			jacket.setImage("default.png");
			productService.addProduct(2L, jacket);
		} catch (Exception e) {
			System.out.println("Product 'Winter Jacket' already exists, skipping.");
		}

		try {
			Product textbook = new Product();
			textbook.setProductName("Science Textbook");
			textbook.setDescription("Comprehensive science textbook");
			textbook.setQuantity(60);
			textbook.setPrice(30.00);
			textbook.setDiscount(5.0);
			textbook.setImage("default.png");
			productService.addProduct(3L, textbook);
		} catch (Exception e) {
			System.out.println("Product 'Science Textbook' already exists, skipping.");
		}
	}

	private void seedUsers() {
		AddressDTO address = new AddressDTO();
		address.setStreet("Main Street");
		address.setBuildingName("Building One");
		address.setCity("Jakarta");
		address.setState("DKI Jakarta");
		address.setCountry("Indonesia");
		address.setPincode("123456");

		try {
			UserDTO admin = new UserDTO();
			admin.setFirstName("Admin");
			admin.setLastName("Account");
			admin.setMobileNumber("1234567890");
			admin.setEmail("admin@mail.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setAddress(address);
			UserDTO registeredAdmin = userService.registerUser(admin);

			Role adminRole = roleRepo.findById(AppConstants.ADMIN_ID).get();
			User adminUser = userRepo.findById(registeredAdmin.getUserId()).get();
			adminUser.getRoles().add(adminRole);
			userRepo.save(adminUser);
		} catch (Exception e) {
			System.out.println("User 'admin@mail.com' already exists, skipping.");
		}

		try {
			UserDTO user = new UserDTO();
			user.setFirstName("Regular");
			user.setLastName("Account");
			user.setMobileNumber("0987654321");
			user.setEmail("user@mail.com");
			user.setPassword(passwordEncoder.encode("user1234"));
			user.setAddress(address);
			userService.registerUser(user);
		} catch (Exception e) {
			System.out.println("User 'user@mail.com' already exists, skipping.");
		}

		try {
			UserDTO seller = new UserDTO();
			seller.setFirstName("Seller");
			seller.setLastName("Account");
			seller.setMobileNumber("1122334455");
			seller.setEmail("seller@mail.com");
			seller.setPassword(passwordEncoder.encode("seller123"));
			seller.setAddress(address);
			userService.registerUser(seller);
		} catch (Exception e) {
			System.out.println("User 'seller@mail.com' already exists, skipping.");
		}
	}

	private void seedMemberships() {
		try {
			Membership gold = new Membership();
			gold.setMembershipCode("GOLD2024");
			gold.setDiscountPercentage(20.0);
			membershipRepo.save(gold);
		} catch (Exception e) {
			System.out.println("Membership 'GOLD2024' already exists, skipping.");
		}

		try {
			Membership silver = new Membership();
			silver.setMembershipCode("SILVER2024");
			silver.setDiscountPercentage(10.0);
			membershipRepo.save(silver);
		} catch (Exception e) {
			System.out.println("Membership 'SILVER2024' already exists, skipping.");
		}
	}

	private void seedCartsAndOrders() {
		try {
			User adminUser = userRepo.findByEmail("admin@mail.com")
					.orElseThrow(() -> new RuntimeException("Admin user not found"));
			Long adminCartId = adminUser.getCart().getCartId();

			ProductDTO laptopResult = productService.searchProductByKeyword("Laptop", 0, 1, "productId", "asc")
					.getContent().get(0);
			ProductDTO tshirtResult = productService.searchProductByKeyword("T-Shirt", 0, 1, "productId", "asc")
					.getContent().get(0);

			cartService.addProductToCart(adminCartId, laptopResult.getProductId(), 1);
			cartService.addProductToCart(adminCartId, tshirtResult.getProductId(), 2);

			orderService.placeOrder("admin@mail.com", adminCartId, "Credit Card", null);
		} catch (Exception e) {
			System.out.println("Admin cart/order seeding skipped: " + e.getMessage());
		}

		try {
			User regularUser = userRepo.findByEmail("user@mail.com")
					.orElseThrow(() -> new RuntimeException("Regular user not found"));
			Long userCartId = regularUser.getCart().getCartId();

			ProductDTO phoneResult = productService.searchProductByKeyword("Smartphone", 0, 1, "productId", "asc")
					.getContent().get(0);
			ProductDTO novelResult = productService.searchProductByKeyword("Novel", 0, 1, "productId", "asc")
					.getContent().get(0);
			ProductDTO jeansResult = productService.searchProductByKeyword("Jeans", 0, 1, "productId", "asc")
					.getContent().get(0);

			cartService.addProductToCart(userCartId, phoneResult.getProductId(), 1);

			AddressDTO codAddress = new AddressDTO();
			codAddress.setStreet("Jalan Sudirman");
			codAddress.setBuildingName("Gedung Utama");
			codAddress.setCity("Jakarta");
			codAddress.setState("DKI Jakarta");
			codAddress.setCountry("Indonesia");
			codAddress.setPincode("102340");

			orderService.placeOrderWithCOD("user@mail.com", userCartId, codAddress, null);

			cartService.addProductToCart(userCartId, novelResult.getProductId(), 3);
			cartService.addProductToCart(userCartId, jeansResult.getProductId(), 1);
		} catch (Exception e) {
			System.out.println("User cart/order seeding skipped: " + e.getMessage());
		}

		try {
			User adminUser = userRepo.findByEmail("admin@mail.com")
					.orElseThrow(() -> new RuntimeException("Admin user not found"));
			Long adminCartId = adminUser.getCart().getCartId();

			ProductDTO jeansResult = productService.searchProductByKeyword("Jeans", 0, 1, "productId", "asc")
					.getContent().get(0);

			cartService.addProductToCart(adminCartId, jeansResult.getProductId(), 2);

			orderService.placeOrder("admin@mail.com", adminCartId, "Credit Card", "GOLD2024");
		} catch (Exception e) {
			System.out.println("Membership order seeding skipped: " + e.getMessage());
		}
	}

	private void seedReviews() {
		try {
			ProductDTO laptopResult = productService.searchProductByKeyword("Laptop", 0, 1, "productId", "asc")
					.getContent().get(0);
			ReviewRequestDTO review = new ReviewRequestDTO();
			review.setStars(5);
			review.setReviewText("Excellent laptop, very fast and reliable!");
			reviewService.addReview("admin@mail.com", laptopResult.getProductId(), review);
		} catch (Exception e) {
			System.out.println("Admin Laptop review seeding skipped: " + e.getMessage());
		}

		try {
			ProductDTO jeansResult = productService.searchProductByKeyword("Jeans", 0, 1, "productId", "asc")
					.getContent().get(0);
			ReviewRequestDTO review = new ReviewRequestDTO();
			review.setStars(4);
			review.setReviewText("Great fit and comfortable denim jeans.");
			reviewService.addReview("admin@mail.com", jeansResult.getProductId(), review);
		} catch (Exception e) {
			System.out.println("Admin Jeans review seeding skipped: " + e.getMessage());
		}

		try {
			ProductDTO phoneResult = productService.searchProductByKeyword("Smartphone", 0, 1, "productId", "asc")
					.getContent().get(0);
			ReviewRequestDTO review = new ReviewRequestDTO();
			review.setStars(4);
			review.setReviewText("Good smartphone, smooth performance and great camera.");
			reviewService.addReview("user@mail.com", phoneResult.getProductId(), review);
		} catch (Exception e) {
			System.out.println("User Smartphone review seeding skipped: " + e.getMessage());
		}
	}

	private void seedSellers() {
		User sellerUser = userRepo.findByEmail("seller@mail.com")
				.orElseThrow(() -> new RuntimeException("Seller user not found"));
		Long sellerUserId = sellerUser.getUserId();

		try {
			SellerRequestDTO techStoreRequest = new SellerRequestDTO();
			techStoreRequest.setName("TechStore");
			techStoreRequest.setDescription("Electronics and gadgets retailer");
			techStoreRequest.setOwnerUserId(sellerUserId);
			SellerDTO techStore = sellerService.createSeller(techStoreRequest);

			ProductDTO headphonesResult = productService
					.searchProductByKeyword("Wireless Headphones", 0, 1, "productId", "asc")
					.getContent().get(0);
			ProductDTO smartwatchResult = productService
					.searchProductByKeyword("Smartwatch", 0, 1, "productId", "asc")
					.getContent().get(0);

			sellerService.assignProduct(techStore.getId(), headphonesResult.getProductId());
			sellerService.assignProduct(techStore.getId(), smartwatchResult.getProductId());
		} catch (Exception e) {
			System.out.println("Seller 'TechStore' seeding skipped: " + e.getMessage());
		}

		try {
			SellerRequestDTO fashionHubRequest = new SellerRequestDTO();
			fashionHubRequest.setName("FashionHub");
			fashionHubRequest.setDescription("Trendy clothing and apparel");
			fashionHubRequest.setOwnerUserId(sellerUserId);
			SellerDTO fashionHub = sellerService.createSeller(fashionHubRequest);

			ProductDTO sneakersResult = productService
					.searchProductByKeyword("Sneakers", 0, 1, "productId", "asc")
					.getContent().get(0);
			ProductDTO jacketResult = productService
					.searchProductByKeyword("Winter Jacket", 0, 1, "productId", "asc")
					.getContent().get(0);

			sellerService.assignProduct(fashionHub.getId(), sneakersResult.getProductId());
			sellerService.assignProduct(fashionHub.getId(), jacketResult.getProductId());
		} catch (Exception e) {
			System.out.println("Seller 'FashionHub' seeding skipped: " + e.getMessage());
		}

		try {
			SellerRequestDTO bookWorldRequest = new SellerRequestDTO();
			bookWorldRequest.setName("BookWorld");
			bookWorldRequest.setDescription("Books and literature store");
			bookWorldRequest.setOwnerUserId(sellerUserId);
			SellerDTO bookWorld = sellerService.createSeller(bookWorldRequest);

			ProductDTO textbookResult = productService
					.searchProductByKeyword("Science Textbook", 0, 1, "productId", "asc")
					.getContent().get(0);

			sellerService.assignProduct(bookWorld.getId(), textbookResult.getProductId());
		} catch (Exception e) {
			System.out.println("Seller 'BookWorld' seeding skipped: " + e.getMessage());
		}
	}

	private void seedWishlists() {
		try {
			User regularUser = userRepo.findByEmail("user@mail.com")
					.orElseThrow(() -> new RuntimeException("Regular user not found"));
			Long wishlistId = regularUser.getWishlist().getWishlistId();

			ProductDTO laptopResult = productService
					.searchProductByKeyword("Laptop", 0, 1, "productId", "asc")
					.getContent().get(0);

			wishlistService.addProductToWishlist(wishlistId, laptopResult.getProductId());
		} catch (Exception e) {
			System.out.println("Wishlist seeding skipped: " + e.getMessage());
		}

		try {
			User adminUser = userRepo.findByEmail("admin@mail.com")
					.orElseThrow(() -> new RuntimeException("Admin user not found"));
			Long wishlistId = adminUser.getWishlist().getWishlistId();

			ProductDTO novelResult = productService
					.searchProductByKeyword("Novel", 0, 1, "productId", "asc")
					.getContent().get(0);
			ProductDTO phoneResult = productService
					.searchProductByKeyword("Smartphone", 0, 1, "productId", "asc")
					.getContent().get(0);

			wishlistService.addProductToWishlist(wishlistId, novelResult.getProductId());
			wishlistService.addProductToWishlist(wishlistId, phoneResult.getProductId());
		} catch (Exception e) {
			System.out.println("Admin wishlist seeding skipped: " + e.getMessage());
		}
	}
}
