package perf.shop.domain.product.api;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import perf.shop.domain.product.application.ProductService;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.mock.InjectMockUser;

@InjectMockUser
@WebMvcTest(ProductApi.class)
@DisplayName("[단위 테스트] ProductApi")
class ProductApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @Nested
    @DisplayName("상품 저장 API 테스트")
    class Save {

        ResultActions save(ProductSaveRequest dto) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.post("/products")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }

        @Test
        @DisplayName("상품 저장 성공")
        void saveProduct_Success() throws Exception {
            // given
            ProductSaveRequest dto = ProductSaveRequest.builder()
                    .name("상품명")
                    .price(10000L)
                    .description("상품 설명")
                    .stock(100L)
                    .categoryId(1L)
                    .build();

            // when
            ResultActions resultActions = save(dto);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.CREATED.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.CREATED.getMessage())))
                    .andExpect(jsonPath("$.data", equalTo(null)));
        }

    }
}
