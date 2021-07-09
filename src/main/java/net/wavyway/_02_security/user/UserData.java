package net.wavyway._02_security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserData implements UserDetails {
	private String username = null;
	private String password = null;
	private String role = null;

	private Collection<? extends GrantedAuthority> authorities = null;
	private Boolean accountExpired = false;
	private Boolean accountLocked = false;
	private Boolean credentialsExpired = false;
	private Boolean accountEnabled = false;

	public UserData() {
	}

	public UserData(String username) {
		this.username = username;
	}

	public UserData(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
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
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
