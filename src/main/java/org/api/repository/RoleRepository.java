package org.api.repository;

import org.api.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRoleEntity, String> {
    public UserRoleEntity findByAuthority(String authority);
    public UserRoleEntity getByAuthority(String authority);
}
