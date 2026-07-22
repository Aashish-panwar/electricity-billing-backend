package com.electricity.electricity_billing_backend.repository;
import com.electricity.electricity_billing_backend.entity.Role;
import com.electricity.electricity_billing_backend.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
}
