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
@EqualsAndHashCode(exclude = {"owner", "chatEntity", "admins", "subscribers", "listenerEntities", "bannedUserEntities"})
@SuperBuilder
public class RoomEntity extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String title;

    private String password;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;

    @OneToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatEntity chatEntity;

    @ManyToMany
    @JoinTable(name = "administration",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"))
    private Set<UserEntity> admins;

    @ManyToMany(mappedBy = "roomsInFavorites")
    private Set<UserEntity> subscribers;

    @Column(nullable = false)
    private boolean streaming;

    @OneToMany(mappedBy = "roomEntity")
    private Set<ListenerEntity> listenerEntities;

    @ManyToMany
    @JoinTable(name = "ban",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<UserEntity> bannedUserEntities;

}
