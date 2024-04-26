package br.com.spring.loginautenticacao.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import br.com.spring.loginautenticacao.models.User;
import br.com.spring.loginautenticacao.repositorys.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;

	public AdminUserConfig(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		var userAdmin = userRepository.findByUsername("admin");
		
		userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
//                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

	}

}
