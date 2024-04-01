package ru.clevertec.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.ProgramRequest;
import ru.clevertec.news.dto.ProgramResponse;
import ru.clevertec.news.entity.Program;
import ru.clevertec.news.exception.CustomException;
import ru.clevertec.news.mapper.ProgramMapper;
import ru.clevertec.news.repository.ProgramRepository;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

    @Transactional(readOnly = true)
    public ProgramResponse findById(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new CustomException("There is no such program", HttpStatus.NOT_FOUND));
        return programMapper.toDto(program);
    }

    @Transactional(readOnly = true)
    public List<ProgramResponse> findAll(Pageable pageable) {
        try {
            return programRepository.findAll(pageable).map(programMapper::toDto).toList();
        } catch (Exception ex) {
            throw new CustomException("Error during finding all programs", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ProgramResponse save(ProgramRequest programRequest) {
        try {
            Program entity = programMapper.toEntity(programRequest);
            return programMapper.toDto(programRepository.save(entity));
        } catch (Exception ex) {
            throw new CustomException("Error during saving program", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void update(Long id, ProgramRequest programRequest) {
        try {
            Program entity = programRepository.findById(id).orElseThrow(() -> new CustomException("There is no such user", HttpStatus.NOT_FOUND));
            Program program = programMapper.toEntity(programRequest);
            updateProgram(entity, program);
            programRepository.save(entity);
        } catch (Exception ex) {
            throw new CustomException("Error during updating user", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            programRepository.deleteById(id);
        } catch (Exception ex) {
            throw new CustomException("Error during updating user", HttpStatus.BAD_REQUEST);
        }
    }

    private void updateProgram(Program program, Program mappedProgram) {
        program.setName(mappedProgram.getName());
        program.setParent(mappedProgram.getParent());
    }

    // custom predicate
    public boolean isAllowedToRead(UserDetails userDetails, Long id) {
        return Arrays.asList(programRepository.findById(id)
                .orElseThrow(() -> new CustomException("There is no such program", HttpStatus.NOT_FOUND))
                .getUsernames()).contains(userDetails.getUsername());
    }

}
