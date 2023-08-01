package org.api.utils.entitiesSeed;

import org.api.entities.UserRoleEntity;
import org.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EntitiesSeedExecutor {
    private final RoleRepository roleRepository;

    @Autowired
    public EntitiesSeedExecutor(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void insertEntities() {

        // Role initialisation
        if(roleRepository.count() == 0L ){
            UserRoleEntity role1 = new UserRoleEntity();
            UserRoleEntity role2 = new UserRoleEntity();

            role1.setAuthority("ROLE_ADMIN");
            role2.setAuthority("ROLE_USER");
            this.roleRepository.save(role1);
            this.roleRepository.save(role2);
        }
    }
}
