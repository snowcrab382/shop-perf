package perf.shop.domain.product.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static perf.shop.mock.fixtures.product.SearchFixture.createProductSearchResponse;
import static perf.shop.mock.fixtures.product.SearchFixture.createSearchCondition;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import perf.shop.domain.product.application.SearchService;
import perf.shop.domain.product.dto.request.SearchCondition;
import perf.shop.domain.product.dto.response.ProductSearchResponse;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.ErrorCode;
import perf.shop.mock.InjectMockUser;

@InjectMockUser
@WebMvcTest(SearchApi.class)
@DisplayName("[단위 테스트] SearchApi")
class SearchApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SearchService searchService;

    @Nested
    @DisplayName("상품 검색 API 테스트")
    class SearchByCondition {

        ResultActions searchByCondition(SearchCondition condition, Pageable pageable) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.get("/products/search")
                    .param("categoryId", String.valueOf(condition.getCategoryId()))
                    .param("keyword", condition.getKeyword())
                    .param("sorter", condition.getSorter())
                    .param("page", String.valueOf(pageable.getPageNumber()))
                    .param("size", String.valueOf(pageable.getPageSize()))
                    .accept(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("성공")
        void searchByCondition_success() throws Exception {
            // given
            SearchCondition condition = createSearchCondition(1L, "상품", "priceDesc");
            Pageable pageable = Pageable.ofSize(1).withPage(0);
            ProductSearchResponse response1 = createProductSearchResponse(1L, "상품", 2000L, "이미지");
            ProductSearchResponse response2 = createProductSearchResponse(2L, "상품", 1000L, "이미지");
            Page<ProductSearchResponse> contents = PageableExecutionUtils.getPage(
                    List.of(response1, response2), pageable, () -> 3L);

            given(searchService.search(any(SearchCondition.class), any(Pageable.class))).willReturn(contents);

            // when
            ResultActions resultActions = searchByCondition(condition, pageable);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.GET.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.GET.getMessage())))
                    .andExpectAll(
                            jsonPath("$.data.content[0].id", equalTo(1)),
                            jsonPath("$.data.content[0].name", equalTo("상품")),
                            jsonPath("$.data.content[0].price", equalTo(2000)),
                            jsonPath("$.data.content[0].image", equalTo("이미지")),
                            jsonPath("$.data.content[1].id", equalTo(2)),
                            jsonPath("$.data.content[1].name", equalTo("상품")),
                            jsonPath("$.data.content[1].price", equalTo(1000)),
                            jsonPath("$.data.content[1].image", equalTo("이미지")),
                            jsonPath("$.data.totalElements", equalTo(3)),
                            jsonPath("$.data.totalPages", equalTo(3)),
                            jsonPath("$.data.last", equalTo(false)),
                            jsonPath("$.data.size", equalTo(1)),
                            jsonPath("$.data.number", equalTo(0)),
                            jsonPath("$.data.numberOfElements", equalTo(2)),
                            jsonPath("$.data.first", equalTo(true)),
                            jsonPath("$.data.empty", equalTo(false))
                    );
        }

        @Test
        @DisplayName("실패 - 상품 정렬 조건이 ENUM값과 다를 경우 예외 발생")
        void searchByCondition_throwException_ifInvalidSorter() throws Exception {
            // given
            SearchCondition condition = createSearchCondition(1L, "상품", "invalidSorter");
            Pageable pageable = Pageable.ofSize(1).withPage(0);

            // when
            ResultActions resultActions = searchByCondition(condition, pageable);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors[0].field", equalTo("sorter")),
                            jsonPath("$.errors[0].value", equalTo("invalidSorter")),
                            jsonPath("$.errors[0].message", equalTo("ENUM 값이 올바르지 않습니다."))
                    );
        }
    }

}
