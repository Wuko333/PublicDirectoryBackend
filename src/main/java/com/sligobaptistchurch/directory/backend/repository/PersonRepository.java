package com.sligobaptistchurch.directory.backend.repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sligobaptistchurch.directory.backend.domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	Set<Person> findByFirstnameContainingIgnoreCase(String firstName);
	Set<Person> findByMiddlenameContainingIgnoreCase(String middleName);
	Set<Person> findByNicknameContainingIgnoreCase(String nickName);
	Set<Person> findByLastnameContainingIgnoreCase(String lastName);
	
}
