package com.reddit.controller;

import java.time.Instant;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.dto.ApiResponse;
import com.reddit.dto.AuthenticationResponse;
import com.reddit.dto.LoginRequest;
import com.reddit.dto.RefreshTokenRequest;
import com.reddit.dto.RegisterRequest;
import com.reddit.model.User;
import com.reddit.repository.UserRepository;
import com.reddit.service.AuthService;
import com.reddit.service.RefreshTokenService;
import com.reddit.service.UserDetailsServiceImpl;
import com.reddit.utility.JwtUtility;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	private AuthService authService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private JwtUtility jwtUtility;
	@Autowired
	private AuthenticationResponse authenticationResponse;
	
	private final RefreshTokenService refreshTokenService;
	private final UserRepository userRepository;
	
	@GetMapping("/test")
    public String showHomePage () {
        return "Testing api";
    }
	
	@PostMapping("/signup")
    public ResponseEntity<ApiResponse> Signup (@RequestBody RegisterRequest registerRequest) {
		
		if(registerRequest.getUsername() == "" || registerRequest.getEmail() == "" 
				|| registerRequest.getPassword() == "")
		{
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, "Please Provide All Information", null));
		}
		User user = userRepository.findByUsername(registerRequest.getUsername());
		
		if(user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, "User Registered Already", user));
		}
		
        authService.SignupService(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "User Registration Successfull", null));
    }
	
	@GetMapping("accountVerification/{token}")
    public ResponseEntity<ApiResponse> verifyAccount (@PathVariable String token) {
        authService.verifyAccount(token);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "User Verified Successfully", null));

    }
	
	@PostMapping("/login")
//	AuthenticationResponse
	public ResponseEntity<ApiResponse> authenticate(@RequestBody LoginRequest loginRequest) throws Exception {
		User user = userRepository.findByUsername(loginRequest.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ApiResponse(false, "User Not Found",null));
		}
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
					);
		} catch(BadCredentialsException e) {
//			throw new Exception("Bad Credentials ", e);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ApiResponse(false, "User Not Found",e));
		}
		
		final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequest.getUsername());
		final String token = jwtUtility.generateToken(userDetails);
		
		// return new AuthenticationResponse(token, " ", jwtUtility.getJwtExpirationInMills(),loginRequest.getUsername());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "User Loggedin Successfully", 
				AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expireAt(Instant.now().plusMillis(jwtUtility.getJwtExpirationInMills()))
				.username(loginRequest.getUsername())
				.build()));
		
	}
	
	
	@PostMapping("/refresh/token")
	public ResponseEntity<ApiResponse> refreshToken (@Valid @RequestBody RefreshTokenRequest refreshTokenRequest ) {
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true,"Refresh Token Generated Successfully", 
				authService.refreshToken(refreshTokenRequest)));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logout (@Valid @RequestBody RefreshTokenRequest refreshTokenRequest ){
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true,"Refresh Token Deleted Successfully", null));
	}
}
