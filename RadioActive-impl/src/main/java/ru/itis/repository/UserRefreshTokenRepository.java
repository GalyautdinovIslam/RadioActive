package ru.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.model.UserRefreshTokenEntity;

import java.util.List;
import java.util.UUID;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity, UUID> {
    List<UserRefreshTokenEntity> findAllByAccount_Id(UUID accountId);
}
