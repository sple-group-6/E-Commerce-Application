package com.app.services;

import com.app.payloads.ProductDTO;
import com.app.payloads.ProductResponse;
import com.app.payloads.SellerDTO;
import com.app.payloads.SellerRequestDTO;
import com.app.payloads.SellerResponse;

public interface SellerService {

    SellerDTO createSeller(SellerRequestDTO request);

    SellerResponse getAllSellers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    SellerDTO getSellerById(Long sellerId);

    SellerDTO updateSeller(Long sellerId, SellerRequestDTO request);

    String deleteSeller(Long sellerId);

    ProductResponse getProductsBySeller(Long sellerId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO assignProduct(Long sellerId, Long productId);

    ProductDTO unassignProduct(Long sellerId, Long productId);

}
