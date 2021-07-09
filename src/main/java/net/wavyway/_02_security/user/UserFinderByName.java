package net.wavyway._02_security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserFinderByName implements UserDetailsService {
	IUserBinder userBinder;

	public UserFinderByName(@Autowired @Qualifier("userBinder") IUserBinder userBinder) {
		this.userBinder = userBinder;
	}

	@Override
	public User loadUserByUsername(String username) {
		try {
			UserData userData = userBinder.getUserData(username);
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userData.getRole());
			return new User(userData.getUsername(), userData.getPassword(), Arrays.asList(grantedAuthority));
		} catch (UsernameNotFoundException exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
