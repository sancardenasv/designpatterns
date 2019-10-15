package com.electroeing.designpatterns.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ClassicState {
    private static final Logger logger = LoggerFactory.getLogger(ClassicState.class);

    void on(LightSwitch ls) {
        logger.warn("Light is already ON");
    }

    void off(LightSwitch ls) {
        logger.warn("Light is already OFF");
    }
}

class OnState extends ClassicState {
    private static final Logger logger = LoggerFactory.getLogger(OnState.class);

    public OnState() {
        logger.info("Light turned on");
    }

    @Override
    void off(LightSwitch ls) {
        logger.info("Switching light off...");
        ls.setClassicState(new OffState());
    }
}

class OffState extends ClassicState {
    private static final Logger logger = LoggerFactory.getLogger(OffState.class);

    public OffState() {
        logger.info("Light turned off");
    }

    @Override
    void on(LightSwitch ls) {
        logger.info("Switching light on...");
        ls.setClassicState(new OnState());
    }
}

class LightSwitch {
    // OnState, OffState
    private ClassicState classicState;

    public LightSwitch() {
        this.classicState = new OffState();
    }

    void on() {
        classicState.on(this);
    }

    void off() {
        classicState.off(this);
    }

    public ClassicState getClassicState() {
        return classicState;
    }

    public void setClassicState(ClassicState classicState) {
        this.classicState = classicState;
    }
}

public class ClassicImplementation {
    private static final Logger logger = LoggerFactory.getLogger(ClassicImplementation.class);

    public static void main(String[] args) {
        final LightSwitch lightSwitch = new LightSwitch();
        lightSwitch.on();
        lightSwitch.off();
        lightSwitch.off();
    }

}
