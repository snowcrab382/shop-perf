package perf.shop.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.product.dao.ProductRepository;
import perf.shop.domain.product.domain.Product;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.domain.product.dto.response.ProductFindByIdResponse;
import perf.shop.domain.product.exception.ProductNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    public void saveProduct(ProductSaveRequest productSaveRequest, Long sellerId) {
        categoryService.validateCategoryExistsById(productSaveRequest.getCategoryId());
        productRepository.save(Product.of(productSaveRequest, sellerId));
    }

    @Transactional(readOnly = true)
    public ProductFindByIdResponse findProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductFindByIdResponse.of(product);
    }

}
