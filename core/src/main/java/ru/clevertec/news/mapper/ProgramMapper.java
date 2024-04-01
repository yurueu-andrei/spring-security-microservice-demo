package ru.clevertec.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.news.dto.ProgramRequest;
import ru.clevertec.news.dto.ProgramResponse;
import ru.clevertec.news.entity.Program;

@Mapper(componentModel = "SPRING", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProgramMapper {

    @Mapping(target = "parent", source = "parentId", qualifiedByName = "mapParentIdToParent")
    Program toEntity(ProgramRequest goodDto);

    @Mapping(target = "parentId", source = "parent", qualifiedByName = "mapParentToParentId")
    ProgramResponse toDto(Program good);

    @Named("mapParentIdToParent")
    default Program mapParentIdToParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        Program program = new Program();
        program.setId(parentId);
        return program;
    }

    @Named("mapParentToParentId")
    default Long mapParentToParentId(Program program) {
        if (program == null) {
            return null;
        }
        return program.getId();
    }

}
