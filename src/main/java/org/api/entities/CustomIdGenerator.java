package org.api.entities;

import org.api.constants.ConstantDate;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String tableName = object.getClass().getSimpleName().toUpperCase();
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        return tableName + uuid;
    }

}
