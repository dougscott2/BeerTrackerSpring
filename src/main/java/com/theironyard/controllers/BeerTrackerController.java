package com.theironyard.controllers;

import com.theironyard.entities.Beer;
import com.theironyard.entities.User;
import com.theironyard.services.BeerRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.util.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by DrScott on 11/10/15.
 */
@Controller
public class BeerTrackerController {
    @Autowired
    BeerRepository beers;
    @Autowired
    UserRepository users;

    @PostConstruct   /////how to make a method to run at the start up!!!!!!!
    public void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = users.findOneByName("Admin");
        if(user == null){
            user = new User();
            user.name="Admin";
            user.password = PasswordHash.createHash("password");
            users.save(user);
        }
    }

    @RequestMapping("/")
    public String home(HttpSession session,
                       Model model,
                       String type,
                       Integer calories,
                       String search,
                       String showMine){
        //HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null){
            return "login";
        }
        if (showMine != null){
            model.addAttribute("beers", users.findOneByName(username).beers);
        }

       else if (search!=null){
            model.addAttribute("beers", beers.searchByName(search));
        }
        else if (type!=null && calories!=null){
            model.addAttribute("beers", beers.findByTypeAndCalories(type, calories));
        }
        else if (type != null){
            model.addAttribute("beers", beers.findByTypeOrderByNameAsc(type));
        }
        else {
            model.addAttribute("beers", beers.findAll());
        }
        return "home";
    }

    @RequestMapping("add-beer")
    public String addBeer(HttpSession session,
                          String beerName,
                          String beerType,
                          Integer beercalories) throws Exception {
       // HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
       if (username == null){
           throw new Exception("Not logged in!");
       }
        User user = users.findOneByName(username);

        Beer beer = new Beer();
        beer.name=beerName;
        beer.type=beerType;
        beer.calories = beercalories;
        beer.user=user;
        beers.save(beer);
        return "redirect:/";
    }
    @RequestMapping("edit-beer")
    public String editBeer(HttpSession session,
                           int id,
                           String name,
                           String type) throws Exception {
       if (session.getAttribute("username")==null){
           throw new Exception("Not logged in!");
       }
        Beer beer = beers.findOne(id);
        beer.name = name;
        beer.type = type;
        beers.save(beer);
        return "redirect:/";
    }
    @RequestMapping("login")
    public String login(HttpSession session, String username, String password) throws Exception {
       // HttpSession session = request.getSession();
        session.setAttribute("username", username);

        User user = users.findOneByName(username);
        if(user==null){
            user = new User();
            user.name = username;
            user.password = PasswordHash.createHash(password);
            users.save(user);
        } else if (!PasswordHash.validatePassword(password, user.password)){
            throw new Exception("WRONG PASSWORD DUMMY!");
        }

        return "redirect:/";
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }
  //  @RequestMapping("showMine")

}
