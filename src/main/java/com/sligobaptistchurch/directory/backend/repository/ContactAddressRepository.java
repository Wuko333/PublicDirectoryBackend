package com.sligobaptistchurch.directory.backend.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sligobaptistchurch.directory.backend.domain.ContactAddress;
import com.sligobaptistchurch.directory.backend.domain.PersonContact;

@Repository
public interface ContactAddressRepository extends JpaRepository<ContactAddress,Long>{
	Set<ContactAddress> findByAddressline1ContainingIgnoreCase(String addressLine1);
	Set<ContactAddress> findByAddressline2ContainingIgnoreCase(String addressLine2);
	Set<ContactAddress> findByCityContainingIgnoreCase(String city);
	Set<ContactAddress> findByProvinceContainingIgnoreCase(String province);
	Set<ContactAddress> findByStateContainingIgnoreCase(String state);
	Set<ContactAddress> findByZipcode(Short zipcode);
	ContactAddress findByPersonContact(PersonContact personContact);
}
