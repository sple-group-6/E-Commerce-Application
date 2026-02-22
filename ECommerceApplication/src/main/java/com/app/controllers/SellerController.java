package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.ProductDTO;
import com.app.payloads.ProductResponse;
import com.app.payloads.SellerDTO;
import com.app.payloads.SellerRequestDTO;
import com.app.payloads.SellerResponse;
import com.app.services.SellerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/admin/sellers")
    public ResponseEntity<SellerDTO> createSeller(@Valid @RequestBody SellerRequestDTO request) {
        SellerDTO savedSeller = sellerService.createSeller(request);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/public/sellers")
    public ResponseEntity<SellerResponse> getAllSellers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_SELLERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        SellerResponse sellerResponse = sellerService.getAllSellers(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(sellerResponse, HttpStatus.FOUND);
    }

    @GetMapping("/public/sellers/{sellerId}")
    public ResponseEntity<SellerDTO> getSellerById(@PathVariable Long sellerId) {
        SellerDTO seller = sellerService.getSellerById(sellerId);
        return new ResponseEntity<>(seller, HttpStatus.FOUND);
    }

    @PutMapping("/admin/sellers/{sellerId}")
    public ResponseEntity<SellerDTO> updateSeller(
            @PathVariable Long sellerId,
            @Valid @RequestBody SellerRequestDTO request) {
        SellerDTO updatedSeller = sellerService.updateSeller(sellerId, request);
        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }

    @DeleteMapping("/admin/sellers/{sellerId}")
    public ResponseEntity<String> deleteSeller(@PathVariable Long sellerId) {
        String status = sellerService.deleteSeller(sellerId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/public/sellers/{sellerId}/products")
    public ResponseEntity<ProductResponse> getProductsBySeller(
            @PathVariable Long sellerId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = sellerService.getProductsBySeller(sellerId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/sellers/{sellerId}/products/{productId}")
    public ResponseEntity<ProductDTO> assignProduct(
            @PathVariable Long sellerId,
            @PathVariable Long productId) {
        ProductDTO productDTO = sellerService.assignProduct(sellerId, productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/sellers/{sellerId}/products/{productId}")
    public ResponseEntity<ProductDTO> unassignProduct(
            @PathVariable Long sellerId,
            @PathVariable Long productId) {
        ProductDTO productDTO = sellerService.unassignProduct(sellerId, productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

}
