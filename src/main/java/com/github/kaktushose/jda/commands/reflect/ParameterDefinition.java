package com.github.kaktushose.jda.commands.reflect;

import com.github.kaktushose.jda.commands.annotations.Concat;
import com.github.kaktushose.jda.commands.annotations.Optional;
import com.github.kaktushose.jda.commands.annotations.constraints.Constraint;
import com.github.kaktushose.jda.commands.annotations.constraints.Max;
import com.github.kaktushose.jda.commands.annotations.constraints.Min;
import com.github.kaktushose.jda.commands.annotations.interactions.Choices;
import com.github.kaktushose.jda.commands.annotations.interactions.Param;
import com.github.kaktushose.jda.commands.dispatching.validation.Validator;
import com.github.kaktushose.jda.commands.dispatching.validation.ValidatorRegistry;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Representation of a command parameter.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see Concat
 * @see Optional
 * @see Constraint
 * @see Choices
 * @see Param
 * @since 2.0.0
 */
public class ParameterDefinition {

    private static final Map<Class<?>, Class<?>> TYPE_MAPPINGS = new HashMap<Class<?>, Class<?>>() {
        {
            this.put(byte.class, Byte.class);
            this.put(short.class, Short.class);
            this.put(int.class, Integer.class);
            this.put(long.class, Long.class);
            this.put(double.class, Double.class);
            this.put(float.class, Float.class);
            this.put(boolean.class, Boolean.class);
            this.put(char.class, Character.class);
        }
    };

    private static final Map<Class<?>, OptionType> OPTION_TYPE_MAPPINGS = new HashMap<Class<?>, OptionType>() {
        {
            this.put(Byte.class, OptionType.STRING);
            this.put(Short.class, OptionType.STRING);
            this.put(Integer.class, OptionType.INTEGER);
            this.put(Long.class, OptionType.NUMBER);
            this.put(Double.class, OptionType.NUMBER);
            this.put(Float.class, OptionType.NUMBER);
            this.put(Boolean.class, OptionType.BOOLEAN);
            this.put(Character.class, OptionType.STRING);
            this.put(String.class, OptionType.STRING);
            this.put(String[].class, OptionType.STRING);
            this.put(User.class, OptionType.USER);
            this.put(Member.class, OptionType.USER);
            this.put(GuildChannel.class, OptionType.CHANNEL);
            this.put(GuildMessageChannel.class, OptionType.CHANNEL);
            this.put(ThreadChannel.class, OptionType.CHANNEL);
            this.put(TextChannel.class, OptionType.CHANNEL);
            this.put(NewsChannel.class, OptionType.CHANNEL);
            this.put(AudioChannel.class, OptionType.CHANNEL);
            this.put(VoiceChannel.class, OptionType.CHANNEL);
            this.put(StageChannel.class, OptionType.CHANNEL);
            this.put(Role.class, OptionType.ROLE);
        }
    };

    private static final Map<Class<?>, List<ChannelType>> CHANNEL_TYPE_RESTRICTIONS = new HashMap<Class<?>, List<ChannelType>>() {
        {
            this.put(GuildMessageChannel.class, Collections.singletonList(ChannelType.TEXT));
            this.put(ThreadChannel.class, Arrays.asList(
                    ChannelType.GUILD_NEWS_THREAD,
                    ChannelType.GUILD_PUBLIC_THREAD,
                    ChannelType.GUILD_PRIVATE_THREAD
            ));
            this.put(TextChannel.class, Collections.singletonList(ChannelType.TEXT));
            this.put(NewsChannel.class, Collections.singletonList(ChannelType.NEWS));
            this.put(AudioChannel.class, Collections.singletonList(ChannelType.VOICE));
            this.put(VoiceChannel.class, Collections.singletonList(ChannelType.VOICE));
            this.put(StageChannel.class, Collections.singletonList(ChannelType.STAGE));
        }
    };

    private final Class<?> type;
    private final boolean isConcat;
    private final boolean isOptional;
    private final String defaultValue;
    private final boolean isPrimitive;
    private final String name;
    private final String description;
    private final List<Choice> choices;
    private final List<ConstraintDefinition> constraints;

    private ParameterDefinition(@NotNull final Class<?> type,
                                final boolean isConcat,
                                final boolean isOptional,
                                @Nullable final String defaultValue,
                                final boolean isPrimitive,
                                @NotNull final String name,
                                @NotNull final String description,
                                @NotNull final List<Choice> choices,
                                @NotNull final List<ConstraintDefinition> constraints) {
        this.type = type;
        this.isConcat = isConcat;
        this.isOptional = isOptional;
        this.defaultValue = defaultValue;
        this.isPrimitive = isPrimitive;
        this.name = name;
        this.description = description;
        this.choices = choices;
        this.constraints = constraints;
    }

    /**
     * Builds a new ParameterDefinition.
     *
     * @param parameter the {@link Parameter} of the command method
     * @param registry  an instance of the corresponding {@link ValidatorRegistry}
     *
     * @return a new ParameterDefinition
     */
    @NotNull
    public static ParameterDefinition build(@NotNull final Parameter parameter, @NotNull final ValidatorRegistry registry) {
        if (parameter.isVarArgs()) {
            throw new IllegalArgumentException("VarArgs is not supported for parameters!");
        }

        Class<?> parameterType = parameter.getType();
        parameterType = TYPE_MAPPINGS.getOrDefault(parameterType, parameterType);

        // Concat
        final boolean isConcat = parameter.isAnnotationPresent(Concat.class);
        if (isConcat && !String.class.isAssignableFrom(parameterType)) {
            throw new IllegalArgumentException("Concat can only be applied to Strings!");
        }

        // Optional
        final boolean isOptional = parameter.isAnnotationPresent(Optional.class);
        String defaultValue = "";
        if (isOptional) {
            defaultValue = parameter.getAnnotation(Optional.class).value();
        }
        if (defaultValue.isEmpty()) {
            defaultValue = null;
        }

        // index constraints
        final List<ConstraintDefinition> constraints = new ArrayList<>();
        for (final Annotation annotation : parameter.getAnnotations()) {
            final Class<?> annotationType = annotation.annotationType();
            if (!annotationType.isAnnotationPresent(Constraint.class)) {
                continue;
            }

            // annotation object is always different, so we cannot cast it. Thus, we need to get the custom error message via reflection
            String message = "";
            try {
                final Method method = annotationType.getDeclaredMethod("message");
                message = (String) method.invoke(annotation);
            } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }
            if (message.isEmpty()) {
                message = "Parameter validation failed";
            }

            final java.util.Optional<Validator> optional = registry.get(annotationType, parameterType);
            if (optional.isPresent()) {
                constraints.add(new ConstraintDefinition(optional.get(), message, annotation));
            }
        }

        // Param
        String name = parameter.getName().toLowerCase();
        String description = "empty description";
        if (parameter.isAnnotationPresent(Param.class)) {
            final Param param = parameter.getAnnotation(Param.class);
            name = param.name().isEmpty() ? name : param.name().toLowerCase();
            description = param.value();
        }

        final List<Choice> choices = new ArrayList<>();
        // Options
        if (parameter.isAnnotationPresent(Choices.class)) {
            final Choices opt = parameter.getAnnotation(Choices.class);
            for (final String option : opt.value()) {
                final String[] parsed = option.split(":", 2);
                if (parsed.length < 1) {
                    continue;
                }
                if (parsed.length < 2) {
                    choices.add(new Choice(parsed[0], parsed[0]));
                    continue;
                }
                choices.add(new Choice(parsed[0], parsed[1]));
            }
        }

        // this value is only used to determine if a default value must be present (primitives cannot be null)
        final boolean usesPrimitives = TYPE_MAPPINGS.containsKey(parameter.getType());

        return new ParameterDefinition(
                parameterType,
                isConcat,
                isOptional,
                defaultValue,
                usesPrimitives,
                name,
                description,
                choices,
                constraints
        );
    }

    /**
     * Transforms this parameter definition to a {@link OptionData}.
     *
     * @return the transformed {@link OptionData}
     */
    public OptionData toOptionData() {
        String name = this.getName();
        name = name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
        final OptionData optionData = new OptionData(
                OPTION_TYPE_MAPPINGS.getOrDefault(this.type, OptionType.STRING),
                name,
                this.description,
                !this.isOptional
        );

        optionData.addChoices(this.choices);

        this.constraints.stream().filter(constraint ->
                constraint.getAnnotation().getClass().isAssignableFrom(Min.class)
        ).findFirst().ifPresent(constraint -> optionData.setMinValue(((Min) constraint.getAnnotation()).value()));

        this.constraints.stream().filter(constraint ->
                constraint.getAnnotation().getClass().isAssignableFrom(Max.class)
        ).findFirst().ifPresent(constraint -> optionData.setMaxValue(((Max) constraint.getAnnotation()).value()));

        java.util.Optional.ofNullable(CHANNEL_TYPE_RESTRICTIONS.get(this.type)).ifPresent(optionData::setChannelTypes);

        return optionData;
    }

    /**
     * Gets the type of the parameter.
     *
     * @return the type of the parameter
     */
    @NotNull
    public Class<?> getType() {
        return this.type;
    }

    /**
     * Whether the parameter should be concatenated.
     *
     * @return {@code true} if the parameter should be concatenated
     */
    public boolean isConcat() {
        return this.isConcat;
    }

    /**
     * Whether the parameter is optional.
     *
     * @return {@code true} if the parameter is optional
     */
    public boolean isOptional() {
        return this.isOptional;
    }

    /**
     * Gets a possibly-null default value to use if the parameter is optional.
     *
     * @return a possibly-null default value
     */
    @Nullable
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Whether the type of the parameter is a primitive.
     *
     * @return {@code true} if the type of the parameter is a primitive
     */
    public boolean isPrimitive() {
        return this.isPrimitive;
    }

    /**
     * Gets a possibly-empty list of {@link ConstraintDefinition ConstraintDefinitions}.
     *
     * @return a possibly-empty list of {@link ConstraintDefinition ConstraintDefinitions}
     */
    @NotNull
    public List<ConstraintDefinition> getConstraints() {
        return this.constraints;
    }

    /**
     * Gets the parameter name.
     *
     * @return the parameter name
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Gets the parameter description. Only used for slash commands.
     *
     * @return the parameter description
     */
    @NotNull
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the parameter choices. Only used for slash commands.
     *
     * @return the parameter choices
     */
    public List<Choice> getChoices() {
        return this.choices;
    }

    @Override
    public String toString() {
        return "{" +
                this.type.getName() +
                ", isConcat=" + this.isConcat +
                ", isOptional=" + this.isOptional +
                ", defaultValue='" + this.defaultValue + '\'' +
                ", isPrimitive=" + this.isPrimitive +
                ", name='" + this.name + '\'' +
                ", constraints=" + this.constraints +
                '}';
    }
}
