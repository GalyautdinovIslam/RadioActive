package ru.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.model.AudioInfoEntity;

import java.util.Optional;
import java.util.UUID;

public interface AudioInfoRepository extends JpaRepository<AudioInfoEntity, UUID> {
    Optional<AudioInfoEntity> findByStorageName(String audioName);

    Optional<AudioInfoEntity> findFirstByRoom_IdOrderByCreateDateAsc(UUID id);
}
