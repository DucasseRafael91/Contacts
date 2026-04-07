package fr.lndr.SpringStockMvcSec.web;

import fr.lndr.SpringStockMvcSec.dao.ArticleRepository;
import fr.lndr.SpringStockMvcSec.dao.CategoryRepository;
import fr.lndr.SpringStockMvcSec.entities.Article;
import fr.lndr.SpringStockMvcSec.entities.Category;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
  @Autowired
  ArticleRepository articleRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @GetMapping("/index")
  public String index(Model model,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "keyword", defaultValue = "") String kw,
      @RequestParam(name = "categoryId", required = false) String categoryId) {

    Long id = null;

    if (categoryId != null && !categoryId.trim().isEmpty()) {
      id = Long.parseLong(categoryId);
    }

    Page<Article> articles;

    if (id != null) {
      articles = articleRepository.findByCategoryIdAndDescriptionContains(
          id, kw, PageRequest.of(page, 5));
    } else {
      articles = articleRepository.findArticleByDescriptionContains(
          kw, PageRequest.of(page, 5));
    }

    model.addAttribute("listArticle", articles.getContent());
    model.addAttribute("pages", new int[articles.getTotalPages()]);
    model.addAttribute("keyword", kw);
    model.addAttribute("currentPage", page);
    model.addAttribute("categoryId", id);
    model.addAttribute("categories", categoryRepository.findAll());

    return "articles";
  }

  @GetMapping("/delete")
  public String delete(Long id, int page, String keyword){

    articleRepository.deleteById(id);

    return "redirect:/index?page="+page+"&keyword="+keyword;
  }

  @GetMapping("/edit")
  public String edit(Long id, Model model){
    Article article = articleRepository.findArticleById(id);
    List<Category> categories = categoryRepository.findAll();
    model.addAttribute("article", article);
    model.addAttribute("categories",categories);
    return "edit";
  }

  @GetMapping("/addToCart")
  public String addToCart(Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword, HttpSession session) {

    List<Article> panier = (List<Article>) session.getAttribute("panier");
    if (panier == null) {
      panier = new ArrayList<>();
      session.setAttribute("panier", panier);
    }
    Article article = articleRepository.findArticleById(id);
    if (article != null) {
      panier.add(article);
    }

    return "redirect:/index?page=" + page + "&keyword=" + keyword;
  }

  @GetMapping("/cart")
  public String showCart(HttpSession session, Model model) {
    List<Article> panier = (List<Article>) session.getAttribute("panier");
    if (panier == null) {
      panier = new ArrayList<>();
    }
    model.addAttribute("listArticle", panier);
    return "cart";
  }

  @GetMapping("/article")
  public String article(Model model) {
    model.addAttribute("article" , new Article());
    return "article";
  }

  @PostMapping("/save")
  public String save(@Valid Article article ,  BindingResult bindingResult) {
    if(bindingResult.hasErrors()) return "article";
    articleRepository.save(article);
    return "redirect:/index";
  }

  @PostMapping("/update")
  public String update(Model model, @Valid Article article, BindingResult bindingResult) {

    if(bindingResult.hasErrors()){
      model.addAttribute("categories", categoryRepository.findAll());
      return "edit";
    }

    articleRepository.save(article);
    return "redirect:/index";
  }
}
