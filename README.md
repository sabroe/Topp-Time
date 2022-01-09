# Topp Time

The Topp Time library addresses timelines in Java.

## Purpose

Full control of Clock instances and timelines:

1. Independence of Java VM settings.
2. Coexistence of multiple clocks, possibly in a hierarchical organization.
3. Control of the progress of time including speed and granularity.
4. Textual configuration of clocks.
5. Builder support for the construction of clocks.
6. Registration of clocks by name.
7. Test-specific manipulation of time and timelines.

## Dependencies

* SLF4J
* Lombok
* Java SE 17

## TODO

The current TODO list before release to Maven Central:

1. Create demo-project aimed at a minimal Spring Boot setup.\
   Spring beans are to be instances of either Clock of Supplier<Clock>.\
   Both types have advantages, the final verdict is not giveen, maybe poth should be possible?
   1. With Clock you know that you have something workable at hand.
   2. With a Supplier there are better possibilities of lazy initialization and the initialization may follow a pattern of usage and not dictated by Spring.
2. Textual configuration may be read -- can it also be written?\
   A question here is -- how to serialize more complex compositions of Duration?
3. Balance between ClockOrigin and ClockDeclaration.\
   Complexity vs. usage of plain strings vs. strict object representation?
4. RandomClock -- does it have any place of usage?\
   How to implement this -- set up a fixed range with a discrete set of outcomes?\
   This may be a kludge.
5. Rename of AdHocDemo to something more... swallow-able!
6. Example of using AdjustableClock?\
   How about some non-linear or super-linear progress?
7. Register of Clock instances?\
   This is indicated by ClockRegister. The intention is to have named clocks registered and accessible and with relying upon high-level abstraction like Spring in particular.
8. Example of using ClockRegister.
9. Documentation?\
   Explanations in JavaDoc and/or README-files.\
   References to example-programs.
