package com.example.bankingproject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
//Prevent cross authitication from make sure its sent from the same browser you are using
//Dosnt include a token that this is me cant veifyCSRF token inside form that authicates you within the browsr
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    SSUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    //Creating a bean to authiticate user and access in spring dont ned to know in depth
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUserDetailsService(userRepository);
        //Works with Login form
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                //I have a custom login form, but why can't I see my CSS?
                .antMatchers("/css/**","/js/**","/img/**","/h2-console/**",
                        "/vendor/**", "/pug/**", "/scss/**",
                        "/", "/homepage", "/registeruser", "/registermanager","/contact").permitAll()
                .antMatchers("/welcome").access("hasAuthority('CUSTOMER') or hasAuthority('MANAGER')")
                .antMatchers( "/user/**").access("hasAuthority('CUSTOMER')")
                .antMatchers( "/manager/**").access("hasAuthority('MANAGER')")
                //Want to see all different levels must alow secuirty to access these folders and make these accessible to anyone ion the browser
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .formLogin().defaultSuccessUrl("/welcome",true)//Ading chaining for allhttp sercueity
                //Allows sucessful logout
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll().permitAll();


    }

//    .httpBasic() This means that the user can avoid a login prompt by putting his/her login details in the request.
//    This can be used for testing, but should be removed before the application goes live.
//
// configure() This overrides the default configure method, configures users who can access the application. By
//    default, Spring Boot will provide a new random password assigned to the user "user" when it starts up, if you
//do not include this method.
//
//    Once you include this method, you will be able to log in with the users configured here. At this point, the
//    configuration is for a single in-memory user. Multiple users can be configured here, as you wi\\ see when you
//    remove the comments in the additional code.

    @Override
    //Going to see whether detials have been passed in and see wheteher this info is passed too login form is passable or not based on this structure
    //In memeory authentitcation
    //allow authitication from database determine if user if has access or not.
    protected void configure (AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("user").password("password").authorities("CUSTOMER");
        auth.inMemoryAuthentication().withUser("manager").password("password").authorities("MANAGER");
        auth.userDetailsService(userDetailsServiceBean());
    }
}