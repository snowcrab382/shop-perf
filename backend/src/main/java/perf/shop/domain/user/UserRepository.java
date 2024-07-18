package perf.shop.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
