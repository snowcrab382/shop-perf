package perf.shop.domain.delivery.dto.response;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.delivery.domain.AddressBook;
import perf.shop.domain.model.ShippingInfo;

@Getter
public class AddressBookResponse {

    private Long addressBookId;
    private ShippingInfo shippingInfo;

    @Builder
    private AddressBookResponse(Long addressBookId, ShippingInfo shippingInfo) {
        this.addressBookId = addressBookId;
        this.shippingInfo = shippingInfo;
    }

    public static AddressBookResponse from(AddressBook addressBook) {
        return AddressBookResponse.builder()
                .addressBookId(addressBook.getId())
                .shippingInfo(addressBook.getShippingInfo())
                .build();
    }
}
