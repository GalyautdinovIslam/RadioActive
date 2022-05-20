package ru.itis.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.models.Room;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    Page<Room> findAllByTitleLikeOrOwner_NicknameLike(String title, String nickname, Pageable pageable);

    Page<Room> findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNotNull(String title, String nickname, Pageable pageable);

    Page<Room> findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNull(String title, String nickname, Pageable pageable);

    Page<Room> findAllByTitleLikeOrOwner_NicknameLikeAndStreaming(String title, String nickname,
                                                                  Boolean streaming, Pageable pageable);

    Page<Room> findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNullAndStreaming(String title, String nickname,
                                                                                 Boolean streaming, Pageable pageable);

    Page<Room> findAllByTitleLikeOrOwner_NicknameLikeAndPasswordNotNullAndStreaming(String title, String nickname,
                                                                                 Boolean streaming, Pageable pageable);
}
