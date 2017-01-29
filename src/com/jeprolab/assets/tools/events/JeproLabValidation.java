package com.jeprolab.assets.tools.events;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public @interface JeproLabValidation {
    enum Requirement { MUST_BE_VALIDATE, MAY_BE_VALIDATE, NEVER_VALIDATE }
    Requirement value() default Requirement.MAY_BE_VALIDATE;
}
