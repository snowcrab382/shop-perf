package perf.shop.domain.order.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static perf.shop.mock.fixtures.common.CommonFixture.createAddressRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createReceiverRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createShippingInfoRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrder;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderCreateRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderLineRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrdererRequest;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.domain.OrderLine;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.order.dto.request.OrdererRequest;
import perf.shop.domain.order.repository.OrdersRepository;
import perf.shop.domain.product.exception.OutOfStockException;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.ErrorCode;
import perf.shop.global.error.exception.InvalidValueException;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] OrdersService")
class OrdersServiceTest {

    @InjectMocks
    OrdersService ordersService;

    @Mock
    OrdersRepository ordersRepository;

    @Mock
    OrderLineFactory orderLineFactory;

    @Nested
    @DisplayName("주문 생성 테스트")
    class CreateOrder {

        @Test
        @DisplayName("성공")
        void createOrder_success() {
            // given
            Long userId = 1L;
            OrdererRequest ordererRequest = createOrdererRequest("주문자명", "test@naver.com");
            AddressRequest addressRequest = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiverRequest = createReceiverRequest("수령자명", "010-1234-5678", null);
            ShippingInfoRequest shippingInfoRequest = createShippingInfoRequest(addressRequest,
                    receiverRequest);
            OrderLineRequest orderLineRequest = createOrderLineRequest(1L, 1, 1000L);
            OrderCreateRequest request = createOrderCreateRequest(ordererRequest, shippingInfoRequest,
                    List.of(orderLineRequest));
            given(orderLineFactory.createOrderLine(any())).willReturn(OrderLine.from(orderLineRequest));

            Order newOrder = createOrder();
            given(ordersRepository.save(any(Order.class))).willReturn(newOrder);

            // when
            ordersService.createOrder(userId, request);

            // then
            then(ordersRepository).should().save(any(Order.class));
        }

        @Test
        @DisplayName("실패 - 주문 상품 리스트가 비어있는 경우 예외 발생")
        void createOrder_throwException_IfOrderLinesIsEmpty() {
            // given
            Long userId = 1L;
            OrdererRequest ordererRequest = createOrdererRequest("주문자명", "test@naver.com");
            AddressRequest addressRequest = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiverRequest = createReceiverRequest("수령자명", "010-1234-5678", null);
            ShippingInfoRequest shippingInfoRequest = createShippingInfoRequest(addressRequest,
                    receiverRequest);
            OrderCreateRequest request = createOrderCreateRequest(ordererRequest, shippingInfoRequest,
                    Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> ordersService.createOrder(userId, request))
                    .isInstanceOf(InvalidValueException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ORDER_LINE_NOT_EXIST);
        }

        @Test
        @DisplayName("실패 - 주문 상품이 존재하지 않는 경우 예외 발생")
        void createOrder_throwException_IfOrderLinesIsNull() {
            // given
            Long userId = 1L;
            OrdererRequest ordererRequest = createOrdererRequest("주문자명", "test@naver.com");
            AddressRequest addressRequest = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiverRequest = createReceiverRequest("수령자명", "010-1234-5678", null);
            ShippingInfoRequest shippingInfoRequest = createShippingInfoRequest(addressRequest,
                    receiverRequest);
            OrderLineRequest orderLineRequest = createOrderLineRequest(1L, 1, 1000L);
            OrderCreateRequest request = createOrderCreateRequest(ordererRequest, shippingInfoRequest,
                    List.of(orderLineRequest));
            given(orderLineFactory.createOrderLine(any())).willThrow(
                    new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> ordersService.createOrder(userId, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("실패 - 주문 상품의 재고가 부족하면 예외 발생")
        void createOrder_throwException_IfOrderProductOutOfStock() {
            // given
            Long userId = 1L;
            OrdererRequest ordererRequest = createOrdererRequest("주문자명", "test@naver.com");
            AddressRequest addressRequest = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiverRequest = createReceiverRequest("수령자명", "010-1234-5678", null);
            ShippingInfoRequest shippingInfoRequest = createShippingInfoRequest(addressRequest,
                    receiverRequest);
            OrderLineRequest orderLineRequest = createOrderLineRequest(1L, 1, 1000L);
            OrderCreateRequest request = createOrderCreateRequest(ordererRequest, shippingInfoRequest,
                    List.of(orderLineRequest));
            given(orderLineFactory.createOrderLine(any())).willThrow(
                    new OutOfStockException(ErrorCode.PRODUCT_OUT_OF_STOCK));

            // when & then
            assertThatThrownBy(() -> ordersService.createOrder(userId, request))
                    .isInstanceOf(OutOfStockException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PRODUCT_OUT_OF_STOCK);
        }
    }
}
