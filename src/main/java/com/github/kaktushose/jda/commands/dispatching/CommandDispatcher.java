package com.github.kaktushose.jda.commands.dispatching;

import com.github.kaktushose.jda.commands.JDACommands;
import com.github.kaktushose.jda.commands.JDAContext;
import com.github.kaktushose.jda.commands.dependency.DependencyInjector;
import com.github.kaktushose.jda.commands.dispatching.adapter.TypeAdapterRegistry;
import com.github.kaktushose.jda.commands.dispatching.filter.Filter;
import com.github.kaktushose.jda.commands.dispatching.filter.FilterRegistry;
import com.github.kaktushose.jda.commands.dispatching.filter.FilterRegistry.FilterPosition;
import com.github.kaktushose.jda.commands.dispatching.interactions.ButtonInteractionDispatcher;
import com.github.kaktushose.jda.commands.dispatching.parser.ParserSupervisor;
import com.github.kaktushose.jda.commands.dispatching.router.Router;
import com.github.kaktushose.jda.commands.dispatching.sender.MessageSender;
import com.github.kaktushose.jda.commands.dispatching.validation.ValidatorRegistry;
import com.github.kaktushose.jda.commands.embeds.help.HelpMessageFactory;
import com.github.kaktushose.jda.commands.interactions.commands.SlashCommandUpdater;
import com.github.kaktushose.jda.commands.interactions.commands.SlashConfiguration;
import com.github.kaktushose.jda.commands.reflect.CommandDefinition;
import com.github.kaktushose.jda.commands.reflect.CommandRegistry;
import com.github.kaktushose.jda.commands.reflect.ImplementationRegistry;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dispatches commands by taking a {@link CommandContext} and passing it through the execution chain.
 *
 * @author Kaktushose
 * @version 2.0.0
 * @since 2.0.0
 */
public class CommandDispatcher {

    private static final Logger log = LoggerFactory.getLogger(CommandDispatcher.class);
    private static boolean isActive;
    private final JDAContext jdaContext;
    private final ImplementationRegistry implementationRegistry;
    private final ParserSupervisor parserSupervisor;
    private final FilterRegistry filterRegistry;
    private final TypeAdapterRegistry adapterRegistry;
    private final ValidatorRegistry validatorRegistry;
    private final CommandRegistry commandRegistry;
    private final DependencyInjector dependencyInjector;
    private final JDACommands jdaCommands;
    private final SlashConfiguration configuration;
    private final SlashCommandUpdater updater;
    private final ButtonInteractionDispatcher buttonListener;

    /**
     * Constructs a new CommandDispatcher.
     *
     * @param jdaContext    the corresponding {@link JDAContext} instance
     * @param jdaCommands   the corresponding {@link JDACommands} instance
     * @param packages      optional packages to exclusively scan
     * @param clazz         a class of the classpath to scan
     * @param configuration the corresponding {@link SlashConfiguration}
     *
     * @throws IllegalStateException if an instance of this class is already active.
     */
    public CommandDispatcher(@NotNull final JDAContext jdaContext,
                             @NotNull final JDACommands jdaCommands,
                             @NotNull final Class<?> clazz,
                             @NotNull final SlashConfiguration configuration,
                             @NotNull final String... packages) {
        this.jdaContext = jdaContext;
        this.jdaCommands = jdaCommands;
        this.configuration = configuration;

        if (isActive) {
            throw new IllegalStateException("An instance of the command framework is already running!");
        }

        this.dependencyInjector = new DependencyInjector();
        this.dependencyInjector.index(clazz, packages);

        this.filterRegistry = new FilterRegistry();
        this.adapterRegistry = new TypeAdapterRegistry();
        this.validatorRegistry = new ValidatorRegistry();

        this.implementationRegistry = new ImplementationRegistry(this.dependencyInjector, this.filterRegistry, this.adapterRegistry, this.validatorRegistry);
        this.implementationRegistry.index(clazz, packages);

        this.parserSupervisor = new ParserSupervisor(this);
        this.buttonListener = new ButtonInteractionDispatcher(jdaCommands);
        jdaContext.performTask(jda -> jda.addEventListener(this.parserSupervisor, this.buttonListener));

        this.commandRegistry = new CommandRegistry(this.adapterRegistry, this.validatorRegistry, this.dependencyInjector, this.buttonListener);
        this.commandRegistry.index(clazz, packages);

        this.dependencyInjector.inject();

        this.updater = new SlashCommandUpdater(jdaContext, configuration);
        this.updater.update(this.commandRegistry.getCommands());
        isActive = true;
    }

    /**
     * Whether this CommandDispatcher is active.
     *
     * @return {@code true} if the CommandDispatcher is active
     */
    public static boolean isActive() {
        return isActive;
    }

    /**
     * Shuts down this CommandDispatcher instance, making it unable to receive any events from Discord.
     * This will <b>not</b> unregister any slash commands.
     */
    public void shutdown() {
        this.jdaContext.performTask(jda -> jda.removeEventListener(this.parserSupervisor, this.buttonListener));
        this.updater.shutdown();
        isActive = false;
    }

    /**
     * Dispatches a {@link CommandContext}. This will route the command, apply all filters and parse the arguments.
     * Finally, the command will be executed.
     *
     * @param context the {@link CommandContext} to dispatch.
     */
    public void onEvent(@NotNull final CommandContext context) {
        log.debug("Applying filters in phase BEFORE_ROUTING...");
        for (final Filter filter : this.filterRegistry.getAll(FilterPosition.BEFORE_ROUTING)) {
            filter.apply(context);
            if (this.checkCancelled(context)) {
                return;
            }
        }

        final HelpMessageFactory helpMessageFactory = this.implementationRegistry.getHelpMessageFactory();
        final Router router = this.implementationRegistry.getRouter();
        final MessageSender sender = this.implementationRegistry.getMessageSender();

        router.findCommands(context, this.commandRegistry.getCommands());

        if (context.isCancelled() && context.isHelpEvent()) {
            log.debug("Sending generic help");
            sender.sendGenericHelpMessage(context, helpMessageFactory.getGenericHelp(this.commandRegistry.getControllers(), context).build());
            return;
        }

        if (this.checkCancelled(context)) {
            log.debug("No matching command found!");
            return;
        }

        final CommandDefinition command = Objects.requireNonNull(context.getCommand());
        log.debug("Input matches command: {}", command);

        if (context.isHelpEvent()) {
            log.debug("Sending specific help");
            sender.sendSpecificHelpMessage(context, helpMessageFactory.getSpecificHelp(context).build());
            return;
        }

        if (context.isSlash()) {
            final List<String> parameters = new ArrayList<>();
            final Map<String, OptionMapping> options = context.getOptionsAsMap();
            command.getActualParameters().forEach(param -> {
                if (!options.containsKey(param.getName())) {
                    return;
                }
                parameters.add(options.get(param.getName()).getAsString());
            });
            if (!parameters.isEmpty()) {
                context.setInput(parameters.toArray(new String[] {}));
            }
        }

        log.debug("Applying filters in phase BEFORE_ADAPTING...");
        for (final Filter filter : this.filterRegistry.getAll(FilterPosition.BEFORE_ADAPTING)) {
            filter.apply(context);
            if (this.checkCancelled(context)) {
                return;
            }
        }

        this.adapterRegistry.adapt(context);
        if (this.checkCancelled(context)) {
            return;
        }

        log.debug("Applying filters in phase BEFORE_EXECUTION...");
        for (final Filter filter : this.filterRegistry.getAll(FilterPosition.BEFORE_EXECUTION)) {
            filter.apply(context);
            if (this.checkCancelled(context)) {
                return;
            }
        }

        if (this.checkCancelled(context)) {
            return;
        }

        log.info("Executing command {} for user {}", command.getMethod().getName(), context.getEvent().getAuthor());
        try {
            if (context.isSlash() && command.getController().isAutoAcknowledge()) {
                context.getInteractionEvent().deferReply(context.getCommand().isEphemeral()).queue();
            }
            log.debug("Invoking method with following arguments: {}", context.getArguments());
            command.getMethod().invoke(command.getInstance(), context.getArguments().toArray());
        } catch (final Exception e) {
            log.error("Command execution failed!", new InvocationTargetException(e));
        }
    }

    private boolean checkCancelled(final CommandContext context) {
        if (context.isCancelled()) {
            this.implementationRegistry.getMessageSender().sendErrorMessage(context, Objects.requireNonNull(context.getErrorMessage()).build());
            return true;
        }
        return false;
    }

    /**
     * Gets the {@link ImplementationRegistry}.
     *
     * @return the {@link ImplementationRegistry}
     */
    public ImplementationRegistry getImplementationRegistry() {
        return this.implementationRegistry;
    }

    /**
     * Gets the {@link ParserSupervisor}.
     *
     * @return the {@link ParserSupervisor}
     */
    public ParserSupervisor getParserSupervisor() {
        return this.parserSupervisor;
    }

    /**
     * Gets the {@link TypeAdapterRegistry}.
     *
     * @return the {@link TypeAdapterRegistry}
     */
    public TypeAdapterRegistry getAdapterRegistry() {
        return this.adapterRegistry;
    }

    /**
     * Gets the {@link ValidatorRegistry}.
     *
     * @return the {@link ValidatorRegistry}
     */
    public ValidatorRegistry getValidatorRegistry() {
        return this.validatorRegistry;
    }

    /**
     * Gets the {@link CommandRegistry}.
     *
     * @return the {@link CommandRegistry}
     */
    public CommandRegistry getCommandRegistry() {
        return this.commandRegistry;
    }

    /**
     * Gets the JDA instance. This can either be {@link JDA} or a {@link ShardManager}. Use {@link #isShardManager()}
     * to distinguish.
     *
     * @return the JDA instance.
     *
     * @deprecated use {@link #getJdaContext()}
     */
    public Object getJda() {
        return this.jdaContext.getJDAObject();
    }

    /**
     * Whether the JDA instance is a {@link ShardManager}.
     *
     * @return {@code true} if the JDA instance is a {@link ShardManager}
     *
     * @deprecated use {@link #getJdaContext()}
     */
    public boolean isShardManager() {
        return this.jdaContext.isShardManager();
    }

    /**
     * Gets the {@link JDAContext}.
     *
     * @return the JDAContext.
     */
    public JDAContext getJdaContext() {
        return this.jdaContext;
    }

    /**
     * Gets the {@link FilterRegistry}.
     *
     * @return the {@link FilterRegistry}
     */
    public FilterRegistry getFilterRegistry() {
        return this.filterRegistry;
    }

    /**
     * Gets the {@link HelpMessageFactory}.
     *
     * @return the {@link HelpMessageFactory}
     */
    public HelpMessageFactory getHelpMessageFactory() {
        return this.implementationRegistry.getHelpMessageFactory();
    }

    /**
     * Gets the {@link JDACommands} instance.
     *
     * @return the {@link JDACommands} instance
     */
    public JDACommands getJdaCommands() {
        return this.jdaCommands;
    }

    /**
     * Gets the {@link DependencyInjector}.
     *
     * @return the {@link DependencyInjector}
     */
    public DependencyInjector getDependencyInjector() {
        return this.dependencyInjector;
    }

    /**
     * Gets the {@link SlashConfiguration}.
     *
     * @return the {@link SlashConfiguration}
     */
    public SlashConfiguration getSlashConfiguration() {
        return this.configuration;
    }
}
