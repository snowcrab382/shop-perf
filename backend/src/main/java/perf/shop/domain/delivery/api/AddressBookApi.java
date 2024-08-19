package perf.shop.domain.delivery.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.delivery.application.AddressBookService;
import perf.shop.domain.delivery.dto.response.AddressBookResponse;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/address-book")
@RequiredArgsConstructor
public class AddressBookApi {

    private final AddressBookService addressBookService;

    @GetMapping
    public ApiResponse<List<AddressBookResponse>> getAddressBook(@UserId Long userId) {
        return ApiResponse.of(ResponseCode.GET, addressBookService.findAllByUserId(userId));
    }
}
