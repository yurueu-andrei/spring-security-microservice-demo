package ru.clevertec.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.security.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
