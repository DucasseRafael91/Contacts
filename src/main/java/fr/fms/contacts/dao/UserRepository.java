package fr.fms.contacts.dao;

import fr.fms.contacts.entities.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findUserById(Long id);
  Optional<User> findUserByMail(String mail);
}