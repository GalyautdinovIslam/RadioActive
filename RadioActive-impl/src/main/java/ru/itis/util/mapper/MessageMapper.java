package ru.itis.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.MessageResponse;
import ru.itis.model.MessageEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class})
public interface MessageMapper {

    @Mapping(target = "chatId", source = "messageEntity.chatEntity", qualifiedByName = "toUuid")
    @Mapping(target = "senderId", source = "messageEntity.sender", qualifiedByName = "toUuid")
    MessageResponse toMessageResponse(MessageEntity messageEntity);

    List<MessageResponse> toMessageResponse(List<MessageEntity> messageEntities);
}
