//package ru.moskvin.files.Services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import ru.moskvin.files.models.RoleRepository;
//import ru.moskvin.files.models.User;
//import ru.moskvin.files.models.UserRepository;
//
//import java.util.HashSet;
//
//@Service
//public class UserServiceImplementation implements UserService {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Override
//    public void save(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRoles(new HashSet<>(roleRepository.findAll()));
//        userRepository.save(user);
//    }
//
//    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//}
