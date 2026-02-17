package com.app.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Address;
import com.app.entites.Cart;
import com.app.entites.Category;
import com.app.entites.Product;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.repositories.AddressRepo;
import com.app.repositories.CategoryRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.RoleRepo;
import com.app.repositories.UserRepo;

@Component
@Transactional
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. Seed Roles
        Role adminRole = new Role();
        adminRole.setRoleId(AppConstants.ADMIN_ID);
        adminRole.setRoleName("ADMIN");

        Role userRole = new Role();
        userRole.setRoleId(AppConstants.USER_ID);
        userRole.setRoleName("USER");

        List<Role> roles = Arrays.asList(adminRole, userRole);
        roles = roleRepo.saveAll(roles);
        adminRole = roles.get(0);
        userRole = roles.get(1);

        // 2. Seed Categories
        if (categoryRepo.count() == 0) {
            Category electronics = new Category();
            electronics.setCategoryName("Electronics");

            Category fashion = new Category();
            fashion.setCategoryName("Fashion");

            Category home = new Category();
            home.setCategoryName("Home & Kitchen");

            Category books = new Category();
            books.setCategoryName("Books");

            Category sports = new Category();
            sports.setCategoryName("Sports");

            categoryRepo.saveAll(Arrays.asList(electronics, fashion, home, books, sports));

            // 3. Seed Products

            // Electronics
            Product laptop = new Product();
            laptop.setProductName("Gaming Laptop");
            laptop.setDescription("High performance gaming laptop with latest GPU");
            laptop.setPrice(1200.00);
            laptop.setDiscount(10.0);
            laptop.setQuantity(50);
            laptop.setSpecialPrice(1080.00);
            laptop.setImage("default.png");
            laptop.setCategory(electronics);

            Product smartphone = new Product();
            smartphone.setProductName("Smartphone X");
            smartphone.setDescription("Latest smartphone with AI camera");
            smartphone.setPrice(800.00);
            smartphone.setDiscount(5.0);
            smartphone.setQuantity(100);
            smartphone.setSpecialPrice(760.00);
            smartphone.setImage("default.png");
            smartphone.setCategory(electronics);

            Product smartwatch = new Product();
            smartwatch.setProductName("Smart Watch Series 5");
            smartwatch.setDescription("Fitness tracker with heart rate monitor");
            smartwatch.setPrice(250.00);
            smartwatch.setDiscount(10.0);
            smartwatch.setQuantity(150);
            smartwatch.setSpecialPrice(225.00);
            smartwatch.setImage("default.png");
            smartwatch.setCategory(electronics);

            Product headphones = new Product();
            headphones.setProductName("Noise Cancelling Headphones");
            headphones.setDescription("Over-ear headphones with active noise cancellation");
            headphones.setPrice(150.00);
            headphones.setDiscount(15.0);
            headphones.setQuantity(80);
            headphones.setSpecialPrice(127.50);
            headphones.setImage("default.png");
            headphones.setCategory(electronics);

            // Fashion
            Product tshirt = new Product();
            tshirt.setProductName("Cotton T-Shirt");
            tshirt.setDescription("100% Cotton, comfortable fit");
            tshirt.setPrice(25.00);
            tshirt.setDiscount(0.0);
            tshirt.setQuantity(200);
            tshirt.setSpecialPrice(25.00);
            tshirt.setImage("default.png");
            tshirt.setCategory(fashion);

            Product jeans = new Product();
            jeans.setProductName("Classic Blue Jeans");
            jeans.setDescription("Slim fit denim jeans");
            jeans.setPrice(60.00);
            jeans.setDiscount(5.0);
            jeans.setQuantity(120);
            jeans.setSpecialPrice(57.00);
            jeans.setImage("default.png");
            jeans.setCategory(fashion);

            Product sneakers = new Product();
            sneakers.setProductName("Running Sneakers");
            sneakers.setDescription("Lightweight running shoes for daily use");
            sneakers.setPrice(85.00);
            sneakers.setDiscount(10.0);
            sneakers.setQuantity(90);
            sneakers.setSpecialPrice(76.50);
            sneakers.setImage("default.png");
            sneakers.setCategory(fashion);

            // Home & Kitchen
            Product blender = new Product();
            blender.setProductName("High Speed Blender");
            blender.setDescription("Perfect for smoothies and soups");
            blender.setPrice(100.00);
            blender.setDiscount(20.0);
            blender.setQuantity(60);
            blender.setSpecialPrice(80.00);
            blender.setImage("default.png");
            blender.setCategory(home);

            Product coffeeMaker = new Product();
            coffeeMaker.setProductName("Automatic Coffee Maker");
            coffeeMaker.setDescription("Brew delicious coffee in minutes");
            coffeeMaker.setPrice(75.00);
            coffeeMaker.setDiscount(10.0);
            coffeeMaker.setQuantity(70);
            coffeeMaker.setSpecialPrice(67.50);
            coffeeMaker.setImage("default.png");
            coffeeMaker.setCategory(home);

            // Books
            Product javaBook = new Product();
            javaBook.setProductName("Java Programming Guide");
            javaBook.setDescription("Comprehensive guide to Java 17");
            javaBook.setPrice(45.00);
            javaBook.setDiscount(5.0);
            javaBook.setQuantity(100);
            javaBook.setSpecialPrice(42.75);
            javaBook.setImage("default.png");
            javaBook.setCategory(books);

            Product springBook = new Product();
            springBook.setProductName("Spring Boot in Action");
            springBook.setDescription("Master Spring Boot framework");
            springBook.setPrice(50.00);
            springBook.setDiscount(10.0);
            springBook.setQuantity(80);
            springBook.setSpecialPrice(45.00);
            springBook.setImage("default.png");
            springBook.setCategory(books);

            // Sports
            Product football = new Product();
            football.setProductName("Professional Football");
            football.setDescription("Standard size 5 football for matches");
            football.setPrice(30.00);
            football.setDiscount(0.0);
            football.setQuantity(150);
            football.setSpecialPrice(30.00);
            football.setImage("default.png");
            football.setCategory(sports);

            Product yogaMat = new Product();
            yogaMat.setProductName("Non-Slip Yoga Mat");
            yogaMat.setDescription("Eco-friendly yoga mat with strap");
            yogaMat.setPrice(20.00);
            yogaMat.setDiscount(5.0);
            yogaMat.setQuantity(200);
            yogaMat.setSpecialPrice(19.00);
            yogaMat.setImage("default.png");
            yogaMat.setCategory(sports);

            productRepo.saveAll(Arrays.asList(
                    laptop, smartphone, smartwatch, headphones,
                    tshirt, jeans, sneakers,
                    blender, coffeeMaker,
                    javaBook, springBook,
                    football, yogaMat));
        }

        // 4. Seed Users
        if (userRepo.count() == 0) {
            // Admin User
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("AdminUser"); // Min 5 chars
            admin.setMobileNumber("1234567890");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(adminRole);

            Address adminAddress = new Address("USA", "NY", "New York", "100001", "5th Ave", "Admin Tower");
            admin.setAddresses(List.of(adminAddress));

            Cart adminCart = new Cart();
            adminCart.setUser(admin);
            admin.setCart(adminCart);

            userRepo.save(admin);

            // Regular User
            User user = new User();
            user.setFirstName("JohnDoe"); // Min 5 chars
            user.setLastName("JohnDoe"); // Min 5 chars
            user.setMobileNumber("0987654321");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.getRoles().add(userRole);

            Address userAddress = new Address("Canada", "ON", "Toronto", "M5V 2H1", "King St", "Condo A");
            user.setAddresses(List.of(userAddress));

            Cart userCart = new Cart();
            userCart.setUser(user);
            user.setCart(userCart);

            userRepo.save(user);
        }

        System.out.println("Data seeding completed successfully from DataSeeder.");
    }
}
