package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import ru.itis.dto.request.ChatRequest;
import ru.itis.dto.response.ChatResponse;
import ru.itis.exceptions.ChatNotFoundException;
import ru.itis.exceptions.RoomNotFoundException;
import ru.itis.exceptions.UserNotFoundException;
import ru.itis.models.Chat;
import ru.itis.models.Listener;
import ru.itis.models.Room;
import ru.itis.models.User;
import ru.itis.repositories.ChatRepository;
import ru.itis.repositories.ListenerRepository;
import ru.itis.repositories.RoomRepository;
import ru.itis.repositories.UserRepository;
import ru.itis.services.ChatService;
import ru.itis.utils.mappers.ChatMapper;

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
        User owner = userRepository.findById(chatRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        Room room = roomRepository.findById(chatRequest.getRoomId()).orElseThrow(RoomNotFoundException::new);
        Chat chat = Chat.builder()
                .owner(owner)
                .room(room)
                .build();
        chat = chatRepository.save(chat);
        return chat.getId();
    }

    @Override
    public ChatResponse getChat(UUID chatId) {
        return chatMapper.toChatResponse(chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new));
    }

    @Override
    public ChatResponse updateChat(UUID chatId, ChatRequest chatRequest) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User owner = userRepository.findById(chatRequest.getOwnerId()).orElseThrow(UserNotFoundException::new);
        Room room = roomRepository.findById(chatRequest.getRoomId()).orElseThrow(RoomNotFoundException::new);
        chat.setOwner(owner);
        chat.setRoom(room);
        return chatMapper.toChatResponse(chatRepository.save(chat));
    }

    @Override
    public void deleteChat(UUID chatId) {
        chatRepository.delete(chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new));
    }

    @Override
    public ChatResponse addModerator(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chat.getModerators().add(user);
        return chatMapper.toChatResponse(chatRepository.save(chat));
    }

    @Override
    public ChatResponse deleteModerator(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chat.getModerators().remove(user);
        return chatMapper.toChatResponse(chatRepository.save(chat));
    }

    @Override
    public ChatResponse muteUser(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chat.getMutedUsers().add(user);
        return chatMapper.toChatResponse(chatRepository.save(chat));
    }

    @Override
    public ChatResponse unmuteUser(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        chat.getMutedUsers().remove(user);
        return chatMapper.toChatResponse(chatRepository.save(chat));
    }

    @Override
    public void connectToChat(UUID chatId, String sessionId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User user = userRepository.findOneByNickname(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(UserNotFoundException::new);
        Room room = chat.getRoom();
        Listener listener = Listener.builder()
                .sessionId(sessionId)
                .room(room)
                .chat(chat)
                .user(user)
                .build();
        listenerRepository.save(listener);
        //room.getListeners().add();
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
            Optional<Listener> optional = listenerRepository.findBySessionId(sessionId);
            if (optional.isPresent()) {
                Listener listener = optional.get();
                listenerRepository.delete(listener);
            }
        }
    }
}
