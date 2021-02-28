package br.net.comexport.api.core.repository;

import br.net.comexport.api.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}