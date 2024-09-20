package perf.shop.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.domain.Order;
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

    @Transactional(readOnly = true)
    public void validateProductExistsById(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    public void saveProduct(ProductSaveRequest productSaveRequest, Long sellerId) {
        categoryService.validateCategoryExistsById(productSaveRequest.getCategoryId());
        productRepository.save(Product.of(productSaveRequest, sellerId));
    }

    @Transactional(readOnly = true)
    public ProductFindByIdResponse findProductById(Long id) {
        Product product = getProduct(id);
        return ProductFindByIdResponse.of(product);
    }

    public void deductStockFromOrder(Order order) {
        order.getOrderLines().forEach(orderLine -> {
            deductStock(orderLine.getProductId(), orderLine.getQuantity());
        });
    }

    public void deductStock(Long productId, Integer quantity) {
        Product product = getProduct(productId);
        product.deductStock(quantity);
    }

    public void restoreStock(Order order) {
        order.getOrderLines().forEach(orderLine -> {
            Product product = getProduct(orderLine.getProductId());
            product.restoreStock(orderLine.getQuantity());
        });
    }

    private Product getProductForUpdate(Long id) {
        return productRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));
    }

}
