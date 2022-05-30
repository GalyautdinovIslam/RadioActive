package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.UserExtendedRequest;
import ru.itis.dto.request.UserRequest;
import ru.itis.dto.response.UserResponse;
import ru.itis.exceptions.RadioUnauthorizedException;
import ru.itis.exceptions.RoomNotFoundException;
import ru.itis.exceptions.UserNotFoundException;
import ru.itis.models.Room;
import ru.itis.models.User;
import ru.itis.repositories.RoomRepository;
import ru.itis.repositories.UserRepository;
import ru.itis.services.UserService;
import ru.itis.utils.mappers.UserMapper;

import javax.transaction.Transactional;
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
        User user = User.builder()
                .email(userExtendedRequest.getEmail())
                .password(userExtendedRequest.getPassword())
                .nickname(userExtendedRequest.getNickname())
                .build();
        user = userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserResponse getUser(UUID userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponse updateUser(UUID userId, UserExtendedRequest userExtendedRequest) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setEmail(userExtendedRequest.getEmail());
        user.setPassword(userExtendedRequest.getPassword());
        user.setNickname(userExtendedRequest.getNickname());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.delete(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponse addRoomToFavorite(UUID userId, UUID roomId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        user.getRoomsInFavorites().add(room);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse deleteRoomFromFavorite(UUID userId, UUID roomId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        user.getRoomsInFavorites().remove(room);
        return userMapper.toUserResponse(userRepository.save(user));
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
