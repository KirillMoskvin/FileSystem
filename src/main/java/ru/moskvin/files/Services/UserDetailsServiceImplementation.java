//package ru.moskvin.files.Services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import ru.moskvin.files.models.Role;
//import ru.moskvin.files.models.User;
////import ru.moskvin.files.models.UserRepository;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class UserDetailsServiceImplementation implements UserDetailsService {
//    @Autowired
////    private UserRepository repository;
//
//    //    @PostConstruct
////    public void init(){
////        if(!userDao.findByUserName("user").isPresent()){
////            userDao.save(User.builder()
////                .username("user")
////                .password("password")
////                .authorities(ImmutableList.of(Role.USER))
////                    .accountExpired(false)
////                    .accountLocked(false)
////                    .credentialsExpired(false)
////                    .disabled(false)
////                    .build()
////            );
////        }
////    }
//
//    @Override
//    @org.springframework.transaction.annotation.Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        //    return userDao.findByUserName(username).orElse(null);
//
//        User user = repository.findByUsername(username);
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//
//        for (Role role : user.getRoles()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//                grantedAuthorities);
////      /* return User.builder().username(username)
////                .password(new BCryptPasswordEncoder().encode("password"))
////                .authorities(ImmutableList.of(Role.USER))
////                .accountExpired(false)
////                .accountLocked(false)
////                .credentialsExpired(false)
////                .disabled(false)
////                .build(); */
//    }
//}
