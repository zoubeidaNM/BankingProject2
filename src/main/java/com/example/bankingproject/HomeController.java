package com.example.bankingproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;



    @RequestMapping("/")
    public String showMainPage(Principal p) {
        //Principal allows you to store to name you except user to have store object parameters in repository

        return "homepage";
    }

    @RequestMapping("/withdraw")
    public String withdrowMoney(Model model) {
        //Principal allows you to store to name you except user to have store object parameters in repository
        model.addAttribute("user",new BankUser());

        return "withdrawmoney";
    }

    @PostMapping("/withdraw")
    public String processWithdrawPage(@Valid @ModelAttribute("user") BankUser user,
                                      BindingResult bindingresult, Model model) {
        model.addAttribute("user", user);
        if (bindingresult.hasErrors()) {
            System.out.println("thereis errors");
            System.out.println(bindingresult.toString());
            return "registeruser";
        } else {
            //double newBalance = balance-withdrawMoney;
            //user.setBalance(newBalance)
        }

        return "index";
    }


    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/registeruser")
    public String registerUser(Model model){

        model.addAttribute("user",new BankUser());
        return "registeruser";
    }

    @PostMapping("/registeruser")
    public String processUserRegistrationPage(@Valid @ModelAttribute("user") BankUser user,
                                          BindingResult bindingresult, Model model){
        model.addAttribute("user", user);
        if(bindingresult.hasErrors()){
            System.out.println("thereis errors");
            System.out.println(bindingresult.toString());
            return "registeruser";
        }else{
            System.out.println("saving user");
            userService.saveUser(user);
            Account account = new Account(user.getAccountNumber());
            account.addUser(user);
            accountRepository.save(account);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/registermanager")
    public String registerManager(Model model){

        model.addAttribute("user",new BankUser());
        return "registermanager";
    }

    @PostMapping("/registermanager")
    public String processManagerRegistrationPage(@Valid @ModelAttribute("user") BankUser user,
                                          BindingResult bindingresult, Model model){
        model.addAttribute("user", user);
        if(bindingresult.hasErrors()){
            System.out.println(bindingresult.toString());
            return "registermanager";
        }else{
            userService.saveManager(user);
            model.addAttribute("message", "Manager Account Successfully Created");
        }
        return "managerindex";
    }

    @RequestMapping("/account/")
    public String listRooms(Model model) {
        //model.addAttribute("rooms", roomRepository.findAll());
        return "usertransactions";
    }

    @GetMapping("/user/transfer")
    public String showUserDashboard(Model model){
        //model.addAttribute("room", new Room());
        return "index";
    }

    @GetMapping("/manager")
    public String showManagerDashboard(Model model){
        //model.addAttribute("room", new Room());
        return "managerindex";
    }

    @GetMapping("/contact")
    public String showContact(Model model){
        //model.addAttribute("room", new Room());
        return "contact";
    }


}
