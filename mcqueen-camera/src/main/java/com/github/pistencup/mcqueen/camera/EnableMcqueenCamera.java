package com.github.pistencup.mcqueen.camera;


import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({CameraConfig.class, CameraRegistrar.class})
public @interface EnableMcqueenCamera {
    /**
     * Alias for the {@link #basePackages()} attribute
     * @return the array of 'basePackages'.
     */
    String[] value() default {};

    /**
     * Base packages for CameraAspect's work.
     * @return the array of 'basePackages'.
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()}. Base packages for CameraAspect's work.
     * @return the array of 'basePackageClasses'.
     */
    Class<?>[] basePackageClasses() default {};
}
