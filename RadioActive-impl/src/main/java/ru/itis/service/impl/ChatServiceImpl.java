package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import ru.itis.dto.request.ChatRequest;
import ru.itis.dto.response.ChatResponse;
import ru.itis.exception.ChatNotFoundException;
import ru.itis.exception.RoomNotFoundException;
import ru.itis.exception.UserNotFoundException;
import ru.itis.model.ChatEntity;
import ru.itis.model.ListenerEntity;
import ru.itis.model.RoomEntity;
import ru.itis.model.UserEntity;
import ru.itis.repository.ChatRepository;
import ru.itis.repository.ListenerRepository;
import ru.itis.repository.RoomRepository;
import ru.itis.repository.UserRepository;
import ru.itis.service.ChatService;
import ru.itis.util.mapper.ChatMapper;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ListenerRepository listenerRepository;
    private final ChatMapper chatMapper;

    @Override
    public UUID createChat(ChatRequest chatRequest) {
        UserEntity owner = userRepository.findById(chatRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        RoomEntity roomEntity = roomRepository.findById(chatRequest.getRoomId()).orElseThrow(RoomNotFoundException::new);
        ChatEntity chatEntity = ChatEntity.builder()
                .owner(owner)
                .roomEntity(roomEntity)
                .build();
        chatEntity = chatRepository.save(chatEntity);
        return chatEntity.getId();
    }

    @Override
    public ChatResponse getChat(UUID chatId) {
        return chatMapper.toChatResponse(chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new));
    }

    @Override
    public ChatResponse updateChat(UUID chatId, ChatRequest chatRequest) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        UserEntity owner = userRepository.findById(chatRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        RoomEntity roomEntity = roomRepository.findById(chatRequest.getRoomId()).orElseThrow(RoomNotFoundException::new);
        chatEntity.setOwner(owner);
        chatEntity.setRoomEntity(roomEntity);
        return chatMapper.toChatResponse(chatRepository.save(chatEntity));
    }

    @Override
    public void deleteChat(UUID chatId) {
        chatRepository.delete(chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new));
    }

    @Override
    public ChatResponse addModerator(UUID chatId, UUID userId) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chatEntity.getModerators().add(userEntity);
        return chatMapper.toChatResponse(chatRepository.save(chatEntity));
    }

    @Override
    public ChatResponse deleteModerator(UUID chatId, UUID userId) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chatEntity.getModerators().remove(userEntity);
        return chatMapper.toChatResponse(chatRepository.save(chatEntity));
    }

    @Override
    public ChatResponse muteUser(UUID chatId, UUID userId) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chatEntity.getMutedUserEntities().add(userEntity);
        return chatMapper.toChatResponse(chatRepository.save(chatEntity));
    }

    @Override
    public ChatResponse unmuteUser(UUID chatId, UUID userId) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chatEntity.getMutedUserEntities().remove(userEntity);
        return chatMapper.toChatResponse(chatRepository.save(chatEntity));
    }

    @Override
    public void connectToChat(UUID chatId, String sessionId) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        UserEntity userEntity = userRepository.findOneByNickname(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(UserNotFoundException::new);
        RoomEntity roomEntity = chatEntity.getRoomEntity();
        ListenerEntity listenerEntity = ListenerEntity.builder()
                .sessionId(sessionId)
                .roomEntity(roomEntity)
                .chatEntity(chatEntity)
                .userEntity(userEntity)
                .build();
        listenerRepository.save(listenerEntity);
    }

    @Override
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
        handleLeaveChat(event);
    }

    @Override
    public void handleDisconnect(SessionDisconnectEvent event) {
        handleLeaveChat(event);
    }

    private void handleLeaveChat(AbstractSubProtocolEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
            Optional<ListenerEntity> optional = listenerRepository.findBySessionId(sessionId);
            if (optional.isPresent()) {
                ListenerEntity listenerEntity = optional.get();
                listenerRepository.delete(listenerEntity);
            }
        }
    }
}
