package com.example.planeo_back.infrastructure.adapter.repository.useraccount;

import com.example.planeo_back.domain.enums.UserAccountStatus;
import com.example.planeo_back.infrastructure.adapter.repository.entity.UserAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface JpaUserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByUsername(String username);

    boolean existsByUsernameAndStatusNot(String username, UserAccountStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserAccount u WHERE u.id = :id")
    Optional<UserAccount> findByIdForUpdate(@Param("id") String id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE UserAccount u
               SET u.status = com.example.planeo_back.domain.enums.UserAccountStatus.DELETED,
                   u.username = NULL,
                   u.deletedAt = :deletedAt
             WHERE u.id = :id
               AND u.status = com.example.planeo_back.domain.enums.UserAccountStatus.PENDING_DELETION
            """)
    int markDeleted(@Param("id") String id, @Param("deletedAt") Instant deletedAt);
}
