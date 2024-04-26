package br.com.spring.loginautenticacao.controllers;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.spring.loginautenticacao.dtos.LoginRequest;
import br.com.spring.loginautenticacao.dtos.LoginResponse;
import br.com.spring.loginautenticacao.repositorys.UserRepository;

@RestController
public class TokenController {
	private final JwtEncoder jwtEncoder;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.jwtEncoder = jwtEncoder;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		var user = userRepository.findByUsername(loginRequest.username());

		if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
			throw new BadCredentialsException("user or password is invalid!");
		}

		var now = Instant.now();
		var expiresIn = 300L;

		var claims = JwtClaimsSet.builder().issuer("mybackend").subject(user.get().getUserId().toString()).issuedAt(now)
				.expiresAt(now.plusSeconds(expiresIn)).build();
		
		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
	}

}