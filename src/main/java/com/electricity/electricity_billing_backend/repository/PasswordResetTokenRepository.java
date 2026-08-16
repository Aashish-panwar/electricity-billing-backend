package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.PasswordResetToken;
import com.electricity.electricity_billing_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}
