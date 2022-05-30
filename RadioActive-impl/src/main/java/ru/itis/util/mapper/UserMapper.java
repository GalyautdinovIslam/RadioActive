package ru.itis.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.itis.dto.response.UserResponse;
import ru.itis.dto.response.UserSearchResponse;
import ru.itis.model.AbstractEntity;
import ru.itis.model.UserEntity;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class})
public interface UserMapper {

    @Mapping(target = "subscribersOfRoomsInOwnershipAmount", source = "userEntity", qualifiedByName = "toSubscribersAmount")
    UserSearchResponse toUserSearchResponse(UserEntity userEntity);

    List<UserSearchResponse> toUserSearchResponse(List<UserEntity> userEntities);

    @Mapping(target = "uuidsOfRoomsInOwnership", source = "userEntity.roomsInOwnership", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfRoomsInAdministration", source = "userEntity.roomsInAdministration", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfChatsInOwnership", source = "userEntity.chatsInOwnership", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfChatsInModeration", source = "userEntity.chatsInModeration", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfRoomsInFavorites", source = "userEntity.roomsInFavorites", qualifiedByName = "toUuidFromSet")
    UserResponse toUserResponse(UserEntity userEntity);

    @Named("toSubscribersAmount")
    default Integer toSubscribersAmount(UserEntity userEntity) {
        return userEntity.getRoomsInOwnership().stream().mapToInt(x -> x.getSubscribers().size()).sum();
    }

    @Named("toAmount")
    default Integer toAmount(Set<? extends AbstractEntity> entities) {
        return entities.size();
    }
}
