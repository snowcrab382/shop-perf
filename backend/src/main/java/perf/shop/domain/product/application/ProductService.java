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

    public void deductStocks(Order order) {
        order.getOrderLines().forEach(orderLine -> {
            Product product = getProductForUpdate(orderLine.getProductId());
            product.deductStock(orderLine.getQuantity());
        });
    }

    public void deductStocksWithOutLock(Order order) {
        order.getOrderLines().forEach(orderLine -> {
            int count = productRepository.deductStock(orderLine.getProductId(), orderLine.getQuantity());
            if (count == 0) {
                throw new EntityNotFoundException(GlobalErrorCode.PRODUCT_OUT_OF_STOCK);
            }
        });
    }

    public void cancelDeductStocksWithOutLock(Order order) {
        order.getOrderLines().forEach(orderLine -> {
            productRepository.cancelDeductStock(orderLine.getProductId(), orderLine.getQuantity());
        });
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

    //TODO: 비관적 락, 동시성 문제 해결 테스트에 사용
    private Product getProductForUpdate(Long id) {
        return productRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));
    }

}
