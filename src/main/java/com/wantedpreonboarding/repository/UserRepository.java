package com.wantedpreonboarding.repository;

import com.wantedpreonboarding.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserIdAndDeleteDtNull(Long userId);
    Optional<UserEntity> findByEmailAndDeleteDtNull(String email);
}
