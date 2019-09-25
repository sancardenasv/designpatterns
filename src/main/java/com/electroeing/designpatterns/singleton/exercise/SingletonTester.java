package com.electroeing.designpatterns.singleton.exercise;

import java.util.function.Supplier;

class SingletonTester
{
    public static boolean isSingleton(Supplier<Object> func)
    {
        return func.get().equals(func.get());
    }

    public static void main(String[] args) {
        boolean singleton = isSingleton(new Supplier<Object>() {
            @Override
            public Object get() {
                return InnerStatic.getInstance();
            }
        });

        System.out.println("Is Singleton: " + singleton);

        boolean singleton2 = isSingleton(new Supplier<Object>() {
            @Override
            public Object get() {
                return new None();
            }
        });

        System.out.println("Is Singleton: " + singleton2);

    }
}

class None {
    int i = 1;
}

class InnerStatic {
    private InnerStatic() {

    }

    private static class Impl {
        private static final InnerStatic INSTANCE = new InnerStatic();
    }

    public static Object getInstance() {
        return InnerStatic.Impl.INSTANCE;
    }

}