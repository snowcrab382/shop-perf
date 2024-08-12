package perf.shop.domain.product.dao;

import static org.springframework.util.StringUtils.hasText;
import static perf.shop.domain.product.domain.QProduct.product;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import perf.shop.domain.product.dto.request.SearchCondition;
import perf.shop.domain.product.dto.request.Sort;
import perf.shop.domain.product.dto.response.ProductSearchResponse;
import perf.shop.domain.product.dto.response.QProductSearchResponse;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepository {

    private final JPAQueryFactory queryFactory;

    public Page<ProductSearchResponse> search(SearchCondition condition, Pageable pageable) {
        List<ProductSearchResponse> content = queryFactory
                .select(new QProductSearchResponse(
                        product.id,
                        product.name,
                        product.price,
                        product.image
                ))
                .from(product)
                .where(
                        categoryIdEquals(condition.getCategoryId()),
                        keywordContains(condition.getKeyword())
                )
                .orderBy(sortBy(condition.getSorter()), product.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        categoryIdEquals(condition.getCategoryId()),
                        keywordContains(condition.getKeyword())
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);

    }

    private OrderSpecifier<?> sortBy(String sorter) {
        if (Sort.LATEST.getValue().equalsIgnoreCase(sorter)) {
            return product.createdAt.desc();
        }

        if (Sort.PRICEASC.getValue().equalsIgnoreCase(sorter)) {
            return product.price.asc();
        }

        if (Sort.PRICEDESC.getValue().equalsIgnoreCase(sorter)) {
            return product.price.desc();
        }

        return null;
    }

    private BooleanExpression keywordContains(String keyword) {
        return hasText(keyword) ? product.name.contains(keyword) : null;
    }

    private BooleanExpression categoryIdEquals(Long categoryId) {
        return categoryId != null ? product.categoryId.eq(categoryId) : null;
    }


}
