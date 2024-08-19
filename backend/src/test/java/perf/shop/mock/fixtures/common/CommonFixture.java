package perf.shop.mock.fixtures.common;

import perf.shop.domain.model.Address;
import perf.shop.domain.model.Receiver;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.model.dto.request.AddressRequest;
import perf.shop.domain.model.dto.request.ReceiverRequest;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;

public class CommonFixture {

    public static Address createAddress(String roadAddress, String addressDetail, String zipcode) {
        return Address.builder()
                .roadAddress(roadAddress)
                .addressDetail(addressDetail)
                .zipcode(zipcode)
                .build();
    }

    public static Receiver createReceiver(String receiverName, String receiverPhone, String requestMessage) {
        return Receiver.builder()
                .receiverName(receiverName)
                .receiverPhone(receiverPhone)
                .requestMessage(requestMessage)
                .build();
    }

    public static ShippingInfo createShippingInfo(Address address, Receiver receiver) {
        return ShippingInfo.builder()
                .address(address)
                .receiver(receiver)
                .build();
    }

    public static AddressRequest createAddressRequest(String roadAddress, String addressDetail, String zipcode) {
        return AddressRequest.builder()
                .roadAddress(roadAddress)
                .addressDetail(addressDetail)
                .zipcode(zipcode)
                .build();
    }

    public static ReceiverRequest createReceiverRequest(String receiverName, String receiverPhone,
                                                        String requestMessage) {
        return ReceiverRequest.builder()
                .receiverName(receiverName)
                .receiverPhone(receiverPhone)
                .requestMessage(requestMessage)
                .build();
    }

    public static ShippingInfoRequest createShippingInfoRequest(AddressRequest address, ReceiverRequest receiver) {
        return ShippingInfoRequest.builder()
                .address(address)
                .receiver(receiver)
                .build();
    }
}
