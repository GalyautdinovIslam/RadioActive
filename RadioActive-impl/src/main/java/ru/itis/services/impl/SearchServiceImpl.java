package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;
import ru.itis.models.Room;
import ru.itis.models.User;
import ru.itis.repositories.RoomRepository;
import ru.itis.repositories.UserRepository;
import ru.itis.services.SearchService;
import ru.itis.utils.mappers.RoomMapper;
import ru.itis.utils.mappers.UserMapper;

import static ru.itis.constants.RadioActiveConstants.defaultSearchPageSize;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    @Override
    public RoomPageSearchResponse searchRoomsByTitleOrOwner(String search, Integer page,
                                                            String streaming, String password,
                                                            String sort) {
        search = "%" + search + "%";
        PageRequest pageRequest;
        Page<Room> rooms;
        boolean notStreamingCondition = streaming == null || !(streaming.equals("true") || streaming.equals("false"));
        boolean notPasswordCondition = password == null || !(password.equals("true") || password.equals("false"));
        if (sort != null && sort.equals("title")) {
            pageRequest = PageRequest.of(page, defaultSearchPageSize, Sort.by(sort));
        } else {
            pageRequest = PageRequest.of(page, defaultSearchPageSize);
        }
        if (notStreamingCondition && notPasswordCondition) {
            rooms = roomRepository.findAllByTitleLikeOrOwner_NicknameLike(search, search, pageRequest);
        } else if (notStreamingCondition) {
            if (password.equals("false")) {
                rooms = roomRepository.findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNull(search, search, pageRequest);
            } else {
                rooms = roomRepository.findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNotNull(search, search, pageRequest);
            }
        } else if (notPasswordCondition) {
            rooms = roomRepository.findAllByTitleLikeOrOwner_NicknameLikeAndStreaming(search, search,
                    streaming.equals("true"), pageRequest);
        } else {
            if (password.equals("false")) {
                rooms = roomRepository.findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNullAndStreaming(search, search,
                        streaming.equals("true"), pageRequest);
            } else {
                rooms = roomRepository.findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNotNullAndStreaming(search, search,
                        streaming.equals("true"), pageRequest);
            }
        }
        return RoomPageSearchResponse.builder()
                .rooms(roomMapper.toRoomSearchResponse(rooms.getContent()))
                .totalPage(rooms.getTotalPages())
                .build();
    }

    @Override
    public UserPageSearchResponse searchUserByNickname(String search, Integer page, String sort) {
        search = "%" + search + "%";
        PageRequest pageRequest;
        if (sort != null && sort.equals("nickname")) {
            pageRequest = PageRequest.of(page, defaultSearchPageSize, Sort.by(sort));
        } else {
            pageRequest = PageRequest.of(page, defaultSearchPageSize);
        }
        Page<User> users = userRepository.findAllByNicknameLike(search, pageRequest);
        return UserPageSearchResponse.builder()
                .users(userMapper.toUserSearchResponse(users.getContent()))
                .totalPage(users.getTotalPages())
                .build();
    }
}
