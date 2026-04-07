package fr.fms.contacts.web;

import fr.fms.contacts.dao.ContactRepository;
import fr.fms.contacts.dao.UserRepository;
import fr.fms.contacts.dao.MessageRepository;
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
  public String index(Model model) {

    model.addAttribute("listContact", contactRepository.findAll());
    return "contacts";
  }
}
