package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// ProductServiceImpl is a special class
@Service
@Slf4j // facade for ...
public class ProductServiceImpl implements ProductService{
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        return null;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return List.of();
    }

    @Override
    public String updateProduct(String productId, ProductRequest productRequest) {
        return "";
    }

    @Override
    public void deleteProduct(String productId) {

    }
}
