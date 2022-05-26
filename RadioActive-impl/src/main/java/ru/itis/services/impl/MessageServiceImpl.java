package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.MessageRequest;
import ru.itis.dto.response.MessageResponse;
import ru.itis.exceptions.ChatNotFoundException;
import ru.itis.exceptions.MessageNotFoundException;
import ru.itis.exceptions.UserNotFoundException;
import ru.itis.models.Chat;
import ru.itis.models.Message;
import ru.itis.models.User;
import ru.itis.repositories.ChatRepository;
import ru.itis.repositories.MessageRepository;
import ru.itis.repositories.UserRepository;
import ru.itis.services.MessageService;
import ru.itis.utils.mappers.MessageMapper;
import ru.itis.utils.ws.MessageDestinationUtils;

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
        User sender = userRepository.findById(messageRequest.getSenderId()).orElseThrow(UserNotFoundException::new);
        Chat chat = chatRepository.findById(messageRequest.getChatId()).orElseThrow(ChatNotFoundException::new);
        Message message = Message.builder()
                .sender(sender)
                .chat(chat)
                .content(messageRequest.getContent())
                .build();
        message = messageRepository.save(message);
        return message.getId();
    }

    @Override
    public MessageResponse getMessage(UUID messageId) {
        return messageMapper.toMessageResponse(messageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new));
    }

    @Override
    public MessageResponse updateMessage(UUID messageId, MessageRequest messageRequest) {
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
        User sender = userRepository.findById(messageRequest.getSenderId()).orElseThrow(UserNotFoundException::new);
        Chat chat = chatRepository.findById(messageRequest.getChatId()).orElseThrow(ChatNotFoundException::new);
        message.setSender(sender);
        message.setChat(chat);
        message.setContent(messageRequest.getContent());
        return messageMapper.toMessageResponse(messageRepository.save(message));
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.delete(messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new));
    }
// TODO
    @Override
    public void sendMessage(UUID chatId, String content) {
        //User sender = userRepository.findById().orElseThrow(UserNotFoundException::new);
        Chat chat = chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        Message message = Message.builder()
                //.sender(sender)
                .chat(chat)
                .content(content)
                .build();
        message = messageRepository.save(message);

        messagingTemplate.convertAndSend(MessageDestinationUtils.getDestination(chatId),
                messageMapper.toMessageResponse(message));
    }

    @Override
    public List<MessageResponse> getLastMessages(UUID chatId, int amount) {
        PageRequest pageRequest = PageRequest.of(0, amount, Sort.by("createDate").descending());
        Page<Message> messages = messageRepository.findAll(pageRequest);
        return messageMapper.toMessageResponse(messages.getContent());
    }
}
