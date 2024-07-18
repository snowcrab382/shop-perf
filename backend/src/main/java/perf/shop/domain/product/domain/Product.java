package perf.shop.product.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Product {

    private Long id;
    private String name;
}
