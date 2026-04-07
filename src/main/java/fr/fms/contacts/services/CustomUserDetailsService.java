package fr.fms.contacts.services;

import fr.fms.contacts.entities.User;
import fr.fms.contacts.dao.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
    User user = userRepository.findByMail(mail)
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + mail));

    // Spring Security attend un objet UserDetails
    return new org.springframework.security.core.userdetails.User(
        user.getMail(),           // identifiant
        user.getPassword(),       // mot de passe encodé
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // rôle par défaut
    );
  }
}