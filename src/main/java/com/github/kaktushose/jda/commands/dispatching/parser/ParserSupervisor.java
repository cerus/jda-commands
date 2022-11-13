package com.github.kaktushose.jda.commands.dispatching.parser;

import com.github.kaktushose.jda.commands.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.dispatching.CommandDispatcher;
import com.github.kaktushose.jda.commands.dispatching.parser.impl.DefaultMessageParser;
import com.github.kaktushose.jda.commands.dispatching.parser.impl.DefaultSlashCommandParser;
import com.github.kaktushose.jda.commands.dispatching.parser.impl.MigratingMessageParser;
import com.github.kaktushose.jda.commands.dispatching.sender.MessageSender;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry for {@link Parser Parsers}. This is also the event listener that will call the corresponding parser.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see Parser
 * @since 2.0.0
 */
public class ParserSupervisor extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ParserSupervisor.class);
    private final CommandDispatcher dispatcher;
    private final Map<Class<? extends GenericEvent>, Parser<? extends GenericEvent>> listeners;

    /**
     * Constructs a new ParserSupervisor.
     *
     * @param dispatcher the calling {@link CommandDispatcher}
     */
    public ParserSupervisor(@NotNull final CommandDispatcher dispatcher) {
        this.listeners = new HashMap<>();
        this.dispatcher = dispatcher;
        switch (dispatcher.getSlashConfiguration().getPolicy()) {
            case TEXT:
                this.register(MessageReceivedEvent.class, new DefaultMessageParser());
                break;
            case SLASH:
                this.register(SlashCommandInteractionEvent.class, new DefaultSlashCommandParser());
                break;
            case TEXT_AND_SLASH:
                this.register(MessageReceivedEvent.class, new DefaultMessageParser());
                this.register(SlashCommandInteractionEvent.class, new DefaultSlashCommandParser());
                break;
            case MIGRATING:
                this.register(MessageReceivedEvent.class, new MigratingMessageParser());
                this.register(SlashCommandInteractionEvent.class, new DefaultSlashCommandParser());
                break;
        }
    }

    /**
     * Registers a new {@link Parser} for the given subtype of {@link GenericEvent}.
     *
     * @param listener the subtype of {@link GenericEvent}
     * @param parser   the {@link Parser} to register
     */
    public void register(@NotNull final Class<? extends GenericEvent> listener, @NotNull final Parser<? extends GenericEvent> parser) {
        this.listeners.put(listener, parser);
        log.debug("Registered parser {} for event {}", parser.getClass().getName(), listener.getSimpleName());
    }

    /**
     * Unregisters the {@link Parser} for the given subtype of {@link GenericEvent}.
     *
     * @param listener the subtype of {@link GenericEvent}
     */
    public void unregister(@NotNull final Class<? extends GenericEvent> listener) {
        this.listeners.remove(listener);
        log.debug("Unregistered parser binding for event {}", listener.getSimpleName());
    }

    /**
     * Distributes {@link GenericEvent GenericEvents} to the corresponding parser. If the parsing didn't fail, will call
     * {@link CommandDispatcher#onEvent(CommandContext)}
     *
     * @param event the {@link GenericEvent GenericEvents} to distribute
     */
    @Override
    public void onGenericEvent(@NotNull final GenericEvent event) {
        if (!this.listeners.containsKey(event.getClass())) {
            return;
        }
        log.debug("Received {}", event.getClass().getSimpleName());
        final Parser<?> parser = this.listeners.get(event.getClass());
        log.debug("Calling {}", parser.getClass().getName());
        final CommandContext context = parser.parseInternal(event, this.dispatcher);

        final MessageSender sender = context.getImplementationRegistry().getMessageSender();

        if (context.isCancelled()) {
            if (context.getErrorMessage() != null) {
                sender.sendErrorMessage(context, context.getErrorMessage().build());
            }
            return;
        }

        try {
            this.dispatcher.onEvent(context);
        } catch (final Exception e) {
            sender.sendErrorMessage(context, context.getImplementationRegistry().getErrorMessageFactory().getCommandExecutionFailedMessage(context, e).build());
            log.error("Command execution failed!", e);
        }
    }
}
