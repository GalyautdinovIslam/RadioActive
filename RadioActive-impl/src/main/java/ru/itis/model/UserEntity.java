package ru.itis.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(exclude = {"roomsInOwnership",
        "roomsInAdministration", "chatsInOwnership",
        "chatsInModeration", "roomsInFavorites"})
@Table(name = "account")
public class UserEntity extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "owner")
    private Set<RoomEntity> roomsInOwnership;

    @ManyToMany(mappedBy = "admins")
    private Set<RoomEntity> roomsInAdministration;

    @OneToMany(mappedBy = "owner")
    private Set<ChatEntity> chatsInOwnership;

    @ManyToMany(mappedBy = "moderators")
    private Set<ChatEntity> chatsInModeration;

    @ManyToMany
    @JoinTable(name = "favorite",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"))
    private Set<RoomEntity> roomsInFavorites;

}
