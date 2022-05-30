package ru.itis.service;

import ru.itis.dto.request.search.RoomSearchRequest;
import ru.itis.dto.request.search.UserSearchRequest;
import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;

public interface SearchService {
    RoomPageSearchResponse searchRoomsByTitleOrOwner(RoomSearchRequest roomSearchRequest);

    UserPageSearchResponse searchUserByNickname(UserSearchRequest userSearchRequest);
}
