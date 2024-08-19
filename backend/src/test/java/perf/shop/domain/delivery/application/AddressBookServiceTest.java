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
import perf.shop.global.error.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] AddressBookService")
class AddressBookServiceTest {

    @InjectMocks
    AddressBookService addressBookService;

    @Mock
    AddressBookRepository addressBookRepository;

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

    AddressBookRequest createAddressBookRequest(ShippingInfoRequest shippingInfo) {
        return AddressBookRequest.builder()
                .shippingInfo(shippingInfo)
                .build();
    }

    Address createAddress(String roadAddress, String addressDetail, String zipcode) {
        return Address.builder()
                .roadAddress(roadAddress)
                .addressDetail(addressDetail)
                .zipcode(zipcode)
                .build();
    }

    Receiver createReceiver(String receiverName, String receiverPhone, String requestMessage) {
        return Receiver.builder()
                .receiverName(receiverName)
                .receiverPhone(receiverPhone)
                .requestMessage(requestMessage)
                .build();
    }

    ShippingInfo createShippingInfo(Address address, Receiver receiver) {
        return ShippingInfo.builder()
                .address(address)
                .receiver(receiver)
                .build();
    }

    AddressBook createAddressBook(Long userId, ShippingInfo shippingInfo) {
        return AddressBook.builder()
                .userId(userId)
                .shippingInfo(shippingInfo)
                .build();
    }

    @Nested
    @DisplayName("주소록 조회 테스트")
    class GetAddressBook {

        @Test
        @DisplayName("성공 - 주소록이 존재하지 않아도 빈 리스트를 반환하며 예외가 발생하지 않음")
        void getAddressBook_success() {
            // given
            Long userId = 1L;
            given(addressBookRepository.findAllByUserId(1L)).willReturn(List.of());

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
            AddressRequest address = createAddressRequest("", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfoRequest = createShippingInfoRequest(address, receiver);
            AddressBookRequest dto = createAddressBookRequest(shippingInfoRequest);

            ShippingInfo shippingInfo = ShippingInfo.from(shippingInfoRequest);
            AddressBook addressBook = AddressBook.of(userId, shippingInfo);
            given(addressBookRepository.save(any(AddressBook.class))).willReturn(addressBook);

            // when
            addressBookService.saveAddressBook(dto, 1L);

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
            AddressRequest addressRequest = createAddressRequest("변경된 주소", "주소", "12345");
            ReceiverRequest receiverRequest = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfoRequest = createShippingInfoRequest(addressRequest, receiverRequest);
            AddressBookRequest dto = createAddressBookRequest(shippingInfoRequest);

            Address address = createAddress("서울시 강남구", "주소", "12345");
            Receiver receiver = createReceiver("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfo shippingInfo = createShippingInfo(address, receiver);
            AddressBook addressBook = createAddressBook(userId, shippingInfo);
            given(addressBookRepository.findByIdAndUserId(addressBookId, userId)).willReturn(
                    Optional.ofNullable(addressBook));

            // when
            addressBookService.updateAddressBook(addressBookId, dto, userId);

            // then
            assertThat(addressBook.getShippingInfo().getAddress().getRoadAddress()).isEqualTo("변경된 주소");
        }

        @Test
        @DisplayName("실패 - 주소록이 존재하지 않으면 예외 발생")
        void updateAddressBook_throwException_IfAddressBoookNotExists() {
            // given
            Long addressBookId = 1L;
            Long userId = 1L;
            AddressRequest addressRequest = createAddressRequest("변경된 주소", "주소", "12345");
            ReceiverRequest receiverRequest = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfoRequest = createShippingInfoRequest(addressRequest, receiverRequest);
            AddressBookRequest dto = createAddressBookRequest(shippingInfoRequest);

            given(addressBookRepository.findByIdAndUserId(addressBookId, userId)).willReturn(
                    Optional.empty());

            // when & then
            assertThatThrownBy(() -> addressBookService.updateAddressBook(addressBookId, dto, userId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_BOOK_NOT_FOUND);
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
            AddressBook addressBook = createAddressBook(userId, null);
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
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_BOOK_NOT_FOUND);
        }
    }
}
