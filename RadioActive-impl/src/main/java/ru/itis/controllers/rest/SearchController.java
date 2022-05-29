package ru.itis.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.rest.SearchApi;
import ru.itis.dto.request.search.RoomSearchRequest;
import ru.itis.dto.request.search.UserSearchRequest;
import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;
import ru.itis.services.SearchService;

@RequiredArgsConstructor
@RestController
public class SearchController implements SearchApi {

    private final SearchService searchService;

    @Override
    public ResponseEntity<RoomPageSearchResponse> searchRoomsByTitleOrOwner(RoomSearchRequest roomSearchRequest) {
        return ResponseEntity.ok(searchService.searchRoomsByTitleOrOwner(roomSearchRequest));
    }

    @Override
    public ResponseEntity<UserPageSearchResponse> searchUserByNickname(UserSearchRequest userSearchRequest) {
        return ResponseEntity.ok(searchService.searchUserByNickname(userSearchRequest));
    }
}
