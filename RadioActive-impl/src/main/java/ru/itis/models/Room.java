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
@EqualsAndHashCode(exclude = {"owner", "chat", "admins", "subscribers", "listeners", "bannedUsers"})
@SuperBuilder
public class Room extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String title;

    private String password;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @OneToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @ManyToMany
    @JoinTable(name = "administration",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"))
    private Set<User> admins;

    @ManyToMany(mappedBy = "roomsInFavorites")
    private Set<User> subscribers;

    @Column(nullable = false)
    private Boolean streaming;

    @ManyToMany
    @JoinTable(name = "listening",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "listener_id", referencedColumnName = "id"))
    private Set<User> listeners;

    @ManyToMany
    @JoinTable(name = "ban",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> bannedUsers;

}
