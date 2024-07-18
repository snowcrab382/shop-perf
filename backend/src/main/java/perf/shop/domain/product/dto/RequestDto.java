package perf.shop.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
