package perf.shop.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.product.dao.ProductSearchRepository;
import perf.shop.domain.product.dto.request.SearchCondition;
import perf.shop.domain.product.dto.response.ProductSearchResponse;

@Transactional
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductSearchRepository productSearchRepository;

    @Transactional(readOnly = true)
    public Page<ProductSearchResponse> search(SearchCondition condition, Pageable pageable) {
        return productSearchRepository.search(condition, pageable);
    }
}
