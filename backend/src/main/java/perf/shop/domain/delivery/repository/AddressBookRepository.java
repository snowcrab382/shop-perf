package perf.shop.domain.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.delivery.domain.AddressBook;

public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
    List<AddressBook> findAllByUserId(Long userId);
}
