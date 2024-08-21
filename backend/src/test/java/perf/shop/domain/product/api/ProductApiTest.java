package perf.shop.domain.product.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static perf.shop.mock.fixtures.product.ProductFixture.createProductFindByIdResponse;
import static perf.shop.mock.fixtures.product.ProductFixture.createProductSaveRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
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
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;
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

        ResultActions save(ProductSaveRequest productSaveRequest) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.post("/products")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productSaveRequest))
                    .accept(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("성공")
        void saveProduct_success() throws Exception {
            // given
            ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "상품 설명", 10000L, 100L, 1L);

            // when
            ResultActions resultActions = save(productSaveRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.CREATED.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.CREATED.getMessage())))
                    .andExpect(jsonPath("$.data", equalTo(null)));
        }

        @Test
        @DisplayName("실패 - 입력값 검증에 실패하면 예외 발생")
        void saveProduct_throwException_ifInputValueIsInvalid() throws Exception {
            // given
            ProductSaveRequest productSaveRequest = createProductSaveRequest(null, "상품 설명", 10000L, 100L, 1L);

            // when
            ResultActions resultActions = save(productSaveRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors[0].field", equalTo("name")),
                            jsonPath("$.errors[0].value", equalTo(null)),
                            jsonPath("$.errors[0].message", equalTo("must not be blank"))
                    );
        }

        @Test
        @DisplayName("실패 - 상품 카테고리가 유효하지 않으면 예외 발생")
        void saveProduct_throwException_ifCategoryInvalid() throws Exception {
            // given
            ProductSaveRequest productSaveRequest = createProductSaveRequest("상품명", "상품 설명", 10000L, 100L, 1L);
            doThrow(new EntityNotFoundException(GlobalErrorCode.CATEGORY_NOT_FOUND))
                    .when(productService).saveProduct(any(ProductSaveRequest.class), anyLong());

            // when
            ResultActions resultActions = save(productSaveRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.CATEGORY_NOT_FOUND.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.CATEGORY_NOT_FOUND.getMessage())))
                    .andExpect(jsonPath("$.errors", equalTo(Collections.emptyList())));
        }

    }

    @Nested
    @DisplayName("상품 조회 API 테스트")
    class FindById {

        ResultActions findById(Long id) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", id)
                    .accept(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("성공")
        void findById_success() throws Exception {
            // given
            Long productId = 1L;
            ProductFindByIdResponse productDetail = createProductFindByIdResponse("상품명", 10000L, "image", "상품 설명",
                    100L);
            given(productService.findProductById(productId)).willReturn(productDetail);

            // when
            ResultActions resultActions = findById(productId);

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
        @DisplayName("실패 - 상품이 존재하지 않으면 예외 발생")
        void findById_throwException_ifProductNotFound() throws Exception {
            // given
            Long productId = 1L;
            given(productService.findProductById(productId)).willThrow(
                    new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));

            // when
            ResultActions resultActions = findById(productId);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.PRODUCT_NOT_FOUND.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.PRODUCT_NOT_FOUND.getMessage())))
                    .andExpect(jsonPath("$.errors", equalTo(List.of())));
        }
    }
}
