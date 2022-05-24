package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.RoomExtendedRequest;
import ru.itis.dto.request.RoomRequest;
import ru.itis.dto.response.RoomResponse;
import ru.itis.exceptions.ChatNotFoundException;
import ru.itis.exceptions.RoomNotFoundException;
import ru.itis.exceptions.UserNotFoundException;
import ru.itis.models.Chat;
import ru.itis.models.Room;
import ru.itis.models.User;
import ru.itis.repositories.ChatRepository;
import ru.itis.repositories.RoomRepository;
import ru.itis.repositories.UserRepository;
import ru.itis.services.RoomService;
import ru.itis.utils.mappers.RoomMapper;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final RoomMapper roomMapper;

    @Override
    public UUID createRoom(RoomRequest roomRequest) {
        User owner = userRepository.findById(roomRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        Chat chat = Chat.builder()
                .owner(owner)
                .build();
        chat = chatRepository.save(chat);
        Room room = Room.builder()
                .title(roomRequest.getTitle())
                .password(roomRequest.getPassword())
                .owner(owner)
                .chat(chat)
                .build();
        chat.setRoom(room);
        room = roomRepository.save(room);
        return room.getId();
    }

    @Override
    public RoomResponse getRoom(UUID roomId) {
        return roomMapper.toRoomResponse(roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new));
    }

    @Override
    public RoomResponse updateRoom(UUID roomId, RoomExtendedRequest roomExtendedRequest) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        User owner = userRepository.findById(roomExtendedRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        Chat chat = chatRepository.findById(roomExtendedRequest.getChatId()).orElseThrow(ChatNotFoundException::new);
        room.setTitle(roomExtendedRequest.getTitle());
        room.setPassword(roomExtendedRequest.getPassword());
        room.setOwner(owner);
        room.setChat(chat);
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(UUID roomId) {
        roomRepository.delete(roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new));
    }

    @Override
    public RoomResponse addAdmin(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        room.getAdmins().add(user);
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse deleteAdmin(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        room.getAdmins().remove(user);
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse banUser(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        room.getBannedUsers().add(user);
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse unbanUser(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        room.getBannedUsers().remove(user);
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }
}
