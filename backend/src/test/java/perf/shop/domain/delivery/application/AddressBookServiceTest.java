package perf.shop.domain.delivery.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.delivery.domain.AddressBook;
import perf.shop.domain.delivery.dto.request.AddressBookRequest;
import perf.shop.domain.delivery.repository.AddressBookRepository;
import perf.shop.domain.model.Address;
import perf.shop.domain.model.Receiver;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;
import perf.shop.mock.fixtures.common.CommonFixture;
import perf.shop.mock.fixtures.delivery.AddressBookFixture;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] AddressBookService")
class AddressBookServiceTest {

    @InjectMocks
    AddressBookService addressBookService;

    @Mock
    AddressBookRepository addressBookRepository;

    @Nested
    @DisplayName("주소록 조회 테스트")
    class GetAddressBook {

        @Test
        @DisplayName("성공 - 주소록이 존재하지 않아도 빈 리스트를 반환하며 예외가 발생하지 않음")
        void getAddressBook_success() {
            // given
            Long userId = 1L;
            given(addressBookRepository.findAllByUserId(userId)).willReturn(List.of());

            // when
            addressBookService.findAllByUserId(userId);

            // then
            then(addressBookRepository).should().findAllByUserId(userId);
        }
    }

    @Nested
    @DisplayName("주소록 저장 테스트")
    class saveAddressBook {

        @Test
        @DisplayName("성공")
        void saveAddressBook_success() {
            // given
            Long userId = 1L;
            AddressRequest address = CommonFixture.createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = CommonFixture.createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfoRequest = CommonFixture.createShippingInfoRequest(address, receiver);
            AddressBookRequest addressBookRequest = AddressBookFixture.createAddressBookRequest(shippingInfoRequest);

            ShippingInfo shippingInfo = ShippingInfo.from(shippingInfoRequest);
            AddressBook addressBook = AddressBook.of(userId, shippingInfo);
            given(addressBookRepository.save(any(AddressBook.class))).willReturn(addressBook);

            // when
            addressBookService.saveAddressBook(addressBookRequest, userId);

            // then
            then(addressBookRepository).should().save(any(AddressBook.class));
        }
    }

    @Nested
    @DisplayName("주소록 수정 테스트")
    class UpdateAddressBook {

        @Test
        @DisplayName("성공")
        void updateAddressBook_success() {
            // given
            Long addressBookId = 1L;
            Long userId = 1L;
            AddressRequest addressRequest = CommonFixture.createAddressRequest("변경된 주소", "주소", "12345");
            ReceiverRequest receiverRequest = CommonFixture.createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfoRequest = CommonFixture.createShippingInfoRequest(addressRequest,
                    receiverRequest);
            AddressBookRequest addressBookRequest = AddressBookFixture.createAddressBookRequest(shippingInfoRequest);

            Address address = CommonFixture.createAddress("기존 주소", "주소", "12345");
            Receiver receiver = CommonFixture.createReceiver("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfo shippingInfo = CommonFixture.createShippingInfo(address, receiver);
            AddressBook addressBook = AddressBookFixture.createAddressBook(userId, shippingInfo);
            given(addressBookRepository.findByIdAndUserId(addressBookId, userId)).willReturn(
                    Optional.ofNullable(addressBook));

            // when
            addressBookService.updateAddressBook(addressBookId, addressBookRequest, userId);

            // then
            assertThat(addressBook.getShippingInfo().getAddress().getRoadAddress()).isEqualTo("변경된 주소");
        }

        @Test
        @DisplayName("실패 - 주소록이 존재하지 않으면 예외 발생")
        void updateAddressBook_throwException_IfAddressBookNotExists() {
            // given
            Long addressBookId = 1L;
            Long userId = 1L;
            AddressRequest addressRequest = CommonFixture.createAddressRequest("변경된 주소", "주소", "12345");
            ReceiverRequest receiverRequest = CommonFixture.createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfoRequest = CommonFixture.createShippingInfoRequest(addressRequest,
                    receiverRequest);
            AddressBookRequest addressBookRequest = AddressBookFixture.createAddressBookRequest(shippingInfoRequest);

            given(addressBookRepository.findByIdAndUserId(addressBookId, userId)).willReturn(
                    Optional.empty());

            // when & then
            assertThatThrownBy(() -> addressBookService.updateAddressBook(addressBookId, addressBookRequest, userId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("주소록 삭제 테스트")
    class DeleteAddressBook {

        @Test
        @DisplayName("성공")
        void deleteAddressBook_success() {
            // given
            Long userId = 1L;
            Long addressBookId = 1L;
            Address address = CommonFixture.createAddress("서울시 강남구", "주소", "12345");
            Receiver receiver = CommonFixture.createReceiver("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfo shippingInfo = CommonFixture.createShippingInfo(address, receiver);
            AddressBook addressBook = AddressBookFixture.createAddressBook(userId, shippingInfo);
            given(addressBookRepository.findByIdAndUserId(addressBookId, userId)).willReturn(
                    Optional.ofNullable(addressBook));

            // when
            addressBookService.deleteAddressBook(addressBookId, userId);

            // then
            then(addressBookRepository).should().delete(any(AddressBook.class));
        }

        @Test
        @DisplayName("실패 - 주소록이 존재하지 않으면 예외 발생")
        void deleteAddressBook_throwException_IfAddressBookNotExists() {
            // given
            Long userId = 1L;
            Long addressBookId = 1L;
            given(addressBookRepository.findByIdAndUserId(addressBookId, userId)).willReturn(
                    Optional.empty());

            // when & then
            assertThatThrownBy(() -> addressBookService.deleteAddressBook(addressBookId, userId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND);
        }
    }
}
