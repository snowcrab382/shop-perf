package perf.shop.domain.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderLineRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.order.domain.OrderLine;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.product.application.ProductService;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] OrderLineFactory")
class OrderLineFactoryTest {

    @InjectMocks
    OrderLineFactory orderLineFactory;

    @Mock
    ProductService productService;

    @Nested
    @DisplayName("createOrderLine 메서드 테스트")
    class CreateOrderLine {

        @Test
        @DisplayName("성공")
        void createOrderLine_success() {
            // given
            OrderLineRequest orderLineRequest = createOrderLineRequest(1L, 100, 1000L);

            // when
            OrderLine orderLine = orderLineFactory.createOrderLine(orderLineRequest);

            // then
            then(productService).should().checkProductStock(anyLong(), anyInt());
            assertThat(orderLine).isNotNull();
        }

        @Test
        @DisplayName("실패 - 주문 상품이 존재하지 않는 경우 예외 발생")
        void createOrderLine_throwException_IfOrderProductNotExists() {
            // given
            OrderLineRequest orderLineRequest = createOrderLineRequest(1L, 100, 1000L);
            doThrow(new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND))
                    .when(productService).checkProductStock(anyLong(), anyInt());

            // when & then
            assertThatThrownBy(() -> orderLineFactory.createOrderLine(orderLineRequest))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("실패 - 주문 상품의 재고가 부족한 경우 예외 발생")
        void createOrderLine_throwException_IfProductOutOfStock() {
            // given
            OrderLineRequest orderLineRequest = createOrderLineRequest(1L, 100, 1000L);
            doThrow(new EntityNotFoundException(ErrorCode.PRODUCT_OUT_OF_STOCK))
                    .when(productService).checkProductStock(anyLong(), anyInt());

            // when & then
            assertThatThrownBy(() -> orderLineFactory.createOrderLine(orderLineRequest))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PRODUCT_OUT_OF_STOCK);
        }
    }

}
