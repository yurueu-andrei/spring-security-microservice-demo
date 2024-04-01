package ru.clevertec.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.news.entity.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {

}
