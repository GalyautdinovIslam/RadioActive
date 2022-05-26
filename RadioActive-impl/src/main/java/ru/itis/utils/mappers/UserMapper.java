package ru.itis.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.itis.dto.response.UserResponse;
import ru.itis.dto.response.UserSearchResponse;
import ru.itis.models.AbstractEntity;
import ru.itis.models.User;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {AbstractMapper.class})
public interface UserMapper {

    @Mapping(target = "subscribersOfRoomsInOwnershipAmount", source = "user", qualifiedByName = "toSubscribersAmount")
    UserSearchResponse toUserSearchResponse(User user);

    List<UserSearchResponse> toUserSearchResponse(List<User> users);

    @Mapping(target = "uuidsOfRoomsInOwnership", source = "user.roomsInOwnership", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfRoomsInAdministration", source = "user.roomsInAdministration", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfChatsInOwnership", source = "user.chatsInOwnership", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfChatsInModeration", source = "user.chatsInModeration", qualifiedByName = "toUuidFromSet")
    @Mapping(target = "uuidsOfRoomsInFavorites", source = "user.roomsInFavorites", qualifiedByName = "toUuidFromSet")
    UserResponse toUserResponse(User user);

    @Named("toSubscribersAmount")
    default Integer toSubscribersAmount(User user) {
        return user.getRoomsInOwnership().stream().mapToInt(x -> x.getSubscribers().size()).sum();
    }

    @Named("toAmount")
    default Integer toAmount(Set<? extends AbstractEntity> entities) {
        return entities.size();
    }
}
