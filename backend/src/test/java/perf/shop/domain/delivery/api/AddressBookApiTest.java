package perf.shop.domain.delivery.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static perf.shop.mock.fixtures.common.CommonFixture.createAddress;
import static perf.shop.mock.fixtures.common.CommonFixture.createAddressRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createReceiver;
import static perf.shop.mock.fixtures.common.CommonFixture.createReceiverRequest;
import static perf.shop.mock.fixtures.common.CommonFixture.createShippingInfo;
import static perf.shop.mock.fixtures.common.CommonFixture.createShippingInfoRequest;
import static perf.shop.mock.fixtures.delivery.AddressBookFixture.createAddressBookRequest;
import static perf.shop.mock.fixtures.delivery.AddressBookFixture.createAddressBookResponse;

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
import perf.shop.domain.delivery.application.AddressBookService;
import perf.shop.domain.delivery.dto.request.AddressBookRequest;
import perf.shop.domain.delivery.dto.response.AddressBookResponse;
import perf.shop.domain.model.Address;
import perf.shop.domain.model.Receiver;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;
import perf.shop.mock.InjectMockUser;

@InjectMockUser
@WebMvcTest(controllers = AddressBookApi.class)
@DisplayName("[단위 테스트] AddressBookApi")
class AddressBookApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AddressBookService addressBookService;

    @Nested
    @DisplayName("주소록 조회 API 테스트")
    class GetAddressBook {

        ResultActions getAddressBook() throws Exception {
            return mockMvc.perform(get("/address-book")
                    .with(csrf())
                    .accept(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("성공")
        void getAddressBook_success() throws Exception {
            // given
            Long addressBookId = 1L;
            Address address = createAddress("서울시 강남구", "주소", "12345");
            Receiver receiver = createReceiver("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfo shippingInfo = createShippingInfo(address, receiver);
            AddressBookResponse addressBookResponse = createAddressBookResponse(addressBookId,
                    shippingInfo);
            given(addressBookService.findAllByUserId(addressBookId)).willReturn(List.of(addressBookResponse));

            // when
            ResultActions resultActions = getAddressBook();

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.GET.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.GET.getMessage())))
                    .andExpectAll(
                            jsonPath("$.data").exists(),
                            jsonPath("$.data[0].addressBookId", equalTo(addressBookId.intValue())),
                            jsonPath("$.data[0].shippingInfo.address.roadAddress", equalTo("서울시 강남구")),
                            jsonPath("$.data[0].shippingInfo.address.addressDetail", equalTo("주소")),
                            jsonPath("$.data[0].shippingInfo.address.zipcode", equalTo("12345")),
                            jsonPath("$.data[0].shippingInfo.receiver.receiverName", equalTo("받는사람")),
                            jsonPath("$.data[0].shippingInfo.receiver.receiverPhone", equalTo("010-1234-5678")),
                            jsonPath("$.data[0].shippingInfo.receiver.requestMessage", equalTo("부재시 연락주세요"))
                    );
        }
    }

    @Nested
    @DisplayName("주소록 저장 API 테스트")
    class SaveAddressBook {

        ResultActions saveAddressBook(AddressBookRequest dto) throws Exception {
            return mockMvc.perform(post("/address-book")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        }

        @Test
        @DisplayName("성공")
        void saveAddressBook_success() throws Exception {
            // given
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            AddressBookRequest addressBookRequest = createAddressBookRequest(shippingInfo);

            // when
            ResultActions resultActions = saveAddressBook(addressBookRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.CREATED.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.CREATED.getMessage())));
        }

        @Test
        @DisplayName("실패 - 요청 값이 유효하지 않는 경우 예외 발생")
        void saveAddressBook_throwException_IfInputValueIsInvalid() throws Exception {
            // given
            AddressRequest address = createAddressRequest("", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            AddressBookRequest addressBookRequest = createAddressBookRequest(shippingInfo);

            // when
            ResultActions resultActions = saveAddressBook(addressBookRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors[0].field", equalTo("shippingInfo.address.roadAddress")),
                            jsonPath("$.errors[0].value", equalTo("")),
                            jsonPath("$.errors[0].message", equalTo("must not be blank"))
                    );

        }
    }

    @Nested
    @DisplayName("주소록 수정 API 테스트")
    class UpdateAddressBook {

        ResultActions updateAddressBook(Long addressBookId, AddressBookRequest dto) throws Exception {
            return mockMvc.perform(put("/address-book/{addressBookId}", addressBookId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        }

        @Test
        @DisplayName("성공")
        void updateAddressBook_success() throws Exception {
            // given
            Long addressBookId = 1L;
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            AddressBookRequest addressBookRequest = createAddressBookRequest(shippingInfo);

            // when
            ResultActions resultActions = updateAddressBook(addressBookId, addressBookRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.UPDATED.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.UPDATED.getMessage())))
                    .andExpect(jsonPath("$.data", equalTo(null)));
        }

        @Test
        @DisplayName("실패 - 요청 값이 유효하지 않은 경우 예외 발생")
        void updateAddressBook_throwException_IfInputValueIsInvalid() throws Exception {
            // given
            Long addressBookId = 1L;
            AddressRequest address = createAddressRequest("", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            AddressBookRequest addressBookRequest = createAddressBookRequest(shippingInfo);

            // when
            ResultActions resultActions = updateAddressBook(addressBookId, addressBookRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors[0].field", equalTo("shippingInfo.address.roadAddress")),
                            jsonPath("$.errors[0].value", equalTo("")),
                            jsonPath("$.errors[0].message", equalTo("must not be blank"))
                    );
        }

        @Test
        @DisplayName("실패 - 주소록이 존재하지 않는 경우 예외 발생")
        void updateAddressBook_throwException_IfAddressBookNotExists() throws Exception {
            // given
            Long addressBookId = 1L;
            AddressRequest address = createAddressRequest("서울시 강남구", "주소", "12345");
            ReceiverRequest receiver = createReceiverRequest("받는사람", "010-1234-5678", "부재시 연락주세요");
            ShippingInfoRequest shippingInfo = createShippingInfoRequest(address, receiver);
            AddressBookRequest addressBookRequest = createAddressBookRequest(shippingInfo);
            doThrow(new EntityNotFoundException(GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND))
                    .when(addressBookService).updateAddressBook(anyLong(), any(AddressBookRequest.class), anyLong());

            // when
            ResultActions resultActions = updateAddressBook(addressBookId, addressBookRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND.getMessage())))
                    .andExpect(jsonPath("$.errors", equalTo(Collections.emptyList())));
        }

    }

    @Nested
    @DisplayName("주소록 삭제 API 테스트")
    class DeleteAddressBook {

        ResultActions deleteAddressBook(Long addressBookId) throws Exception {
            return mockMvc.perform(delete("/address-book/{addressBookId}", addressBookId)
                    .with(csrf()));
        }

        @Test
        @DisplayName("성공")
        void deleteAddressBook_success() throws Exception {
            // given
            Long addressBookId = 1L;

            // when
            ResultActions resultActions = deleteAddressBook(addressBookId);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ResponseCode.DELETED.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ResponseCode.DELETED.getMessage())))
                    .andExpect(jsonPath("$.data", equalTo(null)));
        }

        @Test
        @DisplayName("실패 - 주소록이 존재하지 않는 경우 예외 발생")
        void deleteAddressBook_throwException_IfAddressBookNotExists() throws Exception {
            // given
            Long addressBookId = 1L;
            doThrow(new EntityNotFoundException(GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND))
                    .when(addressBookService).deleteAddressBook(anyLong(), anyLong());

            // when
            ResultActions resultActions = deleteAddressBook(addressBookId);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(GlobalErrorCode.ADDRESS_BOOK_NOT_FOUND.getMessage())))
                    .andExpect(jsonPath("$.errors", equalTo(Collections.emptyList())));
        }

    }

}
