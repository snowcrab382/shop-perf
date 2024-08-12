package perf.shop.domain.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sort {
    LATEST("latest"),
    PRICEASC("priceAsc"),
    PRICEDESC("priceDesc");

    private final String value;
}
