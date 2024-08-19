package perf.shop.domain.delivery.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.delivery.domain.AddressBook;
import perf.shop.domain.delivery.dto.response.AddressBookResponse;
import perf.shop.domain.delivery.repository.AddressBookRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;

    public List<AddressBookResponse> findAllByUserId(Long userId) {
        List<AddressBook> addresses = addressBookRepository.findAllByUserId(userId);
        return addresses.stream()
                .map(AddressBookResponse::from)
                .toList();
    }
}
