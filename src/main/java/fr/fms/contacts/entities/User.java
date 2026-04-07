package fr.fms.contacts.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class User implements Serializable {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Email(message = "Email invalide")
  private String mail;

  @NotNull
  @Size(min = 6, message = "Le mot de passe doit avoir au moins 6 caractères")
  private String password;

  @OneToMany(mappedBy = "user")
  private Collection<Contact> contacts;


  public User(String mail, String password) {
    this.mail = mail;
    this.password = password;
  }
}