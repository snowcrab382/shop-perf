package perf.shop.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.product.dao.CategoryRepository;
import perf.shop.domain.product.domain.Category;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] CategoryService")
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category createCategory(Long id, String name) {
        return Category.builder().id(id).name(name).build();
    }

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
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("카테고리 목록 조회 테스트")
    class FindAll {

        @Test
        @DisplayName("카테고리 목록 조회 성공")
        void findAll_ReturnCategoryResponseList() {
            // given
            Category category1 = createCategory(1L, "카테고리1");
            Category category2 = createCategory(2L, "카테고리2");
            given(categoryRepository.findAll()).willReturn(List.of(category1, category2));

            // when
            categoryService.findAll();

            // then
            assertThat(categoryService.findAll()).extracting("id", "name")
                    .containsExactly(
                            tuple(1L, "카테고리1"),
                            tuple(2L, "카테고리2")
                    );
        }
    }

}
