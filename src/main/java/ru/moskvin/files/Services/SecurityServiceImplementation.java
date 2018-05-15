package ru.moskvin.files.Services;

//import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//import ru.moskvin.files.models.UserRepository;

//@Service
//public class SecurityServiceImplementation implements SecurityService{
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public String findLoggedInUsername() {
//        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (userDetails instanceof UserDetails){
//            return ((UserDetails)userDetails).getUsername();
//        }
//        return null;
//    }
//
//    @Override
//    public void autologin(String username, String password) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//
//        if (usernamePasswordAuthenticationToken.isAuthenticated()){
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        }
//    }
//}
//