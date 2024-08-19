package perf.shop.domain.delivery.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.delivery.domain.AddressBook;
import perf.shop.domain.delivery.dto.request.AddressBookSaveRequest;
import perf.shop.domain.delivery.repository.AddressBookRepository;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;

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
            AddressBookSaveRequest dto = createAddressBookSaveRequest(shippingInfoRequest);

            ShippingInfo shippingInfo = ShippingInfo.from(shippingInfoRequest);
            AddressBook addressBook = AddressBook.of(userId, shippingInfo);
            given(addressBookRepository.save(any(AddressBook.class))).willReturn(addressBook);

            // when
            addressBookService.saveAddressBook(dto, 1L);

            // then
            then(addressBookRepository).should().save(any(AddressBook.class));
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

        AddressBookSaveRequest createAddressBookSaveRequest(ShippingInfoRequest shippingInfo) {
            return AddressBookSaveRequest.builder()
                    .shippingInfo(shippingInfo)
                    .build();
        }
    }

}
