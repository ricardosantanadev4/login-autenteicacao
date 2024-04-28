package br.com.spring.loginautenticacao.repositorys;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.spring.loginautenticacao.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByUseremail(String useremail);

}
