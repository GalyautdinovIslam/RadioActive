package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.search.RoomSearchRequest;
import ru.itis.dto.request.search.UserSearchRequest;
import ru.itis.dto.response.RoomPageSearchResponse;
import ru.itis.dto.response.UserPageSearchResponse;
import ru.itis.filters.specifications.RoomSpecifications;
import ru.itis.filters.specifications.UserSpecifications;
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
    private final RoomSpecifications roomSpecifications;
    private final UserSpecifications userSpecifications;

    @Override
    public RoomPageSearchResponse searchRoomsByTitleOrOwner(RoomSearchRequest roomSearchRequest) {
        String sort = roomSearchRequest.getSort();
        Integer pageSize = roomSearchRequest.getPageSize();
        pageSize = pageSize != null ? pageSize : defaultSearchPageSize;

        PageRequest pageRequest = PageRequest.of(roomSearchRequest.getPage(), pageSize);
        if (sort != null && sort.equals("title"))
            pageRequest = pageRequest.withSort(Sort.by(sort));

        String search = roomSearchRequest.getSearch();
        Page<Room> rooms = roomRepository.findAll(
                Specification.where(roomSpecifications.hasPassword(roomSearchRequest.getPassword()))
                        .and(roomSpecifications.isStreaming(roomSearchRequest.getStreaming()))
                        .and(roomSpecifications.like("title", search)
                                .or(roomSpecifications.like("owner.nickname", search))),
                pageRequest);

        return RoomPageSearchResponse.builder()
                .rooms(roomMapper.toRoomSearchResponse(rooms.getContent()))
                .totalPage(rooms.getTotalPages())
                .build();
    }

    @Override
    public UserPageSearchResponse searchUserByNickname(UserSearchRequest userSearchRequest) {
        String sort = userSearchRequest.getSort();
        Integer pageSize = userSearchRequest.getPageSize();
        pageSize = pageSize != null ? pageSize : defaultSearchPageSize;

        PageRequest pageRequest = PageRequest.of(userSearchRequest.getPage(), pageSize);
        if (sort != null && sort.equals("nickname"))
            pageRequest = pageRequest.withSort(Sort.by(sort));

        String search = userSearchRequest.getSearch();

        Page<User> users = userRepository.findAll(
                Specification.where(userSpecifications.like("nickname", search)),
                pageRequest);

        return UserPageSearchResponse.builder()
                .users(userMapper.toUserSearchResponse(users.getContent()))
                .totalPage(users.getTotalPages())
                .build();
    }
}
