package com.app.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.Seller;
import com.app.entites.User;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.ProductDTO;
import com.app.payloads.ProductResponse;
import com.app.payloads.SellerDTO;
import com.app.payloads.SellerRequestDTO;
import com.app.payloads.SellerResponse;
import com.app.repositories.ProductRepo;
import com.app.repositories.SellerRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SellerDTO createSeller(SellerRequestDTO request) {
        User owner = userRepo.findById(request.getOwnerUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", request.getOwnerUserId()));

        Seller seller = new Seller();
        seller.setName(request.getName());
        seller.setDescription(request.getDescription());
        seller.setOwner(owner);

        Seller savedSeller = sellerRepo.save(seller);

        return toSellerDTO(savedSeller);
    }

    @Override
    public SellerResponse getAllSellers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Seller> pageSellers = sellerRepo.findAll(pageDetails);

        List<Seller> sellers = pageSellers.getContent();

        if (sellers.isEmpty()) {
            throw new APIException("No sellers found !!!");
        }

        List<SellerDTO> sellerDTOs = sellers.stream()
                .map(this::toSellerDTO)
                .collect(Collectors.toList());

        SellerResponse sellerResponse = new SellerResponse();
        sellerResponse.setContent(sellerDTOs);
        sellerResponse.setPageNumber(pageSellers.getNumber());
        sellerResponse.setPageSize(pageSellers.getSize());
        sellerResponse.setTotalElements(pageSellers.getTotalElements());
        sellerResponse.setTotalPages(pageSellers.getTotalPages());
        sellerResponse.setLastPage(pageSellers.isLast());

        return sellerResponse;
    }

    @Override
    public SellerDTO getSellerById(Long sellerId) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "sellerId", sellerId));

        return toSellerDTO(seller);
    }

    @Override
    public SellerDTO updateSeller(Long sellerId, SellerRequestDTO request) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "sellerId", sellerId));

        User owner = userRepo.findById(request.getOwnerUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", request.getOwnerUserId()));

        seller.setName(request.getName());
        seller.setDescription(request.getDescription());
        seller.setOwner(owner);

        Seller updatedSeller = sellerRepo.save(seller);

        return toSellerDTO(updatedSeller);
    }

    @Override
    public String deleteSeller(Long sellerId) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "sellerId", sellerId));

        if (productRepo.existsBySellerId(sellerId)) {
            throw new APIException("Cannot delete seller with sellerId: " + sellerId + " because it has assigned products !!!");
        }

        sellerRepo.delete(seller);

        return "Seller with sellerId: " + sellerId + " deleted successfully !!!";
    }

    @Override
    public ProductResponse getProductsBySeller(Long sellerId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "sellerId", sellerId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepo.findBySellerId(sellerId, pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.isEmpty()) {
            throw new APIException("No products found for seller with sellerId: " + sellerId + " !!!");
        }

        SellerDTO sellerDTO = toSellerDTO(seller);

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> {
                    ProductDTO dto = modelMapper.map(product, ProductDTO.class);
                    dto.setSeller(sellerDTO);
                    return dto;
                })
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO assignProduct(Long sellerId, Long productId) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "sellerId", sellerId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        product.setSeller(seller);
        Product savedProduct = productRepo.save(product);

        ProductDTO dto = modelMapper.map(savedProduct, ProductDTO.class);
        dto.setSeller(toSellerDTO(seller));

        return dto;
    }

    @Override
    public ProductDTO unassignProduct(Long sellerId, Long productId) {
        sellerRepo.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "sellerId", sellerId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getSeller() == null || !product.getSeller().getId().equals(sellerId)) {
            throw new APIException("Product with productId: " + productId + " is not assigned to seller with sellerId: " + sellerId + " !!!");
        }

        product.setSeller(null);
        Product savedProduct = productRepo.save(product);

        ProductDTO dto = modelMapper.map(savedProduct, ProductDTO.class);
        dto.setSeller(null);

        return dto;
    }


    private SellerDTO toSellerDTO(Seller seller) {
        SellerDTO dto = modelMapper.map(seller, SellerDTO.class);
        dto.setOwnerUserId(seller.getOwner().getUserId());
        return dto;
    }

    public List<ProductDTO> mapProductsWithSellers(List<Product> products, ModelMapper modelMapper) {
        List<Long> sellerIds = products.stream()
                .map(Product::getSeller)
                .filter(Objects::nonNull)
                .map(Seller::getId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Seller> sellerMap = sellerRepo.findAllById(sellerIds)
                .stream()
                .collect(Collectors.toMap(Seller::getId, s -> s));

        return products.stream()
                .map(product -> {
                    ProductDTO dto = modelMapper.map(product, ProductDTO.class);
                    if (product.getSeller() != null) {
                        Seller seller = sellerMap.get(product.getSeller().getId());
                        if (seller != null) {
                            SellerDTO sellerDTO = modelMapper.map(seller, SellerDTO.class);
                            sellerDTO.setOwnerUserId(seller.getOwner().getUserId());
                            dto.setSeller(sellerDTO);
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
