package ru.itis.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.itis.models.AbstractEntity;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AbstractMapper {

    @Named("toUuid")
    default UUID toUuid(AbstractEntity abstractEntity) {
        return abstractEntity.getId();
    }

    @Named("toUuidFromSet")
    default Set<UUID> toUuidFromSet(Set<? extends AbstractEntity> set) {
        return set.stream().map(AbstractEntity::getId).collect(Collectors.toSet());
    }
}
