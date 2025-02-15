package perf.shop.domain.delivery.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.delivery.application.AddressBookService;
import perf.shop.domain.delivery.dto.request.AddressBookRequest;
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

    @PostMapping
    public ApiResponse<Void> saveAddressBook(@RequestBody @Valid AddressBookRequest addressBookRequest,
                                             @UserId Long userId) {
        addressBookService.saveAddressBook(addressBookRequest, userId);
        return ApiResponse.of(ResponseCode.CREATED);
    }

    @PutMapping("/{addressBookId}")
    public ApiResponse<Void> updateAddressBook(@PathVariable("addressBookId") Long addressBookId,
                                               @RequestBody @Valid AddressBookRequest addressBookRequest,
                                               @UserId Long userId) {
        addressBookService.updateAddressBook(addressBookId, addressBookRequest, userId);
        return ApiResponse.of(ResponseCode.UPDATED);
    }

    @DeleteMapping("/{addressBookId}")
    public ApiResponse<Void> deleteAddressBook(@PathVariable("addressBookId") Long addressBookId,
                                               @UserId Long userId) {
        addressBookService.deleteAddressBook(addressBookId, userId);
        return ApiResponse.of(ResponseCode.DELETED);
    }
}
