package perf.shop.domain.order.api;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static perf.shop.mock.fixtures.common.CommonFixture.createAddressRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createReceiverRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createShippingInfoRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderCreateRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderLineRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrdererRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createPaymentInfoRequest;

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
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.domain.order.application.OrdersService;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.order.dto.request.OrdererRequest;
import perf.shop.domain.order.dto.request.PaymentInfoRequest;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.GlobalErrorCode;
import perf.shop.global.error.exception.InvalidValueException;
import perf.shop.mock.InjectMockUser;

@InjectMockUser
@WebMvcTest(controllers = OrdersApi.class)
@DisplayName("[단위 테스트] OrdersApi")
class OrdersApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrdersService ordersService;

    @Nested
    @DisplayName("주문 생성 API 테스트")
    class CreateOrder {

        ResultActions createOrder(OrderCreateRequest orderCreateRequest) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderCreateRequest))
                    .accept(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("성공")
        void createOrder_success() throws Exception {
            // given
            OrdererRequest orderer = createOrdererRequest("주문자", "test@naver.com");
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            List<OrderLineRequest> orderLines = List.of(
                    createOrderLineRequest(1L, 2, 10000L),
                    createOrderLineRequest(2L, 1, 20000L)
            );
            PaymentInfoRequest paymentInfo = createPaymentInfoRequest("CARD", "TOSS");
            OrderCreateRequest orderCreateRequest = createOrderCreateRequest(orderer, shippingInfo, orderLines,
                    paymentInfo);

            // when
            ResultActions resultActions = createOrder(orderCreateRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.CREATED.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.CREATED.getMessage())))
                    .andExpect(jsonPath("$.data", equalTo(null)));

        }

        @Test
        @DisplayName("실패 - 입력값 검증에 실패하는 경우 예외 발생")
        void createOrder_throwException_IfInputValueIsInvalid() throws Exception {
            // given
            OrdererRequest orderer = createOrdererRequest("주문자", "");
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            List<OrderLineRequest> orderLines = List.of(
                    createOrderLineRequest(1L, 2, 10000L),
                    createOrderLineRequest(2L, 1, 20000L)
            );
            PaymentInfoRequest paymentInfo = createPaymentInfoRequest("CARD", "TOSS");
            OrderCreateRequest orderCreateRequest = createOrderCreateRequest(orderer, shippingInfo, orderLines,
                    paymentInfo);

            // when
            ResultActions resultActions = createOrder(orderCreateRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors.[*].field",
                                    containsInAnyOrder("orderer.ordererEmail", "shippingInfo.receiver.receiverPhone")),
                            jsonPath("$.errors.[*].value", containsInAnyOrder("", "")),
                            jsonPath("$.errors.[*].message",
                                    containsInAnyOrder("이메일 형식이 올바르지 않습니다.", "휴대폰 번호 형식이 올바르지 않습니다."))
                    );
        }

        @Test
        @DisplayName("실패 - 주문 상품이 존재하지 않는 경우 예외 발생")
        void createOrder_throwException_IfOrderProductNotExists() throws Exception {
            // given
            OrdererRequest orderer = createOrdererRequest("주문자", "test@naver.com");
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            List<OrderLineRequest> orderLines = List.of(
                    createOrderLineRequest(1L, 2, 10000L),
                    createOrderLineRequest(2L, 1, 20000L)
            );
            PaymentInfoRequest paymentInfo = createPaymentInfoRequest("CARD", "TOSS");
            OrderCreateRequest orderCreateRequest = createOrderCreateRequest(orderer, shippingInfo, orderLines,
                    paymentInfo);
            doThrow(new InvalidValueException(GlobalErrorCode.ORDER_LINE_NOT_EXIST))
                    .when(ordersService).createOrder(any(), any());

            // when
            ResultActions resultActions = createOrder(orderCreateRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.ORDER_LINE_NOT_EXIST.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.ORDER_LINE_NOT_EXIST.getMessage())))
                    .andExpect(jsonPath("$.errors", equalTo(Collections.emptyList())));
        }

        @Test
        @DisplayName("실패 - 주문 상품의 재고가 부족한 경우 예외 발생")
        void createOrder_throwException_IfProductOutOfStock() throws Exception {
            // given
            OrdererRequest orderer = createOrdererRequest("주문자", "test@naver.com");
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            List<OrderLineRequest> orderLines = List.of(
                    createOrderLineRequest(1L, 2, 10000L),
                    createOrderLineRequest(2L, 1, 20000L)
            );
            PaymentInfoRequest paymentInfo = createPaymentInfoRequest("CARD", "TOSS");
            OrderCreateRequest orderCreateRequest = createOrderCreateRequest(orderer, shippingInfo, orderLines,
                    paymentInfo);
            doThrow(new InvalidValueException(GlobalErrorCode.PRODUCT_OUT_OF_STOCK))
                    .when(ordersService).createOrder(any(), any());

            // when
            ResultActions resultActions = createOrder(orderCreateRequest);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.PRODUCT_OUT_OF_STOCK.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.PRODUCT_OUT_OF_STOCK.getMessage())))
                    .andExpect(jsonPath("$.errors", equalTo(Collections.emptyList())));
        }
    }

}
