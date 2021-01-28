package com.sligobaptistchurch.directory.backend.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sligobaptistchurch.directory.backend.security.models.Role;
import com.sligobaptistchurch.directory.backend.security.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	Set<User> findByRolesId(Long id);
}
