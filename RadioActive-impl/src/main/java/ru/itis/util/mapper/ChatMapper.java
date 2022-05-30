package ru.itis.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.ChatResponse;
import ru.itis.model.ChatEntity;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class})
public interface ChatMapper {

    @Mapping(target = "ownerId", source = "chatEntity.owner", qualifiedByName = "toUuid")
    @Mapping(target = "roomId", source = "chatEntity.roomEntity", qualifiedByName = "toUuid")
    @Mapping(target = "moderators", source = "chatEntity.moderators", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "mutedUserEntities", source = "chatEntity.mutedUserEntities", qualifiedByName = "toUuidFromSet")
    ChatResponse toChatResponse(ChatEntity chatEntity);
}
