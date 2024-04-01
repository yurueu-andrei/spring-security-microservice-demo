package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news.dto.ProgramRequest;
import ru.clevertec.news.dto.ProgramResponse;
import ru.clevertec.news.service.ProgramService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/programs")
public class ProgramController {

    private final ProgramService programService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PROGRAM') && @programService.isAllowedToRead(principal, #id)")
    public ResponseEntity<ProgramResponse> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(programService.findById(id), HttpStatus.OK);
    }

    @GetMapping
//    @PreAuthorize("@programService.isAllowedToReadAll(principal.username)")
    public ResponseEntity<List<ProgramResponse>> getAll(Pageable pageable) {
        return new ResponseEntity<>(programService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping
//    @PreAuthorize("@programService.isAllowedToCreate(principal.username)")
    public ResponseEntity<ProgramResponse> create(@RequestBody ProgramRequest programRequest) {
        return new ResponseEntity<>(programService.save(programRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("@programService.isAllowedToUpdate(principal.username, #id)")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") Long id, @RequestBody ProgramRequest programRequest) {
        programService.update(id, programRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("@programService.isAllowedToDelete(principal.username)")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Long id) {
        programService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
