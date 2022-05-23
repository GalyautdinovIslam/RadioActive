package ru.itis.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.ChatResponse;
import ru.itis.models.Chat;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class})
public interface ChatMapper {

    @Mapping(target = "ownerId", source = "chat.owner", qualifiedByName = "toUuid")
    @Mapping(target = "roomId", source = "chat.room", qualifiedByName = "toUuid")
    @Mapping(target = "moderators", source = "chat.moderators", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "mutedUsers", source = "chat.mutedUsers", qualifiedByName = "toUuidFromSet")
    ChatResponse toChatResponse(Chat chat);
}
