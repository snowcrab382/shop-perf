package perf.shop.domain.order.api;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.domain.order.application.OrdersService;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.order.dto.request.OrdererRequest;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.ErrorCode;
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

        ResultActions createOrder(OrderCreateRequest dto) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
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
            OrderCreateRequest orderCreateRequest = createOrderCreateRequest(orderer, shippingInfo, orderLines);

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
            OrderCreateRequest orderCreateRequest = createOrderCreateRequest(orderer, shippingInfo, orderLines);

            // when
            ResultActions resultActions = createOrder(orderCreateRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors.[0].field", equalTo("orderer.ordererEmail")),
                            jsonPath("$.errors.[0].value", equalTo("")),
                            jsonPath("$.errors.[0].message", equalTo("이메일 형식이 올바르지 않습니다.")),
                            jsonPath("$.errors.[1].field", equalTo("shippingInfo.receiver.receiverPhone")),
                            jsonPath("$.errors.[1].value", equalTo("")),
                            jsonPath("$.errors.[1].message", equalTo("휴대폰 번호 형식이 올바르지 않습니다."))
                    );

        }

        OrderCreateRequest createOrderCreateRequest(OrdererRequest orderer, ShippingInfoRequest shippingInfo,
                                                    List<OrderLineRequest> orderLines) {
            return OrderCreateRequest.builder()
                    .orderer(orderer)
                    .shippingInfo(shippingInfo)
                    .orderLines(orderLines)
                    .build();
        }

        OrdererRequest createOrdererRequest(String ordererName, String ordererEmail) {
            return OrdererRequest.builder()
                    .ordererName(ordererName)
                    .ordererEmail(ordererEmail)
                    .build();
        }

        AddressRequest createAddressRequest(String roadAddress, String addressDetail, String zipcode) {
            return AddressRequest.builder()
                    .roadAddress(roadAddress)
                    .addressDetail(addressDetail)
                    .zipcode(zipcode)
                    .build();
        }

        ReceiverRequest createReceiverRequest(String receiverName, String receiverPhone, String requestMessage) {
            return ReceiverRequest.builder()
                    .receiverName(receiverName)
                    .receiverPhone(receiverPhone)
                    .requestMessage(requestMessage)
                    .build();
        }

        ShippingInfoRequest createShippingInfoRequest(AddressRequest address, ReceiverRequest receiver) {
            return ShippingInfoRequest.builder()
                    .address(address)
                    .receiver(receiver)
                    .build();
        }

        OrderLineRequest createOrderLineRequest(Long productId, Integer quantity, Long price) {
            return OrderLineRequest.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .price(price)
                    .build();
        }
    }

}
