package com.emailplatform.userservice.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.emailplatform.userservice.domain.entity.QuotaEntity;
import com.emailplatform.userservice.domain.entity.QuotaType;

@Repository
public interface QuotaRepository extends CrudRepository<QuotaEntity, Long> {

    Optional<QuotaEntity> findByType(QuotaType type);
}
