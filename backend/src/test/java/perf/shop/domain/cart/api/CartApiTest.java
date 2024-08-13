package perf.shop.domain.cart.api;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import perf.shop.domain.cart.dto.request.AddProductRequest;
import perf.shop.domain.cart.service.CartService;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.ErrorCode;
import perf.shop.mock.InjectMockUser;

@InjectMockUser
@WebMvcTest(controllers = CartApi.class)
@DisplayName("[단위 테스트] CartApi")
class CartApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CartService cartService;

    @Nested
    @DisplayName("장바구니 상품 추가 API 테스트")
    class AddProduct {

        AddProductRequest createAddProductRequest(Long productId, Integer quantity) {
            return AddProductRequest.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build();
        }

        ResultActions addProduct(AddProductRequest dto) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        }

        @Test
        @DisplayName("상품 장바구니 추가 성공")
        void addProduct_Success() throws Exception {
            // given
            AddProductRequest dto = createAddProductRequest(1L, 2);

            // when
            ResultActions resultActions = addProduct(dto);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.CREATED.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.CREATED.getMessage())))
                    .andExpect(jsonPath("$.data", equalTo(null)));
        }

        @Test
        @DisplayName("입력값 검증에 실패한 경우 예외 발생")
        void addProduct_ThrowException_IfInputValueIsInvalid() throws Exception {
            // given
            AddProductRequest dto = createAddProductRequest(null, 2);

            // when
            ResultActions resultActions = addProduct(dto);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors[0].field", equalTo("productId")),
                            jsonPath("$.errors[0].value", equalTo(null)),
                            jsonPath("$.errors[0].message", equalTo("must not be null"))
                    );
        }
    }

}
