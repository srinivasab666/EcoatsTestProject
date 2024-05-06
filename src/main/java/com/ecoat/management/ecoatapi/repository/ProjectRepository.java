package com.ecoat.management.ecoatapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	Optional<Project> findByProjectName(String projectName);
	
	@Query(value="select project from Project project where project.corporate.id=?1")
    Optional<List<Project>> findProjectsByCorpId(long corpId);

	@Query(value="select p from Project p where p.corporate.id=?1 and p.client.clientId=?2 and p.projectName=?3")
	Optional<Project> findProjectsByCorpIdAndClientId(long corpId,long clientId,String projectName);
}
