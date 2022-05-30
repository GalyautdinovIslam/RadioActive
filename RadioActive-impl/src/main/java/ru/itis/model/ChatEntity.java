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
@EqualsAndHashCode(exclude = {"owner", "roomEntity", "moderators", "mutedUserEntities", "messageEntities", "listenerEntities"})
@SuperBuilder
public class ChatEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;

    @OneToOne(mappedBy = "chatEntity")
    private RoomEntity roomEntity;

    @OneToMany(mappedBy = "chatEntity")
    private Set<ListenerEntity> listenerEntities;

    @ManyToMany
    @JoinTable(name = "moderation",
            joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "moderator_id", referencedColumnName = "id"))
    private Set<UserEntity> moderators;

    @ManyToMany
    @JoinTable(name = "mute",
            joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<UserEntity> mutedUserEntities;

    @OneToMany(mappedBy = "chatEntity")
    private Set<MessageEntity> messageEntities;
}
