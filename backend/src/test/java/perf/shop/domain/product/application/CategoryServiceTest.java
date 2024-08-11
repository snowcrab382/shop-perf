package perf.shop.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.product.dao.CategoryRepository;
import perf.shop.domain.product.exception.CategoryNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] CategoryService")
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Nested
    @DisplayName("카테고리 검증 테스트")
    class ValidateCategoryExistsById {

        @Test
        @DisplayName("카테고리가 존재하면 검증 성공")
        void validateCategoryExistsById_Success_IfCategoryExists() {
            // given
            Long categoryId = 1L;
            given(categoryRepository.existsById(categoryId)).willReturn(true);

            // when
            categoryService.validateCategoryExistsById(categoryId);

            // then
            assertThat(categoryRepository.existsById(categoryId)).isTrue();
        }

        @Test
        @DisplayName("카테고리가 존재하지 않으면 예외 발생")
        void validateCategoryExistsById_ThrowException_IfCategoryNotExists() {
            // given
            Long categoryId = 1L;
            given(categoryRepository.existsById(categoryId)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> categoryService.validateCategoryExistsById(categoryId))
                    .isInstanceOf(CategoryNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

}
