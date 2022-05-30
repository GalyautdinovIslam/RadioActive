package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.RoomExtendedRequest;
import ru.itis.dto.request.RoomRequest;
import ru.itis.dto.response.RoomResponse;
import ru.itis.exceptions.AudioNotFoundException;
import ru.itis.exceptions.ChatNotFoundException;
import ru.itis.exceptions.RoomNotFoundException;
import ru.itis.exceptions.UserNotFoundException;
import ru.itis.models.AudioInfo;
import ru.itis.models.Chat;
import ru.itis.models.Room;
import ru.itis.models.User;
import ru.itis.repositories.AudioInfoRepository;
import ru.itis.repositories.ChatRepository;
import ru.itis.repositories.RoomRepository;
import ru.itis.repositories.UserRepository;
import ru.itis.services.RoomService;
import ru.itis.utils.mappers.RoomMapper;
import ru.itis.utils.ws.MessageDestinationUtil;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    @Value("${audios.storage.path}")
    private String storagePath;

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final AudioInfoRepository audioInfoRepository;
    private final RoomMapper roomMapper;

    private final SimpMessagingTemplate messagingTemplate;

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

    @Override
    public RoomResponse addAudio(UUID roomId, MultipartFile audio) {
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
            String extension = Objects.requireNonNull(audio.getOriginalFilename())
                    .substring(audio.getOriginalFilename().lastIndexOf("."));
            String storagePhotoName = UUID.randomUUID() + extension;
            AudioInfo audioInfo = AudioInfo.builder()
                    .type(audio.getContentType())
                    .originalName(audio.getOriginalFilename())
                    .storageName(storagePhotoName)
                    .size(audio.getSize())
                    .room(room)
                    .build();
            Files.copy(audio.getInputStream(), Paths.get(storagePath, audioInfo.getStorageName()));
            audioInfoRepository.save(audioInfo);
            if (!room.isStreaming()) {
                sendAudiosRunnable(room.getId()).start();
            }
            return roomMapper.toRoomResponse(room);
        } catch (IOException exception) {
            throw new AudioNotFoundException();
        }
    }

    private Thread sendAudiosRunnable(UUID roomId) {
        return new Thread(() -> {
            Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
            room.setStreaming(true);
            roomRepository.save(room);
            Optional<AudioInfo> optional = audioInfoRepository.findFirstByRoom_IdOrderByCreateDateAsc(roomId);
            while (optional.isPresent()) {
                AudioInfo audioInfo = optional.get();

                messagingTemplate.convertAndSend(MessageDestinationUtil.getRoomDestination(roomId),
                        audioInfo.getStorageName());
                try {
                    File file = new File(storagePath + "\\" + audioInfo.getStorageName());
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    AudioFormat audioFormat = audioInputStream.getFormat();
                    long frames = audioInputStream.getFrameLength();
                    double durations = (frames + 0.0) / audioFormat.getFrameRate();
                    Thread.sleep((long) (durations * 1000));
                } catch (UnsupportedAudioFileException | IOException | InterruptedException ignored) {
                }
                audioInfoRepository.delete(audioInfo);
                optional = audioInfoRepository.findFirstByRoom_IdOrderByCreateDateAsc(roomId);
            }
            room.setStreaming(false);
            roomRepository.save(room);
        });
    }
}
