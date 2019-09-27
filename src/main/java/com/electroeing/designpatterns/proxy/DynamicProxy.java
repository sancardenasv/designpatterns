package com.electroeing.designpatterns.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

interface Human {
    void walk();
    void talk();
}

class Person implements Human {
    private static final Logger logger = LoggerFactory.getLogger(Person.class);

    @Override
    public void walk() {
        logger.info("I am walking");
    }

    @Override
    public void talk() {
        logger.info("I am talking");
    }
}

class LoggingHondler implements InvocationHandler {
    private final Object target;
    private Map<String, Integer> calls = new HashMap<>();

    public LoggingHondler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        if (methodName.contains("toString")) {
            return calls.toString();
        }
        calls.merge(methodName, 1, Integer::sum);
        return method.invoke(target, args);
    }
}

public class DynamicProxy {
    private static final Logger logger = LoggerFactory.getLogger(DynamicProxy.class);

    @SuppressWarnings("unchecked")
    public static <T> T withLogging(T target, Class<T> itf) {
        return (T) Proxy.newProxyInstance(itf.getClassLoader(), new Class<?>[] {itf}, new LoggingHondler(target));
    }

    public static void main(String[] args) {
        Person person = new Person();
        Human logging = withLogging(person, Human.class);
        logging.talk();
        logging.walk();
        logging.walk();

        logger.info(logging.toString());
    }
}
