package com.chatapplication.model;

import com.chatapplication.model.enums.Role;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;

    private String username;
    private String password;
    private String nickname;

    @Lob
    private byte[] profilePic;

    @Enumerated(EnumType.STRING)
    private Role role;


    @ManyToMany(mappedBy = "users")
    private Set<Chat> chats = new HashSet<>();

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public byte[] getProfilePic() { return profilePic; }
    public void setProfilePic(byte[] profilePic) { this.profilePic = profilePic; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Set<Chat> getChats() { return chats; }
    public void setChats(Set<Chat> chats) { this.chats = chats; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }


}
