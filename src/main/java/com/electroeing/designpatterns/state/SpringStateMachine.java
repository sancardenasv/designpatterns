package com.electroeing.designpatterns.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

enum States {
    OFF_HOOK, // starting
    ON_HOOK, // ending
    CONNECTING,
    CONNECTED,
    ON_HOLD
}

enum Events {
    CALL_DIALED,
    HUNG_UP,
    CALL_CONNECTED,
    PLACED_ON_HOLD,
    TAKEN_OFF_HOLD,
    LEFT_MESSAGE,
    STOP_USING_PHONE
}

public class SpringStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(SpringStateMachine.class);

    private static StateMachine<States, Events> buildMachine() throws Exception {
        final StateMachineBuilder.Builder<States, Events> builder = StateMachineBuilder.builder();

        builder.configureStates()
                .withStates()
                    .initial(States.OFF_HOOK)
                    .states(EnumSet.allOf(States.class));

        builder.configureTransitions()
                .withExternal()
                    .source(States.OFF_HOOK)
                    .event(Events.CALL_DIALED)
                    .target(States.CONNECTING)
                    .and()
                .withExternal()
                    .source(States.OFF_HOOK)
                    .event(Events.STOP_USING_PHONE)
                    .target(States.ON_HOOK)
                    .and()
                .withExternal()
                    .source(States.CONNECTING)
                    .event(Events.HUNG_UP)
                    .target(States.OFF_HOOK)
                    .and()
                .withExternal()
                    .source(States.CONNECTING)
                    .event(Events.CALL_CONNECTED)
                    .target(States.CONNECTED)
                    .and()
                .withExternal()
                    .source(States.CONNECTED)
                    .event(Events.LEFT_MESSAGE)
                    .target(States.OFF_HOOK)
                    .and()
                .withExternal()
                    .source(States.CONNECTED)
                    .event(Events.HUNG_UP)
                    .target(States.OFF_HOOK)
                    .and()
                .withExternal()
                    .source(States.CONNECTED)
                    .event(Events.PLACED_ON_HOLD)
                    .target(States.OFF_HOOK)
                    .and()
                .withExternal()
                    .source(States.ON_HOLD)
                    .event(Events.TAKEN_OFF_HOLD)
                    .target(States.CONNECTED)
                    .and()
                .withExternal()
                    .source(States.ON_HOLD)
                    .event(Events.HUNG_UP)
                    .target(States.OFF_HOOK);


        return builder.build();
    }

    public static void main(String[] args) throws Exception {
        final StateMachine<States, Events> machine = buildMachine();
        machine.start();

        States exitState = States.ON_HOOK;

        final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            State<States, Events> state = machine.getState();

            logger.info("The phone is currently {}", state.getId());
            logger.info("Select a trigger:");

            final List<Transition<States, Events>> ts = machine.getTransitions()
                    .stream().filter(t -> t.getSource() == state).collect(Collectors.toList());

            for (int i = 0; i < ts.size(); i++) {
                logger.info("{}. {}", i, ts.get(i).getTrigger().getEvent());
            }

            boolean parseOk;
            int choice = 0;
            do {
                try {
                    logger.info("Please enter your choice:");
                    choice = Integer.parseInt(console.readLine());
                    parseOk = choice >= 0 && choice < ts.size();
                } catch (Exception e) {
                    parseOk = false;
                }
            } while (!parseOk);
            // perform the transition
            machine.sendEvent(ts.get(choice).getTrigger().getEvent());

            if (machine.getState().getId() == exitState) {
                break;
            }
        }
        logger.info("And we are done!");
    }
}
