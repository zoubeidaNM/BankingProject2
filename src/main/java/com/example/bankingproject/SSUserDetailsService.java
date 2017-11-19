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


public class SSUserDetailsService implements UserDetailsService {

    //    information can be passed back
    private UserRepository userRepository;

    public SSUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Username String returns userDetials or userdata
       // try{
            //Find usernamefrom database
            BankUser bankuser = userRepository.findByUsername(username);
            if(bankuser==null)
            {
                throw new UsernameNotFoundException("No user found with username: " + username);

                //return null;
            }
            //Creates and new user from depency from pon file username is all from the entity
            //Translates our user to spring user using information from the dtatbase

//        }catch (Exception e)
//        {
//
//        }

        return new org.springframework.security.core.userdetails.User(bankuser.getUsername(),bankuser.getPassword(),getAuthorities(bankuser));

        //return null;
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