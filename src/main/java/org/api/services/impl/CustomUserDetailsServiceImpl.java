package org.api.services.impl;


import org.api.annotation.LogExecutionTime;
import org.api.entities.UserEntity;
import org.api.repository.UserEntityRepository;
import org.api.services.CustomUserDetailsService;
import org.api.utils.ApiValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userEntityRepository.findOneByMail(mail);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("No user found with mail:" + mail);
        }

        return new CustomUserDetailsService(userEntity.get());
    }

}
