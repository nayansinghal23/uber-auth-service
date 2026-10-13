package com.example.uberauthservice.helpers;

import com.example.uberauthservice.models.Passenger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// The purpose of this class is that spring security works on UserDetails polymorphic type for auth.
public class AuthPassengerDetails extends Passenger implements UserDetails {
    private String email;
    private String password;

    public AuthPassengerDetails(Passenger passenger) {
        this.email = passenger.getEmail();
        this.password = passenger.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email; // Instead of username we are considering email
    }

    // Below set of methods are not our concern as we don't need them.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
