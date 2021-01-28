package com.sligobaptistchurch.directory.backend.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sligobaptistchurch.directory.backend.domain.Person;
import com.sligobaptistchurch.directory.backend.domain.PersonContact;

@Repository
public interface PersonContactRepository extends JpaRepository<PersonContact, Long> {
	Set<PersonContact> findByPerson(Person person);
}
