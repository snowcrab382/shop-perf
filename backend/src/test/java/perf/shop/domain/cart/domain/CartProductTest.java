package perf.shop.domain.cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import perf.shop.mock.fixtures.cart.CartFixture;

class CartProductTest {

    @Nested
    @DisplayName("장바구니 상품 수량 등록")
    class AddQuantity {

        @Test
        @DisplayName("성공 - 장바구니 상품 수량 증가")
        void addQuantity_success() throws Exception {
            // given
            CartProduct cartProduct = CartFixture.createCartProduct(null, null, 0);

            // when
            cartProduct.addQuantity(10);
            cartProduct.addQuantity(5);

            // then
            assertThat(cartProduct.getQuantity()).isEqualTo(15);

        }
    }

}
