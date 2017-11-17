package com.example.bankingproject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


// Transctional allow everything to go through
//EService is not an entity
@Transactional
@Service

//These are the details in my database I want you to allow and create access
//takes object from deabates and see the roles and here they are check and make sure username and pasword are correct if it is correct allow


//This is a user object implemments Username and tokens and autorities just the not the actuall detials from the user like email,phone number
//implemmentating certian methods that must be vaild for user
public class SSUserDetailsService implements UserDetailsService {

    //    information can be passed back
    private UserRepository userRepository;

    public SSUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    //Creating a UserDetials Serivce file you must have this method inside class
    //This is the username that I have and this is what Spring needs to do
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Username String returns userDetials or userdata
        try{
            //Find usernamefrom database
            BankUser bankuser = userRepository.findByUsername(username);
            if(bankuser==null)
            {
                return null;
            }
            //Creates and new user from depency from pon file username is all from the entity
            //Translates our user to spring user using information from the dtatbase
            return new org.springframework.security.core.userdetails.User(bankuser.getUsername(),bankuser.getPassword(),getAuthorities(bankuser));

        }catch (Exception e)
        {

        }
        return null;
    }
    //Creates Roles for spring sercurity
    //Granted authority's allows
    private Set<GrantedAuthority> getAuthorities(BankUser user) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for(UserRole eachRole:user.getRoles())
        {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(eachRole.getRole());
            authorities.add(grantedAuthority);
            System.out.println("Granted Authority"+grantedAuthority.toString());
        }
        return authorities;
    }
}