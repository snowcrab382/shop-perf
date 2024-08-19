package perf.shop.domain.delivery.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import perf.shop.domain.delivery.application.AddressBookService;
import perf.shop.domain.delivery.dto.request.AddressBookSaveRequest;
import perf.shop.domain.delivery.dto.response.AddressBookResponse;
import perf.shop.domain.model.Address;
import perf.shop.domain.model.Receiver;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.error.exception.ErrorCode;
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
            AddressBookResponse addressBookResponse = createAddressBookResponse(addressBookId, shippingInfo);
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

        AddressBookResponse createAddressBookResponse(Long addressBookId, ShippingInfo shippingInfo) {
            return AddressBookResponse.builder()
                    .addressBookId(addressBookId)
                    .shippingInfo(shippingInfo)
                    .build();
        }

    }

    @Nested
    @DisplayName("주소록 저장 API 테스트")
    class SaveAddressBook {

        ResultActions saveAddressBook(AddressBookSaveRequest dto) throws Exception {
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
            AddressBookSaveRequest dto = createAddressBookSaveRequest(shippingInfo);

            // when
            ResultActions resultActions = saveAddressBook(dto);

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
            AddressBookSaveRequest dto = createAddressBookSaveRequest(shippingInfo);

            // when
            ResultActions resultActions = saveAddressBook(dto);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())))
                    .andExpect(jsonPath("$.message", equalTo(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage())))
                    .andExpectAll(
                            jsonPath("$.errors").exists(),
                            jsonPath("$.errors[0].field", equalTo("shippingInfo.address.roadAddress")),
                            jsonPath("$.errors[0].value", equalTo("")),
                            jsonPath("$.errors[0].message", equalTo("must not be blank"))
                    );

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
