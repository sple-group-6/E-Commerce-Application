package com.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Membership;

@Repository
public interface MembershipRepo extends JpaRepository<Membership, Long> {
	Optional<Membership> findByMembershipCode(String membershipCode);
}
