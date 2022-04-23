package com.reddit.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.reddit.exceptions.SpringRedditException;
import com.reddit.model.RefreshToken;
import com.reddit.repository.RefreshtokenRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

	private final RefreshtokenRepository refreshtokenRepository;
	
	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		
		return refreshtokenRepository.save(refreshToken);
				
	}
	
	public void validateRefreshToken(String token) {
		refreshtokenRepository.findByToken(token).orElseThrow(()-> new SpringRedditException("Invalid refresh Token"));
	}
	
	 public void deleteRefreshToken(String token) {
		 refreshtokenRepository.deleteByToken(token);
	 }
}
