package com.jamia.jamiaakbira.utils;

import com.jamia.jamiaakbira.domain.RequestContext;
import com.jamia.jamiaakbira.entity.Role;
import com.jamia.jamiaakbira.enumeration.Authority;
import com.jamia.jamiaakbira.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CommandLine {
    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository){
        return args -> {
            /*RequestContext.setUserId(0L);
            var userRole=new Role();
            userRole.setName(Authority.USER.name());
            roleRepository.save(userRole);

            var adminRole=new Role();
            userRole.setName(Authority.ADMIN.name());
            roleRepository.save(adminRole);

            var superAdminRole=new Role();
            userRole.setName(Authority.ADMIN.name());
            roleRepository.save(superAdminRole);

            RequestContext.start();*/
        };
    }
}
