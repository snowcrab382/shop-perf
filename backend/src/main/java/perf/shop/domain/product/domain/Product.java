package perf.shop.domain.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.domain.product.exception.OutOfStockException;
import perf.shop.global.common.domain.BaseEntity;
import perf.shop.global.error.exception.GlobalErrorCode;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(length = 30, nullable = false)
    private String name;

    private String image;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long stock;

    @Builder
    private Product(Long categoryId, Long sellerId, String name, String description, Long price, Long stock) {
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    //TODO 이미지 업로드는 고민이 필요
    public static Product of(ProductSaveRequest productSaveRequest, Long sellerId) {
        return Product.builder()
                .categoryId(productSaveRequest.getCategoryId())
                .sellerId(sellerId)
                .name(productSaveRequest.getName())
                .description(productSaveRequest.getDescription())
                .price(productSaveRequest.getPrice())
                .stock(productSaveRequest.getStock())
                .build();
    }

    public void deductStock(Integer quantity) {
        checkProductStock(quantity);
        stock -= quantity;
    }

    public void restoreStock(Integer quantity) {
        checkProductStock(quantity);
        stock += quantity;
    }

    private void checkProductStock(Integer quantity) {
        if (stock < quantity) {
            throw new OutOfStockException(GlobalErrorCode.PRODUCT_OUT_OF_STOCK);
        }
    }


}
