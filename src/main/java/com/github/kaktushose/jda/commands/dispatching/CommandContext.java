package com.github.kaktushose.jda.commands.dispatching;

import com.github.kaktushose.jda.commands.JDACommands;
import com.github.kaktushose.jda.commands.reflect.CommandDefinition;
import com.github.kaktushose.jda.commands.reflect.ImplementationRegistry;
import com.github.kaktushose.jda.commands.settings.GuildSettings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class models a command execution. The
 * {@link com.github.kaktushose.jda.commands.dispatching.parser.Parser Parser} constructs a new CommandContext for each
 * valid event received. The CommandContext is then passed through the execution chain until it is then transformed into
 * a {@link CommandEvent}.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see GenericEvent
 * @since 2.0.0
 */
public class CommandContext {

    private final SlashCommandInteractionEvent interactionEvent;
    private String[] input;
    private List<OptionMapping> options;
    private GenericEvent event;
    private CommandDefinition command;
    private List<CommandDefinition> possibleCommands;
    private List<Object> arguments;
    private MessageCreateBuilder errorMessage;
    private GuildSettings settings;
    private ImplementationRegistry registry;
    private JDACommands jdaCommands;
    private boolean isHelpEvent;
    private boolean cancelled;

    private CommandContext(final GenericEvent event, final JDACommands jdaCommands,
                           final GuildSettings settings,
                           final ImplementationRegistry registry,
                           final SlashCommandInteractionEvent interactionEvent) {
        this.input = new String[0];
        this.options = new ArrayList<>();
        this.possibleCommands = new ArrayList<>();
        this.arguments = new ArrayList<>();
        this.event = event;
        this.jdaCommands = jdaCommands;
        this.settings = settings;
        this.registry = registry;
        this.interactionEvent = interactionEvent;
    }

    /**
     * Constructs a new CommandContext.
     *
     * @param event       The underlying {@link SlashCommandInteractionEvent}
     * @param jdaCommands the corresponding {@link JDACommands} instance
     * @param settings    the corresponding {@link GuildSettings}
     * @param registry    the corresponding {@link ImplementationRegistry}
     */
    public CommandContext(@NotNull final SlashCommandInteractionEvent event,
                          @NotNull final JDACommands jdaCommands,
                          @NotNull final GuildSettings settings,
                          @NotNull final ImplementationRegistry registry) {
        this(GenericEvent.fromEvent(event), jdaCommands, settings, registry, event);
    }

    /**
     * Constructs a new CommandContext.
     *
     * @param event       The underlying {@link MessageReceivedEvent}
     * @param jdaCommands the corresponding {@link JDACommands} instance
     * @param settings    the corresponding {@link GuildSettings}
     * @param registry    the corresponding {@link ImplementationRegistry}
     */
    public CommandContext(@NotNull final MessageReceivedEvent event,
                          @NotNull final JDACommands jdaCommands,
                          @NotNull final GuildSettings settings,
                          @NotNull final ImplementationRegistry registry) {
        this(GenericEvent.fromEvent(event), jdaCommands, settings, registry, null);
    }

    /**
     * Gets the raw user input. Will be empty in phase
     * {@link com.github.kaktushose.jda.commands.dispatching.filter.FilterRegistry.FilterPosition#BEFORE_ROUTING
     * FilterPosition.BEFORE_ROUTING} if {@link #isSlash} returns {@code true}.
     *
     * @return the raw user input
     *
     * @see #getOptions()
     */
    @NotNull
    public String[] getInput() {
        return this.input;
    }

    /**
     * Set the user input.
     *
     * @param input the user input
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setInput(@NotNull final String[] input) {
        this.input = input;
        return this;
    }

    /**
     * Gets the {@link OptionMapping OptionMappings}. Will be empty if {@link #isSlash} returns {@code false}.
     *
     * @return the {@link OptionMapping OptionMappings}
     *
     * @see #getInput()
     */
    @NotNull
    public List<OptionMapping> getOptions() {
        return this.options;
    }

    /**
     * Set the {@link OptionMapping OptionMappings}.
     *
     * @param options the {@link OptionMapping OptionMappings}
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setOptions(@NotNull final List<OptionMapping> options) {
        this.options = options;
        return this;
    }

    /**
     * Gets the {@link OptionMapping OptionMappings} inserted in a {@code Map<Name, Mapping>}.
     * Will be empty if {@link #isSlash} returns {@code false}.
     *
     * @return the {@link OptionMapping OptionMappings}
     *
     * @see #getInput()
     */
    @NotNull
    public Map<String, OptionMapping> getOptionsAsMap() {
        final Map<String, OptionMapping> result = new HashMap<>();
        this.options.forEach(option -> result.put(option.getName(), option));
        return result;
    }

    /**
     * Gets the corresponding {@link MessageReceivedEvent}.
     *
     * @return the corresponding {@link MessageReceivedEvent}
     */
    @NotNull
    public GenericEvent getEvent() {
        return this.event;
    }

    /**
     * Set the {@link MessageReceivedEvent}.
     *
     * @param event the {@link MessageReceivedEvent}
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setEvent(@NotNull final MessageReceivedEvent event) {
        this.event = GenericEvent.fromEvent(event);
        return this;
    }

    /**
     * Set the {@link SlashCommandInteractionEvent}.
     *
     * @param event the {@link SlashCommandInteractionEvent}
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setEvent(@NotNull final SlashCommandInteractionEvent event) {
        this.event = GenericEvent.fromEvent(event);
        return this;
    }

    /**
     * Gets the {@link CommandDefinition}.
     *
     * @return the {@link CommandDefinition}
     */
    @Nullable
    public CommandDefinition getCommand() {
        return this.command;
    }

    /**
     * Set the {@link CommandDefinition}.
     *
     * @param command the {@link CommandDefinition}
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setCommand(@Nullable final CommandDefinition command) {
        this.command = command;
        return this;
    }

    /**
     * Gets a list of {@link CommandDefinition CommandDefinitions} that match the input. This list gets populated if
     * and only if the command routing fails because more than one matching command was found. If no matching command
     * was found or if the command routing succeeded, this list will be empty.
     *
     * @return a possibly-empty list of {@link CommandDefinition CommandDefinitions} that match the input
     */
    @NotNull
    public List<CommandDefinition> getPossibleCommands() {
        return this.possibleCommands == null ? new ArrayList<>() : this.possibleCommands;
    }

    /**
     * Sets the list of {@link CommandDefinition CommandDefinitions} that match the input.
     *
     * @param possibleCommands a list of {@link CommandDefinition CommandDefinitions} that match the input
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setPossibleCommands(@NotNull final List<CommandDefinition> possibleCommands) {
        this.possibleCommands = possibleCommands;
        return this;
    }

    /**
     * Gets the parsed arguments.
     *
     * @return the parsed arguments
     */
    @NotNull
    public List<Object> getArguments() {
        return this.arguments;
    }

    /**
     * Set the arguments.
     *
     * @param arguments the parsed arguments
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setArguments(@NotNull final List<Object> arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * Gets the {@link Message} to send if an error occurred.
     *
     * @return {@link Message} to send
     */
    @Nullable
    public MessageCreateBuilder getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Set the the {@link Message} to send if an error occurred.
     *
     * @param message the {@link Message} to send
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setErrorMessage(@NotNull final MessageCreateBuilder message) {
        this.errorMessage = message;
        return this;
    }

    /**
     * Gets the corresponding {@link GuildSettings}.
     *
     * @return the corresponding {@link GuildSettings}
     */
    @NotNull
    public GuildSettings getSettings() {
        return this.settings;
    }

    /**
     * Set the {@link GuildSettings}.
     *
     * @param settings the {@link GuildSettings}
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setSettings(@NotNull final GuildSettings settings) {
        this.settings = settings;
        return this;
    }

    /**
     * Gets the corresponding {@link ImplementationRegistry} instance.
     *
     * @return the corresponding {@link ImplementationRegistry} instance
     */
    @NotNull
    public ImplementationRegistry getImplementationRegistry() {
        return this.registry;
    }

    /**
     * Set the {@link ImplementationRegistry} instance.
     *
     * @param registry the {@link ImplementationRegistry} instance
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setImplementationRegistry(@NotNull final ImplementationRegistry registry) {
        this.registry = registry;
        return this;
    }

    /**
     * Gets the corresponding {@link JDACommands} instance.
     *
     * @return the corresponding {@link JDACommands} instance
     */
    @NotNull
    public JDACommands getJdaCommands() {
        return this.jdaCommands;
    }

    /**
     * Set the {@link JDACommands} instance.
     *
     * @param jdaCommands the {@link JDACommands} instance
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setJdaCommands(@NotNull final JDACommands jdaCommands) {
        this.jdaCommands = jdaCommands;
        return this;
    }

    /**
     * Whether the context represents a help event.
     *
     * @return {@code true} if the context represents a help event
     */
    public boolean isHelpEvent() {
        return this.isHelpEvent;
    }

    /**
     * Set whether the context represents a help event.
     *
     * @param helpEvent whether the context represents a help event
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setHelpEvent(final boolean helpEvent) {
        this.isHelpEvent = helpEvent;
        return this;
    }

    /**
     * Whether the context should be cancelled.
     *
     * @return {@code true} if the context should be cancelled
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set whether the context should be cancelled.
     *
     * @param cancelled whether the context should be cancelled
     *
     * @return the current CommandContext instance
     */
    @NotNull
    public CommandContext setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    /**
     * Whether the context was created from a slash command.
     *
     * @return {@code true} if the context was created from a slash command
     */
    public boolean isSlash() {
        return this.interactionEvent != null;
    }

    /**
     * Gets the possibly-null {@link SlashCommandInteractionEvent}.
     *
     * @return possibly-null {@link SlashCommandInteractionEvent}
     *
     * @see #isSlash()
     */
    @Nullable
    public SlashCommandInteractionEvent getInteractionEvent() {
        return this.interactionEvent;
    }

    /**
     * Gets the right prefix for the current context. If {@link #isSlash()} is {@code true} this returns <em>/</em> else
     * {@link GuildSettings#getPrefix()}
     *
     * @return the contextual prefix
     */
    public String getContextualPrefix() {
        return this.isSlash() ? "/" : this.settings.getPrefix();
    }

}
