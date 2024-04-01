package ru.clevertec.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.security.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
