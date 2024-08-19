package perf.shop.domain.product.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static perf.shop.mock.fixtures.product.ProductFixture.createProductSaveRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("[단위 테스트] ProductSaveRequest")
class ProductSaveRequestTest {

    static ValidatorFactory factory;
    static Validator validator;

    @BeforeAll
    static void init() {
        factory = jakarta.validation.Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("실패 - 카테고리ID가 없으면 유효성 검증 실패")
    void categoryId_failValidation_isNull() {
        // given
        ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "상품 설명", 10000L, 10L, null);

        // when
        Set<ConstraintViolation<ProductSaveRequest>> violations = validator.validate(productSaveRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("널이어서는 안됩니다");
    }

    @Test
    @DisplayName("실패 - 상품명이 비어있으면 유효성 검증 실패")
    void name_failValidation_isBlank() {
        // given
        ProductSaveRequest productSaveRequest = createProductSaveRequest("", "상풍 설명", 10000L, 10L, 100L);

        // when
        Set<ConstraintViolation<ProductSaveRequest>> violations = validator.validate(productSaveRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("공백일 수 없습니다");
    }

    @Test
    @DisplayName("실패 - 상품설명이 비어있으면 유효성 검증 실패")
    void description_failValidation_isBlank() {
        // given
        ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "", 10000L, 10L, 100L);

        // when
        Set<ConstraintViolation<ProductSaveRequest>> violations = validator.validate(productSaveRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("공백일 수 없습니다");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1000})
    @DisplayName("실패 - 가격이 최소값보다 작으면 유효성 검증 실패")
    void price_failValidation_isLessThanMin(long price) {
        // given
        ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "상풍 설명", price, 10L, 100L);

        // when
        Set<ConstraintViolation<ProductSaveRequest>> violations = validator.validate(productSaveRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("1 이상이어야 합니다");
    }

    @ParameterizedTest
    @ValueSource(longs = {1000001, 9999999})
    @DisplayName("실패 - 가격이 최대값보다 크다면 유효성 검증 실패")
    void price_failValidation_isGreaterThanMax(long price) {
        // given
        ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "상풍 설명", price, 10L, 100L);

        // when
        Set<ConstraintViolation<ProductSaveRequest>> violations = validator.validate(productSaveRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("1000000 이하여야 합니다");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1000})
    @DisplayName("실패 - 재고수량이 최소값보다 작으면 유효성 검증 실패")
    void stock_failValidation_isLessThanMin(long stock) {
        // given
        ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "상풍 설명", 10000L, stock, 100L);

        // when
        Set<ConstraintViolation<ProductSaveRequest>> violations = validator.validate(productSaveRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("1 이상이어야 합니다");
    }

    @ParameterizedTest
    @ValueSource(longs = {100001, 999999})
    @DisplayName("실패 - 가격이 최대값보다 크다면 유효성 검증 실패")
    void stock_failValidation_isGreaterThanMax(long stock) {
        // given
        ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "상풍 설명", 10000L, stock, 100L);

        // when
        Set<ConstraintViolation<ProductSaveRequest>> violations = validator.validate(productSaveRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("100000 이하여야 합니다");
    }
}
