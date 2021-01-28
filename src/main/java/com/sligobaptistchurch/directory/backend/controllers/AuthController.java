package com.sligobaptistchurch.directory.backend.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sligobaptistchurch.directory.backend.repository.RoleRepository;
import com.sligobaptistchurch.directory.backend.repository.UserRepository;
import com.sligobaptistchurch.directory.backend.security.jwt.JwtUtils;
import com.sligobaptistchurch.directory.backend.security.models.ERole;
import com.sligobaptistchurch.directory.backend.security.models.Role;
import com.sligobaptistchurch.directory.backend.security.models.User;
import com.sligobaptistchurch.directory.backend.security.payload.request.LoginRequest;
import com.sligobaptistchurch.directory.backend.security.payload.request.SignupRequest;
import com.sligobaptistchurch.directory.backend.security.payload.response.JwtResponse;
import com.sligobaptistchurch.directory.backend.security.payload.response.MessageResponse;
import com.sligobaptistchurch.directory.backend.security.services.UserDetailsImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt,
												userDetails.getId(),
												userDetails.getUsername(),
												userDetails.getEmail(),
												roles));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		
		//Create new user's account, automatically give him a guest role
		User user = new User(signupRequest.getUsername(),
							 signupRequest.getEmail(),
							 encoder.encode(signupRequest.getPassword()));
		
		
		Set<Role> roles = new HashSet<>();
		
		Role guestRole = roleRepository.findByName(ERole.ROLE_GUEST)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		
		roles.add(guestRole);
		
		user.setRoles(roles);
		userRepository.save(user);
		
		
		
		return ResponseEntity.ok(new MessageResponse("Thank you for signing up! An admin will process your request"
				+ " and get back to you shortly. You will be emailed upon approval."));
	}
	
	//To be removed on final deployment
	@PostMapping("/adminSignup")
	public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		
		//Create new user's account, automatically give him an admin role
		User user = new User(signupRequest.getUsername(),
							 signupRequest.getEmail(),
							 encoder.encode(signupRequest.getPassword()));
		
		
		Set<Role> roles = new HashSet<>();
		
		Role guestRole = roleRepository.findByName(ERole.ROLE_GUEST)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		
		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		
		
		
		roles.add(guestRole);
		roles.add(userRole);
		roles.add(adminRole);
		
		user.setRoles(roles);
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("Thanks for signing up fellow admin!"));
	}
	
	
}
