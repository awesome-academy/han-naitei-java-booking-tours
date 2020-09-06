package com.bookingTour.model;

import com.bookingTour.model.enu.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
	private UserModel user = null;

	public CustomUserDetails(UserModel user) {
		super(user.getUserName(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
		this.user = user;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	private static Collection<? extends GrantedAuthority> mapRolesToAuthorities(int value) {
		return Role.toList(value).stream().map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());
	}

}
