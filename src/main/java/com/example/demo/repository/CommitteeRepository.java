package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Committee;

@Repository("committeerepo")
public interface CommitteeRepository extends JpaRepository<Committee, Long> {

	@Query("UPDATE Committee c SET c.committeeName=:name WHERE c.committeeId=:id")
	@Modifying
	public int updateCommittee(Long id, String name);

	Optional<Committee> findByCommitteeName(String committeeName);
}
