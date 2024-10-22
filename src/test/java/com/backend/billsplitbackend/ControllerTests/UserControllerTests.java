package com.backend.billsplitbackend.ControllerTests;


import com.backend.billsplitbackend.Config.JwtProvider;
import com.backend.billsplitbackend.Controller.UserController;
import com.backend.billsplitbackend.Entity.User;
import com.backend.billsplitbackend.Repository.UserRepository;
import com.backend.billsplitbackend.Service.UserServiceImplementation;
import com.backend.billsplitbackend.response.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTests {

	  @InjectMocks
	  private UserController userController;

	  @Mock
	  private UserRepository userRepository;

	  @Mock
	  private PasswordEncoder passwordEncoder;

	  @Mock
	  private UserServiceImplementation userService;

	  @Mock
	  private JwtProvider jwtProvider;

	  @Mock
	  private AuthenticationManager authenticationManager;

	  @BeforeEach
	  void setUp() {
			MockitoAnnotations.initMocks(this);
	  }

	  @Test
	  void testSignupSuccess() {
			User user = new User();
			user.setEmail("test@test.com");
			user.setPassword("password");
			user.setUsername("Test User");
			user.setRole("USER");

			when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
			when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded_password");
			when(userRepository.save(any(User.class))).thenReturn(user);

			Authentication authentication = mock(Authentication.class);
			when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
					.thenReturn(authentication);

			ResponseEntity<AuthResponse> response = userController.createUserHandler(user);

			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals("Register Success", response.getBody().getMessage());
	  }

	  @Test
	  void testSignupEmailAlreadyExists() {
			User user = new User();
			user.setEmail("test@test.com");

			when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

			Exception exception = assertThrows(BadCredentialsException.class, () ->
					userController.createUserHandler(user));

			assertEquals("email address already exists ", exception.getMessage());
	  }

//	  @Test
//	  void testSigninSuccess() {
//			User loginRequest = new User();
//			loginRequest.setEmail("test@test.com");
//			loginRequest.setPassword("password");
//
//			Authentication authentication = mock(Authentication.class);
//			when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//					.thenReturn(authentication);
//			when(jwtProvider.generateToken(authentication)).thenReturn("jwt_token");
//
//			ResponseEntity<AuthResponse> response = userController.signin(loginRequest);
//
//			assertEquals(HttpStatus.OK, response.getStatusCode());
//			assertEquals("Login success", response.getBody().getMessage());
//	  }
}