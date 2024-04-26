package br.com.spring.loginautenticacao.models;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.spring.loginautenticacao.dtos.LoginRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private UUID userId;

	private String username;
	private String password;

	 public UUID getUserId() {
	        return userId;
	    }

	public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(loginRequest.password(), this.password);
	}
}
