package com.reddit.service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.reddit.dto.AuthenticationResponse;
import com.reddit.dto.LoginRequest;
import com.reddit.dto.RefreshTokenRequest;
import com.reddit.dto.RegisterRequest;
import com.reddit.exceptions.SpringRedditException;
import com.reddit.model.NotificationEmail;
import com.reddit.model.User;
import com.reddit.model.VerificationToken;
import com.reddit.repository.UserRepository;
import com.reddit.repository.VerificationTokenRepository;
import com.reddit.utility.JwtUtility;

import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
@Service
@AllArgsConstructor
public class AuthService {

	@Autowired
	private MailService mailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	private final JwtUtility jwtUtility;
	private final RefreshTokenService refreshTokenService;
	
	
	public void SignupService(RegisterRequest registerRequest) {
		User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToke(user);
        mailService.sendMail(new NotificationEmail("Please Active Your Account", user.getEmail(),
                "Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
                        + "http://localhost:9000/api/auth/accountVerification/"+ token));
	}

	private String generateVerificationToke(User user) {
		String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
	}
	
	
	public void  verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username =  verificationToken.getUser().getUsername();
	       User user  = userRepository.findByUsername(username);
	       if(user==null) {
	    	   new SpringRedditException(("User Not Found "+ username));
	       }
//	    		   .orElseThrow(() -> new SpringRedditException(("User Not Found "+ username)));
	       user.setEnabled(true);
	       userRepository.save(user);
	}
	
	public User getCurrentUser() {
//        Jwt principal = (Jwt) SecurityContextHolder.
//                getContext().getAuthentication().getPrincipal();
//        return userRepository.findByUsername(principal.getSubject())
//                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());
        if(user==null) {
	    	   new UsernameNotFoundException(("User Not Found "+ authentication.getName()));
	       }
        return user;
//                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + authentication.getName()));
    }
	
	public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenrequest) {
		refreshTokenService.validateRefreshToken(refreshTokenrequest.getRefreshToken());
		String token = jwtUtility.generateTokenWithUsername(refreshTokenrequest.getUsername());
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenrequest.getRefreshToken())
				.expireAt(Instant.now().plusMillis(jwtUtility.getJwtExpirationInMills()))
				.username(refreshTokenrequest.getUsername())
				.build();
	}
}
