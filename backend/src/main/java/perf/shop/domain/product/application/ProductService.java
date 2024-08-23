package perf.shop.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.product.dao.ProductRepository;
import perf.shop.domain.product.domain.Product;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.domain.product.dto.response.ProductFindByIdResponse;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;

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
    public ProductFindByIdResponse findProductById(Long id) {
        Product product = getProduct(id);
        return ProductFindByIdResponse.of(product);
    }

    @Transactional(readOnly = true)
    public void checkProductStock(Long id, Integer quantity) {
        Product product = getProduct(id);
        product.checkProductStock(quantity);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));
    }

}
