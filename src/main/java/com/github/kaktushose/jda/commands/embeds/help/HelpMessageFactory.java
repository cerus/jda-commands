package com.github.kaktushose.jda.commands.embeds.help;

import com.github.kaktushose.jda.commands.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.reflect.ControllerDefinition;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Generic interface for factory classes that generate help messages.
 *
 * @author Kaktushose
 * @version 2.0.0
 * @see DefaultHelpMessageFactory
 * @since 2.0.0
 */
public interface HelpMessageFactory {

    /**
     * Gets a {@link Message} to send to get information about a specific command.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send to get information about a specific command
     */
    MessageCreateBuilder getSpecificHelp(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send to get an overview over all available commands.
     *
     * @param controllers a {@link Set} of all available {@link ControllerDefinition ControllerDefinitions}
     * @param context     context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send to get an overview over all available commands
     */
    MessageCreateBuilder getGenericHelp(@NotNull Set<ControllerDefinition> controllers, @NotNull CommandContext context);
}
