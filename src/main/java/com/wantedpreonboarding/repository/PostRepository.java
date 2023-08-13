package com.wantedpreonboarding.repository;

import com.wantedpreonboarding.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findByDeleteDtNullOrderByCreateDtDesc(Pageable pageable);
}
