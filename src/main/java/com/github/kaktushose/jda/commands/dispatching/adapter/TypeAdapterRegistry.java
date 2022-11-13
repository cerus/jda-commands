package com.github.kaktushose.jda.commands.dispatching.adapter;

import com.github.kaktushose.jda.commands.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.dispatching.CommandEvent;
import com.github.kaktushose.jda.commands.dispatching.adapter.impl.*;
import com.github.kaktushose.jda.commands.embeds.error.ErrorMessageFactory;
import com.github.kaktushose.jda.commands.reflect.CommandDefinition;
import com.github.kaktushose.jda.commands.reflect.ParameterDefinition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central registry for all type adapters.
 *
 * @author Kaktushose
 * @version 2.0.0
 * @see TypeAdapter
 * @since 2.0.0
 */
public class TypeAdapterRegistry {

    private static final Logger log = LoggerFactory.getLogger(TypeAdapterRegistry.class);
    private final Map<Class<?>, TypeAdapter<?>> parameterAdapters;

    /**
     * Constructs a new TypeAdapterRegistry. This will register default type adapters for:
     * <ul>
     *     <li>all primitive data types</li>
     *     <li>String</li>
     *     <li>String Array</li>
     *     <li>{@link Member}</li>
     *     <li>{@link User}</li>
     *     <li>{@link TextChannel}</li>
     *     <li>{@link Role}</li>
     * </ul>
     */
    public TypeAdapterRegistry() {
        this.parameterAdapters = new HashMap<>();

        // default types
        this.register(Byte.class, new ByteAdapter());
        this.register(Short.class, new ShortAdapter());
        this.register(Integer.class, new IntegerAdapter());
        this.register(Long.class, new LongAdapter());
        this.register(Float.class, new FloatAdapter());
        this.register(Double.class, new DoubleAdapter());
        this.register(Character.class, new CharacterAdapter());
        this.register(Boolean.class, new BooleanAdapter());
        this.register(String.class, (TypeAdapter<String>) (raw, guild) -> Optional.of(raw));
        this.register(String[].class, (TypeAdapter<String>) (raw, guild) -> Optional.of(raw));

        // jda specific
        this.register(Member.class, new MemberAdapter());
        this.register(User.class, new UserAdapter());
        this.register(GuildChannel.class, new GuildChannelAdapter());
        this.register(GuildMessageChannel.class, new GuildMessageChannelAdapter());
        this.register(ThreadChannel.class, new ThreadChannelAdapter());
        this.register(TextChannel.class, new TextChannelAdapter());
        this.register(NewsChannel.class, new NewsChannelAdapter());
        this.register(AudioChannel.class, new AudioChannelAdapter());
        this.register(VoiceChannel.class, new VoiceChannelAdapter());
        this.register(StageChannel.class, new StageChannelAdapter());
        this.register(Role.class, new RoleAdapter());
    }

    /**
     * Registers a new type adapter.
     *
     * @param type    the type the adapter is for
     * @param adapter the {@link TypeAdapter}
     */
    public void register(@NotNull final Class<?> type, @NotNull final TypeAdapter<?> adapter) {
        this.parameterAdapters.put(type, adapter);
        log.debug("Registered adapter {} for type {}", adapter.getClass().getName(), type.getName());
    }

    /**
     * Unregisters a new type adapter.
     *
     * @param type the type the adapter is for
     */
    public void unregister(@NotNull final Class<?> type) {
        this.parameterAdapters.remove(type);
        log.debug("Unregistered adapter for type {}", type.getName());
    }

    /**
     * Checks if a type adapter for the given type exists.
     *
     * @param type the type to check
     *
     * @return {@code true} if a type adapter exists
     */
    public boolean exists(@Nullable final Class<?> type) {
        return this.parameterAdapters.containsKey(type);
    }

    /**
     * Retrieves a type adapter.
     *
     * @param type the type to get the adapter for
     *
     * @return the type adapter or an empty Optional if none found
     */
    public Optional<TypeAdapter<?>> get(@Nullable final Class<?> type) {
        return Optional.ofNullable(this.parameterAdapters.get(type));
    }

    /**
     * Takes a {@link CommandContext} and attempts to type adapt the command input to the type specified by the
     * {@link CommandDefinition}. Cancels the {@link CommandContext} if the type adapting fails.
     *
     * @param context the {@link CommandContext} to type adapt
     */
    public void adapt(@NotNull final CommandContext context) {
        final CommandDefinition command = Objects.requireNonNull(context.getCommand());
        final List<Object> arguments = new ArrayList<>();
        final String[] input = context.getInput();
        final ErrorMessageFactory messageFactory = context.getImplementationRegistry().getErrorMessageFactory();

        log.debug("Type adapting arguments...");
        arguments.add(new CommandEvent(command, context));
        // start with index 1 so we skip the CommandEvent
        for (int i = 0; i < command.getActualParameters().size(); i++) {
            final ParameterDefinition parameter = command.getActualParameters().get(i);

            // if parameter is array don't parse
            if (String[].class.isAssignableFrom(parameter.getType())) {
                log.debug("First parameter is String array. Not adapting arguments");
                arguments.add(input);
                break;
            }

            final String raw;
            // current parameter index == total amount of input, check if it's optional else cancel context
            if (i == input.length) {
                if (!parameter.isOptional()) {
                    log.debug("Syntax error! Cancelled event.");
                    context.setCancelled(true);
                    context.setErrorMessage(messageFactory.getSyntaxErrorMessage(context));
                    break;
                }

                // if the default value is an empty String (thus not present) add a null value to the argument list
                // else try to type adapt the default value
                if (parameter.getDefaultValue() == null) {
                    arguments.add(null);
                    continue;
                } else {
                    raw = parameter.getDefaultValue();
                }
            } else {
                raw = input[i];
            }

            if (i == command.getActualParameters().size() - 1 && parameter.isConcat()) {
                final StringBuilder sb = new StringBuilder();
                for (final String s : Arrays.copyOfRange(input, i, input.length)) {
                    sb.append(s).append(" ");
                }
                arguments.add(sb.toString().trim());
                break;
            }

            log.debug("Trying to adapt input \"{}\" to type {}", raw, parameter.getType().getName());

            final Optional<TypeAdapter<?>> adapter = this.get(parameter.getType());
            if (!adapter.isPresent()) {
                throw new IllegalArgumentException("No type adapter found!");
            }

            final Optional<?> parsed = adapter.get().parse(raw, context);
            if (!parsed.isPresent()) {
                log.debug("Type adapting failed!");
                context.setCancelled(true);
                context.setErrorMessage(messageFactory.getSyntaxErrorMessage(context));
                break;
            }

            arguments.add(parsed.get());
            log.debug("Added {} to the argument list", parsed.get());
        }
        context.setArguments(arguments);
    }
}
