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

	@Column(name = "userid")
	private UUID userId;

	private String useremail;
	private String password;

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getUserId() {
		return userId;
	}

//	criptografa o password enviado pelo usuario e compara a senha recebida com a senha do banco dedados
//	depois retorna um true se as senhas estiverem iguais
	public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(loginRequest.password(), this.password);
	}
}
