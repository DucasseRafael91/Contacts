package fr.fms.contacts.web;

import fr.fms.contacts.dao.ContactRepository;
import fr.fms.contacts.dao.UserRepository;
import fr.fms.contacts.dao.MessageRepository;
import fr.fms.contacts.entities.User;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ContactRepository contactRepository;

  @Autowired
  MessageRepository articleRepository;

  @Autowired
  ContactRepository categoryRepository;

  @GetMapping("/index")
  public String index(Model model, HttpSession session) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userRepository.findUserByMail(username).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    session.setAttribute("loggedUser", username);
    model.addAttribute("listContact", contactRepository.findContactsByUser(user));
    return "contacts";
  }

  @GetMapping("/delete")
  public String delete(Long id){

    contactRepository.deleteById(id);

    return "redirect:/index";
  }
}
