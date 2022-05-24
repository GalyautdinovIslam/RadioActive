package ru.itis.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.MessageResponse;
import ru.itis.models.Message;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class})
public interface MessageMapper {

    @Mapping(target = "chatId", source = "message.chat", qualifiedByName = "toUuid")
    @Mapping(target = "senderId", source = "message.sender", qualifiedByName = "toUuid")
    MessageResponse toMessageResponse(Message message);
}
