package perf.shop.domain.product.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Product {

    private Long id;
    private String name;
}
