package ru.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.model.ChatEntity;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
}
