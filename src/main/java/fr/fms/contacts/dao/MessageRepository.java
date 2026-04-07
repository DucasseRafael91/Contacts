package fr.fms.contacts.dao;

import fr.fms.contacts.entities.Message;
import fr.fms.contacts.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
  Message findMessageById(Long id);
}