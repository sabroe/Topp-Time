package com.yelstream.topp.time.declare;

import com.yelstream.topp.time.build.ClockConfiguration;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *   Declaration of a {@link Clock} instance.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
public class ClockDeclaration {
    @Getter
    private final String declaration;

    /**
     * Parses the declaration and creates a matching clock instance.
     * @return Clock.
     */
    public Clock toClock() {
        return parse(declaration);
    }

    private static final String CLOCK_ORIGIN_NAME_REGEX="([\\w]+)";
    private static final String ARGUMENT_NAME_REGEX="([\\w]+)";
    private static final String ARGUMENT_VALUE_REGEX="([\\p{Print}&&[^,]]*)";
    private static final String ARGUMENT_REGEX=ARGUMENT_NAME_REGEX + "(=" + ARGUMENT_VALUE_REGEX +")?";

    private static final String CLOCK_ORIGIN_NAME_GROUP_NAME="clockOriginName";
    private static final String ARGUMENTS_GROUP_NAME="arguments";
    private static final String CLOCK_DEFINITION_REGEX="^" + namedGroup(CLOCK_ORIGIN_NAME_GROUP_NAME, CLOCK_ORIGIN_NAME_REGEX) + "([(]" + namedGroup(ARGUMENTS_GROUP_NAME, ARGUMENT_REGEX + "([,]" + ARGUMENT_REGEX + ")*") + "?" + "[)])?" + "$";
    private static final Pattern CLOCK_DEFINITION_PATTERN=Pattern.compile(CLOCK_DEFINITION_REGEX);

    private static final String ARGUMENT_GROUP_NAME="argument";
    private static final String ARGUMENT_NAME_GROUP_NAME="name";
    private static final String ARGUMENT_VALUE_GROUP_NAME="value";
    private static final String ARGUMENT_ARGUMENTS_REGEX=namedGroup(ARGUMENT_GROUP_NAME, namedGroup(ARGUMENT_NAME_GROUP_NAME, ARGUMENT_NAME_REGEX) + "(=" + namedGroup(ARGUMENT_VALUE_GROUP_NAME, ARGUMENT_VALUE_REGEX) +")?");
    private static final Pattern ARGUMENT_PATTERN=Pattern.compile(ARGUMENT_ARGUMENTS_REGEX);

    private static String namedGroup(String group, String regEx) {
        return String.format("(?<%s>%s)", group, regEx);
    }

    /**
     * Creates a clock.
     * @param declaration Clock declaration.
     * @return Clock.
     */
    public static Clock createClock(String declaration) {
        return parse(declaration);
    }

    private static Clock parse(String declaration) {
        log.debug("Clock builder creation from clock declaration {}.", declaration);
        ClockConfiguration.Builder clockConfigurationBuilder=null;
        Matcher matcher=CLOCK_DEFINITION_PATTERN.matcher(declaration);
        if (!matcher.matches()) {
            log.warn("Failure to parse; cannot match syntax of clock declaration {}!", declaration);
            throw new IllegalArgumentException(String.format("Failure to parse; cannot syntax of match clock declaration %s!", declaration));
        } else {
            String clockOriginName=matcher.group(CLOCK_ORIGIN_NAME_GROUP_NAME);
            String arguments=matcher.group(ARGUMENTS_GROUP_NAME);
            log.debug("Clock builder creation from clock declaration {} read clock origin name {} and arguments {}.", declaration, clockOriginName, arguments);
            Map<String,String> argumentMap=new LinkedHashMap<>();  //Yes, keep the keys in the order they were inserted!
            argumentMap.put("origin",clockOriginName);
            if (arguments!=null) {
                parseArguments(argumentMap,arguments);
            }
            clockConfigurationBuilder=ClockConfiguration.Builder.of(argumentMap);
        }
        log.debug("Parsing of clock declaration {} completed.",declaration);
        return clockConfigurationBuilder.build().toClock();
    }

    private static void parseArguments(Map<String,String> argumentMap,
                                       String arguments) {
        Matcher matcher=ARGUMENT_PATTERN.matcher(arguments);
        int start=0;
        while (matcher.find(start)) {
            String name=matcher.group(ARGUMENT_NAME_GROUP_NAME);
            String value=matcher.group(ARGUMENT_VALUE_GROUP_NAME);
            argumentMap.put(name,value);
            start=matcher.end();
        }
    }
}
