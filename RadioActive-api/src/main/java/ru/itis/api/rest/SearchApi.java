package ru.itis.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.itis.dto.request.search.RoomSearchRequest;
import ru.itis.dto.request.search.UserSearchRequest;
import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping("api/v1/search")
public interface SearchApi {

    @GetMapping(value = "/rooms", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<RoomPageSearchResponse> searchRoomsByTitleOrOwner(@RequestBody RoomSearchRequest roomSearchRequest);

    @GetMapping(value = "/users", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UserPageSearchResponse> searchUserByNickname(@RequestBody UserSearchRequest userSearchRequest);
}
