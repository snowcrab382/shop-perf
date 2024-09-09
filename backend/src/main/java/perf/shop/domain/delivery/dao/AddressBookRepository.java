package perf.shop.domain.delivery.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.delivery.domain.AddressBook;

public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
    List<AddressBook> findAllByUserId(Long userId);

    Optional<AddressBook> findByIdAndUserId(Long addressBookId, Long userId);
}
