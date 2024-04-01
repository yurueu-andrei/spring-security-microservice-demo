package ru.clevertec.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.security.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User u left join fetch u.roles r left join fetch r.authorities where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

}
