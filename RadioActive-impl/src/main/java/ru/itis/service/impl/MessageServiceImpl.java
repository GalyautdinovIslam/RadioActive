package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.MessageRequest;
import ru.itis.dto.response.MessageResponse;
import ru.itis.exception.ChatNotFoundException;
import ru.itis.exception.MessageNotFoundException;
import ru.itis.exception.UserNotFoundException;
import ru.itis.model.ChatEntity;
import ru.itis.model.MessageEntity;
import ru.itis.model.UserEntity;
import ru.itis.repository.ChatRepository;
import ru.itis.repository.MessageRepository;
import ru.itis.repository.UserRepository;
import ru.itis.service.MessageService;
import ru.itis.util.mapper.MessageMapper;
import ru.itis.util.ws.MessageDestinationUtil;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public UUID createMessage(MessageRequest messageRequest) {
        UserEntity sender = userRepository.findById(messageRequest.getSenderId()).orElseThrow(UserNotFoundException::new);
        ChatEntity chatEntity = chatRepository.findById(messageRequest.getChatId()).orElseThrow(ChatNotFoundException::new);
        MessageEntity messageEntity = MessageEntity.builder()
                .sender(sender)
                .chatEntity(chatEntity)
                .content(messageRequest.getContent())
                .build();
        messageEntity = messageRepository.save(messageEntity);
        return messageEntity.getId();
    }

    @Override
    public MessageResponse getMessage(UUID messageId) {
        return messageMapper.toMessageResponse(messageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new));
    }

    @Override
    public MessageResponse updateMessage(UUID messageId, MessageRequest messageRequest) {
        MessageEntity messageEntity = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
        UserEntity sender = userRepository.findById(messageRequest.getSenderId()).orElseThrow(UserNotFoundException::new);
        ChatEntity chatEntity = chatRepository.findById(messageRequest.getChatId()).orElseThrow(ChatNotFoundException::new);
        messageEntity.setSender(sender);
        messageEntity.setChatEntity(chatEntity);
        messageEntity.setContent(messageRequest.getContent());
        return messageMapper.toMessageResponse(messageRepository.save(messageEntity));
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.delete(messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new));
    }

    @Override
    public void sendMessage(UUID chatId, String content) {
        UserEntity sender = userRepository.findOneByNickname(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(UserNotFoundException::new);
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        MessageEntity messageEntity = MessageEntity.builder()
                .sender(sender)
                .chatEntity(chatEntity)
                .content(content)
                .build();
        messageEntity = messageRepository.save(messageEntity);

        messagingTemplate.convertAndSend(MessageDestinationUtil.getChatDestination(chatId),
                messageMapper.toMessageResponse(messageEntity));
    }

    @Override
    public List<MessageResponse> getLastMessages(UUID chatId, int amount) {
        PageRequest pageRequest = PageRequest.of(0, amount, Sort.by("createDate").descending());
        Page<MessageEntity> messages = messageRepository.findAll(pageRequest);
        return messageMapper.toMessageResponse(messages.getContent());
    }
}
