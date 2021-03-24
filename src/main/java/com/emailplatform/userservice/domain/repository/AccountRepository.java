package com.emailplatform.userservice.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.emailplatform.userservice.domain.entity.AccountEntity;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    List<AccountEntity> findByUserUserReference(String userReference);

    Optional<AccountEntity> findByAccountReferenceAndUserUserReference(String accountReference, String userReference);

    Optional<AccountEntity> findByAccountReference(String accountReference);
}
