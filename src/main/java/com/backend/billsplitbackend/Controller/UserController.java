package com.backend.billsplitbackend.Controller;

import com.backend.billsplitbackend.Config.JwtProvider;
import com.backend.billsplitbackend.Entity.User;
import com.backend.billsplitbackend.Repository.UserRepository;
import com.backend.billsplitbackend.Service.UserServiceImplementation;
import com.backend.billsplitbackend.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserServiceImplementation customUserDetails;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getUsername();
        String role = user.getRole();

        Optional<User> isEmailExist = userRepository.findUserByEmail(email);
        if (isEmailExist.isPresent()) {
            //throw new Exception("Email Is Already Used With Another Account");
            System.out.println("email exist with name : " + email);
            throw new BadCredentialsException("email address already exists ");
        }
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setUsername(fullName);
        createdUser.setRole(role);
        createdUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(createdUser);
        userRepository.save(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(email + "-------" + password);

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login success");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    private Authentication authenticate(String email, String password) {
        try {
            // This will throw UsernameNotFoundException if user is not found
            UserDetails userDetails = customUserDetails.loadUserByUsername(email);

            // Log details for debugging
            System.out.println("User Details: " + userDetails);
            System.out.println("Provided Password: " + password);
            System.out.println("Stored Encoded Password: " + userDetails.getPassword());

            // Check password
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                System.out.println("Password mismatch for user: " + email);
                throw new BadCredentialsException("Invalid password");
            }

            // Create authentication token
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

        } catch (UsernameNotFoundException e) {
            // More specific logging
            System.out.println("User not found: " + email);
            throw new BadCredentialsException("Invalid email and password", e);
        }
    }


}