package br.com.spring.loginautenticacao.controllers;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.spring.loginautenticacao.dtos.LoginRequest;
import br.com.spring.loginautenticacao.dtos.LoginResponse;
import br.com.spring.loginautenticacao.repositorys.UserRepository;

@RestController
@RequestMapping("/api/token")
public class TokenController {
	private final JwtEncoder jwtEncoder;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository,
			BCryptPasswordEncoder bcryptPasswordEncoder) {
		this.jwtEncoder = jwtEncoder;
		this.userRepository = userRepository;
		this.passwordEncoder = bcryptPasswordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		var user = userRepository.findByUseremail(loginRequest.useremail());

		/*
		 * se user estiver vazio retorna uma exececao, caso contraro verifica se a senha
		 * passada pelo usuario e igual a senha do banco de dados
		 */
		if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
			throw new BadCredentialsException("user or password is invalid!");
		}

		var now = Instant.now();
		var expiresIn = 300L;

//		gera o token jwt configurando os atributos do json do token jwt e retorna na requizicao
		var claims = JwtClaimsSet.builder().issuer("mybackend").subject(user.get().getUserId().toString()).issuedAt(now)
				.expiresAt(now.plusSeconds(expiresIn)).build();

//		criptografa os json do jwt com a chave privada e gera o valor do tokem com os dados criptografados
		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
	}

}