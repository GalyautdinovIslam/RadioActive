package ru.itis.services;

import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;

public interface SearchService {
    RoomPageSearchResponse searchRoomsByTitleOrOwner(String search, Integer page,
                                                     String streaming, String password,
                                                     String sort);

    UserPageSearchResponse searchUserByNickname(String search, Integer page, String sort);
}
