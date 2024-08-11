package perf.shop.domain.product.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import perf.shop.domain.product.application.ProductService;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.domain.product.dto.response.ProductFindByIdResponse;
import perf.shop.domain.product.exception.ProductNotFoundException;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.ErrorCode;
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

    @Nested
    @DisplayName("상품 조회 API 테스트")
    class FindById {

        @Test
        @DisplayName("상품 조회 성공")
        void findById_Success() throws Exception {
            // given
            Long id = 1L;
            ProductFindByIdResponse productDetail = ProductFindByIdResponse.builder()
                    .name("상품명")
                    .price(10000L)
                    .image("상품 이미지 URL")
                    .description("상품 설명")
                    .stock(100L)
                    .build();
            given(productService.findProductById(id)).willReturn(productDetail);

            // when
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", id)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.GET.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.GET.getMessage())))
                    .andExpectAll(
                            jsonPath("$.data.name").exists(),
                            jsonPath("$.data.price").exists(),
                            jsonPath("$.data.image").exists(),
                            jsonPath("$.data.description").exists(),
                            jsonPath("$.data.stock").exists()
                    );
        }

        @Test
        @DisplayName("상품이 존재하지 않으면 예외 발생")
        void findById_ThrowException_IfProductNotFound() throws Exception {
            // given
            Long id = 1L;
            given(productService.findProductById(id)).willThrow(
                    new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

            // when
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", id)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ErrorCode.PRODUCT_NOT_FOUND.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ErrorCode.PRODUCT_NOT_FOUND.getMessage())))
                    .andExpect(jsonPath("$.errors", equalTo(List.of())));
        }
    }
}
