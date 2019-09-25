package com.electroeing.designpatterns.singleton;

class InnerStatic {
    private InnerStatic() {

    }

    private static class Impl {
        private static final InnerStatic INSTANCE = new InnerStatic();
    }

    public InnerStatic getInstance() {
        return Impl.INSTANCE;
    }
}

public class InnerStaticSingleton {
}
