package ru.itis.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.RoomResponse;
import ru.itis.dto.response.RoomSearchResponse;
import ru.itis.models.Room;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class, UserMapper.class})
public interface RoomMapper {

    @Mapping(target = "ownerNickname", source = "room.owner.nickname")
    @Mapping(target = "subscriberAmount", source = "room.subscribers", qualifiedByName = "toAmount")
    @Mapping(target = "listenerAmount", source = "room.listeners", qualifiedByName = "toAmount")
    RoomSearchResponse toRoomSearchResponse(Room room);

    List<RoomSearchResponse> toRoomSearchResponse(List<Room> rooms);

    @Mapping(target = "ownerId", source = "room.owner", qualifiedByName = "toUuid")
    @Mapping(target = "chatId", source = "room.chat", qualifiedByName = "toUuid")
    @Mapping(target = "admins", source = "room.admins", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "listeners", source = "room.listeners", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "bannedUsers", source = "room.bannedUsers", qualifiedByName = "toUuidFromSet")
    RoomResponse toRoomResponse(Room room);
}
