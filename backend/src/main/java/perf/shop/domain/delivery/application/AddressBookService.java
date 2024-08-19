package perf.shop.domain.delivery.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.delivery.domain.AddressBook;
import perf.shop.domain.delivery.dto.request.AddressBookRequest;
import perf.shop.domain.delivery.dto.response.AddressBookResponse;
import perf.shop.domain.delivery.repository.AddressBookRepository;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;

    @Transactional(readOnly = true)
    public List<AddressBookResponse> findAllByUserId(Long userId) {
        List<AddressBook> addresses = addressBookRepository.findAllByUserId(userId);
        return addresses.stream()
                .map(AddressBookResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AddressBook findByIdAndUserId(Long addressBookId, Long userId) {
        return addressBookRepository.findByIdAndUserId(addressBookId, userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ADDRESS_BOOK_NOT_FOUND));
    }

    public void saveAddressBook(AddressBookRequest addressBookRequest, Long userId) {
        ShippingInfo shippingInfo = ShippingInfo.from(addressBookRequest.getShippingInfo());
        addressBookRepository.save(AddressBook.of(userId, shippingInfo));
    }

    public void updateAddressBook(Long addressBookId, AddressBookRequest addressBookRequest, Long userId) {
        AddressBook addressBook = findByIdAndUserId(addressBookId, userId);
        addressBook.updateShippingInfo(ShippingInfo.from(addressBookRequest.getShippingInfo()));
    }

    public void deleteAddressBook(Long addressBookId, Long userId) {
        AddressBook addressBook = findByIdAndUserId(addressBookId, userId);
        addressBookRepository.delete(addressBook);
    }
}
