package ru.itis.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"owner", "room", "moderators", "mutedUsers", "messages", "listeners"})
@SuperBuilder
public class Chat extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @OneToOne(mappedBy = "chat")
    private Room room;

    @OneToMany(mappedBy = "chat")
    private Set<Listener> listeners;

    @ManyToMany
    @JoinTable(name = "moderation",
            joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "moderator_id", referencedColumnName = "id"))
    private Set<User> moderators;

    @ManyToMany
    @JoinTable(name = "mute",
            joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> mutedUsers;

    @OneToMany(mappedBy = "chat")
    private Set<Message> messages;
}
