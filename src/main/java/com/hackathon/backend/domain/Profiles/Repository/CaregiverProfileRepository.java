package com.hackathon.backend.domain.Profiles.Repository;

import com.hackathon.backend.domain.Profiles.Entity.CaregiverProfile;
import com.hackathon.backend.domain.Profiles.Entity.DisabledProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverProfileRepository extends JpaRepository<CaregiverProfile, Long> {
}
