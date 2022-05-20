package ru.itis.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.SearchApi;
import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;
import ru.itis.services.SearchService;

@RequiredArgsConstructor
@RestController
public class SearchController implements SearchApi {

    private final SearchService searchService;

    @Override
    public ResponseEntity<RoomPageSearchResponse> searchRoomsByTitleOrOwner(String search,  Integer page,
                                                                            String streaming, String password,
                                                                            String sort) {
        return ResponseEntity.ok(searchService.searchRoomsByTitleOrOwner(search, page, streaming, password, sort));
    }

    @Override
    public ResponseEntity<UserPageSearchResponse> searchUserByNickname(String search, Integer page, String sort) {
        return ResponseEntity.ok(searchService.searchUserByNickname(search, page, sort));
    }
}
