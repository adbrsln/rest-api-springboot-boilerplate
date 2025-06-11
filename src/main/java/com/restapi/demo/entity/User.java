package com.restapi.demo.entity;


import com.restapi.demo.security.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // In a real app, this should be hashed!

    // A user can have many todos.
    // cascade = CascadeType.ALL: If a User is deleted, all their Todos are also deleted.
    // orphanRemoval = true: If a Todo is removed from this list, it's deleted from the DB.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    @Enumerated(EnumType.STRING) // Tells JPA to store the role as a String (e.g., "USER") in the database
    private Role role;

    // --- UserDetails Method Implementations ---

    /**
     * Returns the authorities granted to the user.
     * We create a list containing a single authority based on the user's role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the password used to authenticate the user.
     * This is the hashed password from our database.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user. In our case, this is the email.
     * This is the unique identifier that Spring Security will use.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired.
     * For this boilerplate, we'll return true, meaning accounts never expire.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * For this boilerplate, we'll return true, meaning users are never locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * For this boilerplate, we'll return true, meaning credentials never expire.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * For this boilerplate, we'll return true, meaning users are always enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    public Object orElseThrow(Object o) {
       return o;
    }
}