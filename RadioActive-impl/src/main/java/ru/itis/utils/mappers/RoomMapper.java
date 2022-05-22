package ru.itis.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.RoomSearchResponse;
import ru.itis.models.Room;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "title", source = "room.title")
    @Mapping(target = "ownerNickname", source = "room.owner.nickname")
    @Mapping(target = "subscriberAmount", expression = "java(room.getSubscribers().size())")
    @Mapping(target = "streaming", source = "room.streaming")
    @Mapping(target = "listenerAmount", expression = "java(room.getListeners().size())")
    RoomSearchResponse toRoomSearchResponse(Room room);

    List<RoomSearchResponse> toRoomSearchResponse(List<Room> rooms);
}
