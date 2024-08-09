package perf.shop.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.product.dao.CategoryRepository;
import perf.shop.domain.product.exception.CategoryNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public void validateCategoryExistsById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
}
