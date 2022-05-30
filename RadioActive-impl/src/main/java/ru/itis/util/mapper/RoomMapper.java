package ru.itis.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.RoomResponse;
import ru.itis.dto.response.RoomSearchResponse;
import ru.itis.model.RoomEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class, UserMapper.class})
public interface RoomMapper {

    @Mapping(target = "ownerNickname", source = "roomEntity.owner.nickname")
    @Mapping(target = "subscriberAmount", source = "roomEntity.subscribers", qualifiedByName = "toAmount")
    @Mapping(target = "listenerAmount", source = "roomEntity.listenerEntities", qualifiedByName = "toAmount")
    RoomSearchResponse toRoomSearchResponse(RoomEntity roomEntity);

    List<RoomSearchResponse> toRoomSearchResponse(List<RoomEntity> roomEntities);

    @Mapping(target = "ownerId", source = "roomEntity.owner", qualifiedByName = "toUuid")
    @Mapping(target = "chatId", source = "roomEntity.chatEntity", qualifiedByName = "toUuid")
    @Mapping(target = "admins", source = "roomEntity.admins", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "listenerEntities", source = "roomEntity.listenerEntities", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "bannedUserEntities", source = "roomEntity.bannedUserEntities", qualifiedByName = "toUuidFromSet")
    RoomResponse toRoomResponse(RoomEntity roomEntity);
}
