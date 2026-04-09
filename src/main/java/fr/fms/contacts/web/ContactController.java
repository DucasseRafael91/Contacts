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


  /*
  Affiche la page d'accueil
  @param model le modèle utilisé pour transmettre des données à la vue
  @param session la session HTTP courante de l'utilisateur
  @return le nom de la vue à afficher ("contacts")
   */
  @GetMapping("/index")
  public String index(Model model, HttpSession session) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth.getName().equals("anonymousUser")) {
      model.addAttribute("listContact", null);
      return "contacts";
    }

    String mail = auth.getName();
    User user = userRepository.findUserByMail(mail).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    session.setAttribute("loggedUser", mail);
    model.addAttribute("listContact", contactRepository.findContactsByUser(user));

    return "contacts";
  }

  /*
  Méthode pour supprimer un contact
  @param id : id du contact à supprimer
  @return le nom de la vue à afficher ("index")
  */
  @GetMapping("/delete")
  public String delete(Long id){

    contactRepository.deleteById(id);

    return "redirect:/index";
  }

  /*
  Méthode pour accéder à la page d'ajout de contact
  @param model : le modèle utilisé pour transmettre des données à la vue
  @return le nom de la vue à afficher ("add")
  */
  @GetMapping("/add")
  public String add(Model model) {
    model.addAttribute("contact" , new Contact());
    model.addAttribute("categories",categorieRepository.findAll());
    return "add";
  }

  /*
  Méthode pour ajouter un contact
  @param Contact : Objet contact à ajouter
  @param bindingResult : Résultats des saisies
  @return s'il a des erreurs bindingResult le nom de la vue à afficher ("add")
  @return le nom de la vue à afficher ("index")
  */
  @PostMapping("/save")
  public String save(Model model, @Valid Contact contact, BindingResult bindingResult) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (bindingResult.hasErrors()) {
      model.addAttribute("categories", categorieRepository.findAll());
      return "add";
    }

    String mail = auth.getName();
    User user = userRepository.findUserByMail(mail).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    contact.setUser(user);
    contactRepository.save(contact);

    return "redirect:/index";
  }

  /*
  Méthode pour afficher la page de mise à jour d'un contact
  @param id : Id de l'objet à mettre à jour
  @param model : le modèle utilisé pour transmettre des données à la vue
  @return le nom de la vue à afficher ("edit")
  */
  @GetMapping("/edit")
  public String edit(Long id, Model model){
    Contact contact = contactRepository.findContactById(id);
    model.addAttribute("contact", contact);
    model.addAttribute("categories",categorieRepository.findAll());
    return "edit";
  }

  /*
  Méthode pour mettre à jour un contact
  @param model : le modèle utilisé pour transmettre des données à la vue
  @param Contact : Objet contact à mettre à jour
  @param bindingResult : Résultats des saisies
  @return s'il a des erreurs bindingResult le nom de la vue à afficher ("edit")
  @return le nom de la vue à afficher ("index")
  */
  @PostMapping("/update")
  public String update(Model model, @Valid Contact contact, BindingResult bindingResult) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (bindingResult.hasErrors()) {
      model.addAttribute("categories", categorieRepository.findAll());
      return "edit";
    }

    String mail = auth.getName();
    User user = userRepository.findUserByMail(mail).orElseThrow();
    contact.setUser(user);
    contactRepository.save(contact);

    return "redirect:/index";
  }
}
