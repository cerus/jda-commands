package com.github.kaktushose.jda.commands.dependency;

import com.github.kaktushose.jda.commands.annotations.Produces;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central registry for dependencies and producing methods. Registered dependencies will be injected with the
 * corresponding values if present, else null.
 *
 * @author Kaktushose
 * @version 2.0.0
 * @see Produces
 * @see com.github.kaktushose.jda.commands.annotations.Inject Inject
 * @since 1.0.0
 */
public class DependencyInjector {

    private final Map<Class<?>, Object> providedObjects;
    private final Map<Object, List<Field>> dependencies;
    private final Logger log = LoggerFactory.getLogger(DependencyInjector.class);

    /**
     * Create a new DependencyInjector.
     */
    public DependencyInjector() {
        this.providedObjects = new HashMap<>();
        this.dependencies = new HashMap<>();
    }

    /**
     * Scans the whole classpath for methods annotated with {@link Produces}. If found, creates a new instance of
     * the declaring class and will call the method to retrieve the object and register it as a dependency.
     *
     * @param packages package(s) to exclusively scan
     * @param clazz    a class of the classpath to scan
     */
    public void index(@NotNull final Class<?> clazz, @NotNull final String... packages) {
        this.log.debug("Indexing dependency providers...");

        final FilterBuilder filter = new FilterBuilder();
        for (final String pkg : packages) {
            filter.includePackage(pkg);
        }

        final ConfigurationBuilder config = new ConfigurationBuilder()
                .setScanners(Scanners.SubTypes, Scanners.MethodsAnnotated)
                .setUrls(ClasspathHelper.forClass(clazz))
                .filterInputsBy(filter);
        final Reflections reflections = new Reflections(config);

        final Set<Method> methods = reflections.getMethodsAnnotatedWith(Produces.class);
        for (final Method method : methods) {
            if (method.getParameterTypes().length != 0) {
                this.log.error("An error has occurred! Skipping Producer {}", method,
                        new IllegalArgumentException("Producer method must not have parameters!"));
                continue;
            }
            final Object instance;
            try {
                instance = method.getDeclaringClass().getConstructors()[0].newInstance();
            } catch (final Exception e) {
                this.log.error("Unable to create provider instance!", e);
                continue;
            }
            try {
                method.setAccessible(true);
                final Object object = method.invoke(instance);
                this.providedObjects.put(method.getReturnType(), object);
            } catch (final Exception e) {
                this.log.error("Unable to access method {}", method, e);
            }
        }
    }

    /**
     * Takes an instance of a class and scans it for methods annotated with {@link Produces}. If found, will call
     * the method to retrieve the object and register it as a dependency.
     *
     * @param provider instance of the class to scan
     */
    public void registerProvider(@NotNull final Object provider) {
        for (final Method method : provider.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Produces.class)) {
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                this.log.error("An error has occurred! Skipping Producer {}", method,
                        new IllegalArgumentException("Producer method must not have parameters!"));
                continue;
            }
            try {
                method.setAccessible(true);
                final Object object = method.invoke(provider);
                this.providedObjects.put(object.getClass(), object);
            } catch (final Exception e) {
                this.log.error("Unable to access method {}", method, e);
            }
        }
    }

    /**
     * Registers fields inside a class as dependencies.
     *
     * @param instance instance of the declaring class
     * @param fields   the dependencies to register
     */
    public void registerDependencies(@NotNull final Object instance, @NotNull final List<Field> fields) {
        this.dependencies.put(instance, fields);
    }

    /**
     * Injects all registered dependencies with the corresponding value. If no value is present {@code null} gets injected.
     */
    public void inject() {
        this.dependencies.forEach((instance, fields) -> {
            for (final Field field : fields) {
                try {
                    field.setAccessible(true);
                    field.set(instance, this.getDependency(field.getType()));
                } catch (final IllegalAccessException e) {
                    this.log.error("Unable to inject field {}", field, e);

                }
            }
        });
    }

    private Object getDependency(@Nullable final Class<?> clazz) {
        return this.providedObjects.get(clazz);
    }
}
