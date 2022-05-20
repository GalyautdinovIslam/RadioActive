package ru.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.models.Chat;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
