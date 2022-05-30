package ru.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.models.UserRefreshTokenEntity;

import java.util.List;
import java.util.UUID;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity, UUID> {
    List<UserRefreshTokenEntity> findAllByAccount_Id(UUID accountId);
}
