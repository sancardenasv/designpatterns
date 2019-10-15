package com.electroeing.designpatterns.state;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum PhoneState {
    OFF_HOOK, // starting
    ON_HOOK, // ending
    CONNECTING,
    CONNECTED,
    ON_HOLD
}

enum PhoneTrigger {
    CALL_DIALED,
    HUNG_UP,
    CALL_CONNECTED,
    PLACED_ON_HOLD,
    TAKEN_OFF_HOLD,
    LEFT_MESSAGE,
    STOP_USING_PHONE
}

public class HandmadeStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(HandmadeStateMachine.class);
    private static Map<PhoneState, List<Pair<PhoneTrigger, PhoneState>>> rules = new HashMap<>();

    static {
        rules.put(PhoneState.OFF_HOOK,
                Arrays.asList(
                        new Pair<>(PhoneTrigger.CALL_DIALED, PhoneState.CONNECTING),
                        new Pair<>(PhoneTrigger.STOP_USING_PHONE, PhoneState.ON_HOOK)
                ));
        rules.put(PhoneState.CONNECTING,
                Arrays.asList(
                        new Pair<>(PhoneTrigger.HUNG_UP, PhoneState.OFF_HOOK),
                        new Pair<>(PhoneTrigger.CALL_CONNECTED, PhoneState.CONNECTED)
                ));
        rules.put(PhoneState.CONNECTED,
                Arrays.asList(
                        new Pair<>(PhoneTrigger.LEFT_MESSAGE, PhoneState.OFF_HOOK),
                        new Pair<>(PhoneTrigger.HUNG_UP, PhoneState.OFF_HOOK),
                        new Pair<>(PhoneTrigger.PLACED_ON_HOLD, PhoneState.ON_HOLD)
                ));
        rules.put(PhoneState.ON_HOLD,
                Arrays.asList(
                        new Pair<>(PhoneTrigger.TAKEN_OFF_HOLD, PhoneState.CONNECTED),
                        new Pair<>(PhoneTrigger.HUNG_UP, PhoneState.OFF_HOOK)
                ));
    }

    private static PhoneState currentState = PhoneState.OFF_HOOK;
    private static PhoneState exitState = PhoneState.ON_HOOK;

    public static void main(String[] args) {
        final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            logger.info("The phone is currently {}", currentState);
            final StringBuilder sb = new StringBuilder("Select a trigger: \n");
            for (int i = 0; i < rules.get(currentState).size(); i++) {
                final PhoneTrigger trigger = rules.get(currentState).get(i).getKey();
                sb.append(i).append(". ").append(trigger).append("\n");
            }
            logger.info("{}", sb);

            boolean parseOk;
            int choice = 0;
            do {
                try {
                    logger.info("Please enter your choice: ");
                    choice = Integer.parseInt(console.readLine());
                    parseOk = choice >= 0 && choice < rules.get(currentState).size();
                } catch (IOException e) {
                    parseOk = false;
                }
            } while (!parseOk);

            currentState = rules.get(currentState).get(choice).getValue();

            if (currentState == exitState) break;
        }
        logger.info("And we are done!");
    }
}
