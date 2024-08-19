package perf.shop.domain.delivery.application;

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
import perf.shop.domain.delivery.repository.AddressBookRepository;

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

}
