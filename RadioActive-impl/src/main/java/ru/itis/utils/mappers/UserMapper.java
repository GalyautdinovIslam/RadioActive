package ru.itis.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.dto.response.UserSearchResponse;
import ru.itis.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "nickname", source = "user.nickname")
    @Mapping(target = "subscribersOfRoomsInOwnershipAmount", expression = "java(user.getRoomsInOwnership()" +
            ".stream().mapToInt(x -> x.getSubscribers().size())" +
            ".sum())")
    UserSearchResponse toUserSearchResponse(User user);

    List<UserSearchResponse> toUserSearchResponse(List<User> users);
}
