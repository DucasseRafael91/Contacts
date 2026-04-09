package fr.fms.contacts.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


  /*
  Affiche la page de connexion
  @return le nom de la vue à afficher ("login")
   */
  @GetMapping("/login")
  public String login() {
    return "login";
  }

}