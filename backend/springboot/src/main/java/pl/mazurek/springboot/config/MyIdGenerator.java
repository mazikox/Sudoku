package pl.mazurek.springboot.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class MyIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "PB_PAYEE_";
    private static final AtomicInteger COUNTER = new AtomicInteger(0);


    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return PREFIX + COUNTER.incrementAndGet();
    }
}