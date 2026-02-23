package net.saadbr.dessin.security;

import java.util.Set;

/**
 * @author saade
 **/
public class User {
    private String username;
    private String password;
    private Set<Role> roles;

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }
}
