package ru.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.model.ListenerEntity;

import java.util.Optional;
import java.util.UUID;

public interface ListenerRepository extends JpaRepository<ListenerEntity, UUID> {
    Optional<ListenerEntity> findBySessionId(String sessionId);
}
