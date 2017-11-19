package com.example.bankingproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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


    // permit all pages
    //----------------------------------------------------------------------------------------
    @RequestMapping("/")
    public String showMainPage() {
        return "homepage";
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
        if(bindingresult.hasErrors())
        {
            return "registeruser";
        }else
            {
            if((accountRepository.findByAccountNumber(user.getAccountNumber())== null) &&
                    (userRepository.findByUsername(user.getUsername()) ==null)){

                //create user in his account
                Account account = new Account(user.getAccountNumber());
                account.addUser(user);
                user.addAccount(account);
                accountRepository.save(account);
                userService.saveUser(user);
                model.addAttribute("message", "User Account Successfully Created");
            }
                else if (userRepository.findByUsername(user.getUsername()) ==null){
                model.addAttribute("error" , true);
                model.addAttribute("error_message", "Username already exists. Try again!");

                return "registeruser";

            }else if (accountRepository.findByAccountNumber(user.getAccountNumber())== null){
                model.addAttribute("error" , true);
                model.addAttribute("error_message", "Account number already exists. Try again!");

                return "registeruser";

            }

            }
        return "login";
    }

    @RequestMapping("/registermanager")
    public String registerManager(Model model){
        model.addAttribute("user",new BankUser());
        return "registermanager";
    }

    @PostMapping("/registermanager")
    public String processManagerRegistrationPage(@Valid @ModelAttribute("user") BankUser user,
                                                 BindingResult bindingresult, Model model){
        if(bindingresult.hasErrors()){
            return "registermanager";
        }else{
            if(userRepository.findByUsername(user.getUsername()) ==null) {
                user.setAccountNumber("000000");
                userService.saveManager(user);

                model.addAttribute("user", user);
                model.addAttribute("message", "Manager Account Successfully Created");
            }else {
                model.addAttribute("error" , true);
                model.addAttribute("error_message", "Username already exists. Try again!");

                return "registermanager";

            }
        }
        return "login";
    }

    @GetMapping("/contact")
    public String showContact(Model model){
        return "contact";
    }
    //----------------------------------------------------------------------------------------

    //Customer and manager pages
    //----------------------------------------------------------------------------------------

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }
    //----------------------------------------------------------------------------------------

    //manager pages
    //----------------------------------------------------------------------------------------
    @GetMapping("/manager/dashboard")
    public String showManagerDashboard(Principal principal, Model model){
        BankUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("bankuser", user);
        return "managerindex";
    }

    @GetMapping("/manager/listaccounts")
    public String showAllAccountsPage(Principal principal, Model model){

        BankUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("bankuser", user);
        model.addAttribute("accounts", accountRepository.findAll());
        return "showallaccounts";
    }

    @RequestMapping("/manager/detail/{id}")
    public String showAccountToManager(@PathVariable("id") long id, Model model){
        Account account = accountRepository.findOne(id);

        model.addAttribute("account", accountRepository.findOne(id));
        model.addAttribute("accountNumber", account.getAccountNumber());
        model.addAttribute("balance", account.getBalance());
        return "showalltransactions";
    }


    //----------------------------------------------------------------------------------------


    //customer pages
    //----------------------------------------------------------------------------------------

    @RequestMapping("/user/dashboard")
    public String showUserDashboard(Principal principal, Model model){
        BankUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("bankuser", user);

        return "index";
    }

    @RequestMapping("/user/detail/{id}")
    public String showAccount(@PathVariable("id") long id, Model model){
        Account account = accountRepository.findOne(id);

        model.addAttribute("account", accountRepository.findOne(id));
        model.addAttribute("accountNumber", account.getAccountNumber());
        model.addAttribute("balance", account.getBalance());
        return "showtransactions";
    }


    @RequestMapping("/user/withdraw")
    public String showWithdrowPage(Principal principal, Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);

        return "withdrawmoney";
    }

    @PostMapping("/user/withdraw")
    public String processWithdrawPage(Principal principal, @RequestParam String date, @RequestParam String amount,
                                      @RequestParam String description, @RequestParam String number,
                                      Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());

        try {

            DateTimeFormatter dTF = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate date1=null;
            date1 = LocalDate.parse(date, dTF);
        }catch (Exception e) {

            model.addAttribute("error" , true);
            model.addAttribute("error_message", "Please enter a valid date. Try again!");
            model.addAttribute("user", user);

            return "withdrawmoney";
         }

        try{

            double withdrawAmount=Double.parseDouble(amount);
            Account account = accountRepository.findByAccountNumber(number);
            double balance = account.getBalance();
            if(withdrawAmount > balance){
                model.addAttribute("error" , true);
                model.addAttribute("error_message", "The amount withdrawn must be equal or lower then the account actual balance. Try again!");
                model.addAttribute("user", user);

                return "withdrawmoney";
            }

            double newBalance = account.getBalance() - withdrawAmount;

            Transaction transaction = new Transaction();
            transaction.setDate(date);
            transaction.setAccountBalance(newBalance);
            transaction.setAction("Debit");
            transaction.setAccountNumber(account.getAccountNumber());
            transaction.setAmount(withdrawAmount);
            transaction.setDescription(description);
            transaction.addAccount(account);
            transactionRepository.save(transaction);

            account.setBalance(newBalance);
            account.addTransaction(transaction);
            accountRepository.save(account);

        }catch (Exception e) {
                 model.addAttribute("error" , true);
                model.addAttribute("error_message", "Enter a valid withdraw amount. Try again!");
                model.addAttribute("user", user);

                return "withdrawmoney";
            }

        model.addAttribute("bankuser", user);

        return "index";
    }

    @RequestMapping("/user/deposit")
    public String showdepositPage(Principal principal, Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "depositmoney";
    }


    @PostMapping("/user/deposit")
    public String processDepositPage(Principal principal, @RequestParam String date, @RequestParam String amount,
                                      @RequestParam String description, @RequestParam String number,
                                      Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());
        try {

            DateTimeFormatter dTF = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate date1=null;
            date1 = LocalDate.parse(date, dTF);
        }catch (Exception e) {

            model.addAttribute("error" , true);
            model.addAttribute("error_message", "Please enter a valid date. Try again!");
            model.addAttribute("user", user);

            return "depositmoney";
        }

        try{

            double depositAmount=Double.parseDouble(amount);

            Account account = accountRepository.findByAccountNumber(number);

            double newBalance = account.getBalance() + depositAmount;

            Transaction transaction = new Transaction();
            transaction.setDate(date);
            transaction.setAccountBalance(newBalance);
            transaction.setAction("Credit");
            transaction.setAccountNumber(account.getAccountNumber());
            transaction.setAmount(depositAmount);
            transaction.setDescription(description);
            transaction.addAccount(account);

            transactionRepository.save(transaction);

            account.setBalance(newBalance);
            account.addTransaction(transaction);

            accountRepository.save(account);

        }catch (Exception e) {

                 model.addAttribute("error" , true);
                model.addAttribute("error_message", "Enter a valid deposit amount. Try again!");
                model.addAttribute("user", user);

                return "depositmoney";
            }

        model.addAttribute("bankuser", user);
        return "index";
    }

    @RequestMapping("/user/transfer")
    public String showTransferPage(Principal principal, Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "transfermoney";
    }

    @PostMapping("/user/transfer")
    public String processTransferPage(Principal principal, @RequestParam String accountnumber1,
                                      @RequestParam String accountnumber2, @RequestParam String transferamount,
                                      @RequestParam String transferdate, Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());
        try {

            DateTimeFormatter dTF = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate date1=null;
            date1 = LocalDate.parse(transferdate, dTF);
        }catch (Exception e) {

            model.addAttribute("error" , true);
            model.addAttribute("error_message", "Please enter a valid date. Try again!");
            model.addAttribute("user", user);

            return "transfermoney";
        }

        try{

            double amount = Double.parseDouble(transferamount);

            Account account1 = accountRepository.findByAccountNumber(accountnumber1);
            Account account2 = accountRepository.findByAccountNumber(accountnumber2);

            double balance1 = account1.getBalance();
            double balance2 = account2.getBalance();

            if(amount > balance1){
                model.addAttribute("error" , true);
                model.addAttribute("error_message", "The amount to transfer must be lower or equal to the account actual balance. Try again!");
                model.addAttribute("user", user);

                return "transfermoney";
            }

            if(accountnumber1.equalsIgnoreCase(accountnumber2)){
                model.addAttribute("error" , true);
                model.addAttribute("error_message", "No Transfer between the same account. Try again!");
                model.addAttribute("user", user);

                return "transfermoney";
            }

            account1.setBalance(balance1-amount);
            account2.setBalance(balance2+amount);

            Transaction transaction1 = new Transaction();
            transaction1.setDate(transferdate);
            transaction1.setAccountBalance(balance1-amount);
            transaction1.setAction("Debit");
            transaction1.setAccountNumber(accountnumber1);
            transaction1.setAmount(amount);
            transaction1.setDescription("transfer to "+ accountnumber2 );
            transaction1.addAccount(account1);

            transactionRepository.save(transaction1);
            account1.addTransaction(transaction1);
            accountRepository.save(account1);

            Transaction transaction2 = new Transaction();
            transaction2.setDate(transferdate);
            transaction2.setAccountBalance(balance2+amount);
            transaction2.setAction("Credit");
            transaction2.setAccountNumber(accountnumber2);
            transaction2.setAmount(amount);
            transaction2.setDescription("transfer from "+ accountnumber1 );
            transaction2.addAccount(account2);

            transactionRepository.save(transaction2);
            account2.addTransaction(transaction2);
            accountRepository.save(account2);


        }catch (Exception e) {
               model.addAttribute("error" , true);
                model.addAttribute("error_message", "Enter a valid transfer amount. Try again!");
                model.addAttribute("user", user);

                return "transfermoney";
        }

        model.addAttribute("bankuser", user);
        return "index";
    }

    @RequestMapping("/user/newaccount")
    public String showCreateAccountPage(Principal principal, Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "addaccount";
    }

    @PostMapping("/user/newaccount")
    public String processCreateAccountPage(Principal principal, @RequestParam String accountnumber,
                                           @RequestParam String accountbalance, Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());

        try {

            if (accountRepository.findByAccountNumber(accountnumber) == null) {
                double balance = Double.parseDouble(accountbalance);

                Account account = new Account(accountnumber);
                account.setBalance(balance);
                account.addUser(user);
                accountRepository.save(account);

                user.addAccount(account);
                userRepository.save(user);
            }else{
                model.addAttribute("error" , true);
                model.addAttribute("error_message", "Account number already exists. Try again!");
                model.addAttribute("user", user);

                return "addaccount";

            }


        }catch (Exception e) {
            model.addAttribute("error" , true);
            model.addAttribute("error_message", "Enter a valid credentials. Try again!");
            model.addAttribute("user", user);

            return "addaccount";


        }

         model.addAttribute("bankuser", user);

        return "index";
    }

    @RequestMapping("/user/listaccounts")
    public String showAccountsPage(Principal principal, Model model) {

        BankUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "showaccounts";
    }

    //----------------------------------------------------------------------------------------



}
