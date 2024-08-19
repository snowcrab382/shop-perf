package perf.shop.domain.delivery.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.delivery.application.AddressBookService;
import perf.shop.domain.delivery.dto.request.AddressBookSaveRequest;
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
    public ApiResponse<Void> saveAddressBook(@RequestBody @Valid AddressBookSaveRequest addressBookSaveRequest,
                                             @UserId Long userId) {
        addressBookService.saveAddressBook(addressBookSaveRequest, userId);
        return ApiResponse.of(ResponseCode.CREATED);
    }

    @PutMapping("/{addressBookId}")
    public ApiResponse<Void> updateAddressBook(@PathVariable("addressBookId") Long addressBookId,
                                               @RequestBody @Valid AddressBookSaveRequest addressBookSaveRequest,
                                               @UserId Long userId) {
        addressBookService.updateAddressBook(addressBookId, addressBookSaveRequest, userId);
        return ApiResponse.of(ResponseCode.UPDATED);
    }
}
