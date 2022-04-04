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
@SuperBuilder
@EqualsAndHashCode(exclude = {"roomsInOwnership",
        "roomsInAdministration", "chatsInOwnership",
        "chatsInModeration", "roomsInFavorites"})
@Table(name = "account")
public class User extends AbstractEntity {

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "owner")
    private Set<Room> roomsInOwnership;

    @ManyToMany(mappedBy = "admins")
    private Set<Room> roomsInAdministration;

    @OneToMany(mappedBy = "owner")
    private Set<Chat> chatsInOwnership;

    @ManyToMany(mappedBy = "moderators")
    private Set<Chat> chatsInModeration;

    @ManyToMany
    @JoinTable(name = "favorite",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"))
    private Set<Room> roomsInFavorites;

}
