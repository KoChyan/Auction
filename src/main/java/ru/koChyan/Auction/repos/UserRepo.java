package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koChyan.Auction.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);

    User findByEmail(String email);
}
