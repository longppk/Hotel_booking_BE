package com.hotel.booking.config;

import com.hotel.booking.enums.Role;
import com.hotel.booking.models.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUserDetails implements UserDetails {

    private Long id;
    private String username;

    private String password;
    private Role role;
    private boolean isActive;

    public UserInfoUserDetails(Users user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.isActive = user.isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(getRole().name()));
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive; // Sử dụng trường từ Users entity
    }

}