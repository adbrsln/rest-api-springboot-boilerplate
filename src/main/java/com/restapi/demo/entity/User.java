package com.restapi.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

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
}