package perf.shop.domain.order.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static perf.shop.mock.fixtures.common.CommonFixture.createAddressRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createReceiverRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createShippingInfoRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderCreateRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderLineRequest;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrdererRequest;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.domain.Orderer;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.order.dto.request.OrdererRequest;
import perf.shop.domain.order.repository.OrdersRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] OrdersService")
class OrdersServiceTest {

    @InjectMocks
    OrdersService ordersService;

    @Mock
    OrdersRepository ordersRepository;

    @Nested
    @DisplayName("주문 생성 테스트")
    class CreateOrder {

        @Test
        @DisplayName("성공")
        void createOrder_success() {
            // given
            Long userId = 1L;
            OrdererRequest orderer = createOrdererRequest("주문자", "");
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            List<OrderLineRequest> orderLines = List.of(
                    createOrderLineRequest(1L, 2, 10000L),
                    createOrderLineRequest(2L, 1, 20000L)
            );
            OrderCreateRequest request = createOrderCreateRequest(orderer, shippingInfo, orderLines);

            Order newOrder = Order.of(Orderer.from(userId, orderer), ShippingInfo.from(shippingInfo));
            given(ordersRepository.save(any(Order.class))).willReturn(newOrder);

            // when
            ordersService.createOrder(userId, request);

            // then
            then(ordersRepository).should().save(any(Order.class));

        }
    }
}
