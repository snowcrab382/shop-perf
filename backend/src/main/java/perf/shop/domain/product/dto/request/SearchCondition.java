package perf.shop.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import perf.shop.global.annotation.EnumValue;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchCondition {

    private String keyword;
    private Long categoryId;

    @EnumValue(enumClass = Sort.class, ignoreCase = true)
    @NotBlank
    private String sorter;

    @Builder
    private SearchCondition(String keyword, Long categoryId, String sorter) {
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.sorter = sorter;
    }

}
