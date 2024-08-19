package perf.shop.domain.product.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static perf.shop.mock.fixtures.product.CategoryFixture.createCategoryResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import perf.shop.domain.product.application.CategoryService;
import perf.shop.domain.product.dto.response.CategoryResponse;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.mock.InjectMockUser;

@InjectMockUser
@WebMvcTest(CategoryApi.class)
@DisplayName("[단위 테스트] CategoryApi")
class CategoryApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoryService categoryService;

    @Nested
    @DisplayName("카테고리 목록 조회 API 테스트")
    class FindAll {

        @Test
        @DisplayName("카테고리 목록 조회 성공")
        void findAll_Success() throws Exception {
            // given
            CategoryResponse category1 = createCategoryResponse(1L, "카테고리1");
            CategoryResponse category2 = createCategoryResponse(2L, "카테고리2");
            given(categoryService.findAll()).willReturn(List.of(category1, category2));

            // when
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                    .accept(MediaType.APPLICATION_JSON));

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.GET.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.GET.getMessage())))
                    .andExpectAll(
                            jsonPath("$.data[0].id", equalTo(1)),
                            jsonPath("$.data[0].name", equalTo("카테고리1")),
                            jsonPath("$.data[1].id", equalTo(2)),
                            jsonPath("$.data[1].name", equalTo("카테고리2"))
                    );
        }
    }
}
