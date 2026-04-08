package fr.fms.contacts.web;

import fr.fms.contacts.dao.CategorieRepository;
import fr.fms.contacts.dao.ContactRepository;
import fr.fms.contacts.dao.UserRepository;
import fr.fms.contacts.dao.MessageRepository;
import fr.fms.contacts.entities.Categorie;
import fr.fms.contacts.entities.Contact;
import fr.fms.contacts.entities.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ContactRepository contactRepository;

  @Autowired
  MessageRepository articleRepository;

  @Autowired
  CategorieRepository categorieRepository;

  @GetMapping("/index")
  public String index(Model model, HttpSession session) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
      model.addAttribute("listContact", null); // ou liste vide
      return "contacts";
    }

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

  @GetMapping("/add")
  public String add(Model model) {
    model.addAttribute("contact" , new Contact());
    return "add";
  }

  @PostMapping("/save")
  public String save(@Valid Contact contact,
      BindingResult bindingResult,
      @AuthenticationPrincipal UserDetails userDetails) {

    if (bindingResult.hasErrors()) {
      return "add";
    }

    // récupérer l'utilisateur depuis Spring Security
    String mail = userDetails.getUsername();

    User user = userRepository.findUserByMail(mail).orElseThrow();
    contact.setUser(user);
    contactRepository.save(contact);

    return "redirect:/index";
  }

  @GetMapping("/edit")
  public String edit(Long id, Model model){
    Contact contact = contactRepository.findContactById(id);
    List<Categorie> categories = categorieRepository.findAll();
    model.addAttribute("contact", contact);
    model.addAttribute("categories",categories);
    return "edit";
  }

  @PostMapping("/update")
  public String update(Model model, @Valid Contact contact, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("categories", categorieRepository.findAll());
      return "edit";
    }

    String mail = userDetails.getUsername();
    User user = userRepository.findUserByMail(mail).orElseThrow();
    contact.setUser(user);
    contactRepository.save(contact);

    return "redirect:/index";
  }
}
