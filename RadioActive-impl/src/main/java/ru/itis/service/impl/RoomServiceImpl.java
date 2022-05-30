package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.RoomExtendedRequest;
import ru.itis.dto.request.RoomRequest;
import ru.itis.dto.response.RoomResponse;
import ru.itis.exception.AudioNotFoundException;
import ru.itis.exception.ChatNotFoundException;
import ru.itis.exception.RoomNotFoundException;
import ru.itis.exception.UserNotFoundException;
import ru.itis.model.AudioInfoEntity;
import ru.itis.model.ChatEntity;
import ru.itis.model.RoomEntity;
import ru.itis.model.UserEntity;
import ru.itis.repository.AudioInfoRepository;
import ru.itis.repository.ChatRepository;
import ru.itis.repository.RoomRepository;
import ru.itis.repository.UserRepository;
import ru.itis.service.RoomService;
import ru.itis.util.mapper.RoomMapper;
import ru.itis.util.ws.MessageDestinationUtil;

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
        UserEntity owner = userRepository.findById(roomRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        ChatEntity chatEntity = ChatEntity.builder()
                .owner(owner)
                .build();
        chatEntity = chatRepository.save(chatEntity);
        RoomEntity roomEntity = RoomEntity.builder()
                .title(roomRequest.getTitle())
                .password(roomRequest.getPassword())
                .owner(owner)
                .chatEntity(chatEntity)
                .build();
        chatEntity.setRoomEntity(roomEntity);
        roomEntity = roomRepository.save(roomEntity);
        return roomEntity.getId();
    }

    @Override
    public RoomResponse getRoom(UUID roomId) {
        return roomMapper.toRoomResponse(roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new));
    }

    @Override
    public RoomResponse updateRoom(UUID roomId, RoomExtendedRequest roomExtendedRequest) {
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        UserEntity owner = userRepository.findById(roomExtendedRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        ChatEntity chatEntity = chatRepository.findById(roomExtendedRequest.getChatId()).orElseThrow(ChatNotFoundException::new);
        roomEntity.setTitle(roomExtendedRequest.getTitle());
        roomEntity.setPassword(roomExtendedRequest.getPassword());
        roomEntity.setOwner(owner);
        roomEntity.setChatEntity(chatEntity);
        return roomMapper.toRoomResponse(roomRepository.save(roomEntity));
    }

    @Override
    public void deleteRoom(UUID roomId) {
        roomRepository.delete(roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new));
    }

    @Override
    public RoomResponse addAdmin(UUID roomId, UUID userId) {
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        roomEntity.getAdmins().add(userEntity);
        return roomMapper.toRoomResponse(roomRepository.save(roomEntity));
    }

    @Override
    public RoomResponse deleteAdmin(UUID roomId, UUID userId) {
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        roomEntity.getAdmins().remove(userEntity);
        return roomMapper.toRoomResponse(roomRepository.save(roomEntity));
    }

    @Override
    public RoomResponse banUser(UUID roomId, UUID userId) {
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        roomEntity.getBannedUserEntities().add(userEntity);
        return roomMapper.toRoomResponse(roomRepository.save(roomEntity));
    }

    @Override
    public RoomResponse unbanUser(UUID roomId, UUID userId) {
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        roomEntity.getBannedUserEntities().remove(userEntity);
        return roomMapper.toRoomResponse(roomRepository.save(roomEntity));
    }

    @Override
    public RoomResponse addAudio(UUID roomId, MultipartFile audio) {
        try {
            RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
            String extension = Objects.requireNonNull(audio.getOriginalFilename())
                    .substring(audio.getOriginalFilename().lastIndexOf("."));
            String storagePhotoName = UUID.randomUUID() + extension;
            AudioInfoEntity audioInfoEntity = AudioInfoEntity.builder()
                    .type(audio.getContentType())
                    .originalName(audio.getOriginalFilename())
                    .storageName(storagePhotoName)
                    .size(audio.getSize())
                    .roomEntity(roomEntity)
                    .build();
            Files.copy(audio.getInputStream(), Paths.get(storagePath, audioInfoEntity.getStorageName()));
            audioInfoRepository.save(audioInfoEntity);
            if (!roomEntity.isStreaming()) {
                sendAudiosRunnable(roomEntity.getId()).start();
            }
            return roomMapper.toRoomResponse(roomEntity);
        } catch (IOException exception) {
            throw new AudioNotFoundException();
        }
    }

    private Thread sendAudiosRunnable(UUID roomId) {
        return new Thread(() -> {
            RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
            roomEntity.setStreaming(true);
            roomRepository.save(roomEntity);
            Optional<AudioInfoEntity> optional = audioInfoRepository.findFirstByRoom_IdOrderByCreateDateAsc(roomId);
            while (optional.isPresent()) {
                AudioInfoEntity audioInfoEntity = optional.get();

                messagingTemplate.convertAndSend(MessageDestinationUtil.getRoomDestination(roomId),
                        audioInfoEntity.getStorageName());
                try {
                    File file = new File(storagePath + "\\" + audioInfoEntity.getStorageName());
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    AudioFormat audioFormat = audioInputStream.getFormat();
                    long frames = audioInputStream.getFrameLength();
                    double durations = (frames + 0.0) / audioFormat.getFrameRate();
                    Thread.sleep((long) (durations * 1000));
                } catch (UnsupportedAudioFileException | IOException | InterruptedException ignored) {
                }
                audioInfoRepository.delete(audioInfoEntity);
                optional = audioInfoRepository.findFirstByRoom_IdOrderByCreateDateAsc(roomId);
            }
            roomEntity.setStreaming(false);
            roomRepository.save(roomEntity);
        });
    }
}
