package com.electricity.electricity_billing_backend.config;


import com.electricity.electricity_billing_backend.entity.Role;
import com.electricity.electricity_billing_backend.enums.RoleType;
import com.electricity.electricity_billing_backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        for (RoleType roleType : RoleType.values()) {

            roleRepository.findByName(roleType)
                    .orElseGet(() ->
                            roleRepository.save(
                                    Role.builder()
                                            .name(roleType)
                                            .build()
                            ));
        }

        System.out.println("Default roles initialized.");
    }
}