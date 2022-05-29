package ru.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.models.Listener;

import java.util.Optional;
import java.util.UUID;

public interface ListenerRepository extends JpaRepository<Listener, UUID> {
    Optional<Listener> findBySessionId(String sessionId);
}
