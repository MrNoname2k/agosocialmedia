package org.api.services;

import org.api.entities.UserRoleEntity;

public interface RoleService {

    boolean persist(UserRoleEntity role) throws Exception;

    UserRoleEntity getByName(String name);
}
