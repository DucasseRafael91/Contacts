package fr.fms.contacts.dao;

import fr.fms.contacts.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
  Contact findContactById(Long id);
}