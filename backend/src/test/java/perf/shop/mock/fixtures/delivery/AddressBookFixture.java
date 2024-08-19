package perf.shop.mock.fixtures.delivery;

import perf.shop.domain.delivery.domain.AddressBook;
import perf.shop.domain.delivery.dto.request.AddressBookRequest;
import perf.shop.domain.delivery.dto.response.AddressBookResponse;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;

public class AddressBookFixture {

    public static AddressBookRequest createAddressBookRequest(ShippingInfoRequest shippingInfoRequest) {
        return AddressBookRequest.builder()
                .shippingInfo(shippingInfoRequest)
                .build();
    }

    public static AddressBookResponse createAddressBookResponse(Long addressBookId, ShippingInfo shippingInfo) {
        return AddressBookResponse.builder()
                .addressBookId(addressBookId)
                .shippingInfo(shippingInfo)
                .build();
    }

    public static AddressBook createAddressBook(Long userId, ShippingInfo shippingInfo) {
        return AddressBook.builder()
                .userId(userId)
                .shippingInfo(shippingInfo)
                .build();
    }
}
