package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.UserExtendedRequest;
import ru.itis.dto.request.UserRequest;
import ru.itis.dto.response.UserResponse;
import ru.itis.exception.RadioUnauthorizedException;
import ru.itis.exception.RoomNotFoundException;
import ru.itis.exception.UserNotFoundException;
import ru.itis.model.RoomEntity;
import ru.itis.model.UserEntity;
import ru.itis.repository.RoomRepository;
import ru.itis.repository.UserRepository;
import ru.itis.service.UserService;
import ru.itis.util.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UUID createUser(UserExtendedRequest userExtendedRequest) {
        UserEntity userEntity = UserEntity.builder()
                .email(userExtendedRequest.getEmail())
                .password(userExtendedRequest.getPassword())
                .nickname(userExtendedRequest.getNickname())
                .build();
        userEntity = userRepository.save(userEntity);
        return userEntity.getId();
    }

    @Override
    public UserResponse getUser(UUID userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponse updateUser(UUID userId, UserExtendedRequest userExtendedRequest) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userEntity.setEmail(userExtendedRequest.getEmail());
        userEntity.setPassword(userExtendedRequest.getPassword());
        userEntity.setNickname(userExtendedRequest.getNickname());
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.delete(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponse addRoomToFavorite(UUID userId, UUID roomId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        userEntity.getRoomsInFavorites().add(roomEntity);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @Override
    public UserResponse deleteRoomFromFavorite(UUID userId, UUID roomId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        userEntity.getRoomsInFavorites().remove(roomEntity);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @Override
    public Optional<UserResponse> findBySubject(String subject) {
        return userRepository.findOneByNickname(subject)
                .map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse login(UserRequest userRequest) {
        return userRepository.findOneByEmail(userRequest.getEmail())
                .filter(user -> passwordEncoder.matches(userRequest.getPassword(), user.getPassword()))
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new RadioUnauthorizedException("Failed to login: " + userRequest.getEmail()));
    }
}
