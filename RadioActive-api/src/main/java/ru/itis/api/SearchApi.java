package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping("api/v1/search")
public interface SearchApi {

    @GetMapping(value = "/rooms", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<RoomPageSearchResponse> searchRoomsByTitleOrOwner(@RequestParam String search,
                                                                     @RequestParam Integer page,
                                                                     @RequestParam(required = false) String streaming,
                                                                     @RequestParam(required = false) String password,
                                                                     @RequestParam(required = false) String sort);

    @GetMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UserPageSearchResponse> searchUserByNickname(@RequestParam String search,
                                                                @RequestParam Integer page,
                                                                @RequestParam(required = false) String sort);
}
