package com.github.kaktushose.jda.commands.embeds.error;

import com.github.kaktushose.jda.commands.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.interactions.commands.CommandRegistrationPolicy;
import com.github.kaktushose.jda.commands.reflect.ConstraintDefinition;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Generic interface for factory classes that generate error messages.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see DefaultErrorMessageFactory
 * @since 2.0.0
 */
public interface ErrorMessageFactory {

    /**
     * Gets a {@link Message} to send when no command was found.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when no command was found
     */
    MessageCreateBuilder getCommandNotFoundMessage(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send when a user is missing permissions.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when a user is missing permissions
     */
    MessageCreateBuilder getInsufficientPermissionsMessage(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send when a {@link net.dv8tion.jda.api.entities.Guild Guild} is muted.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when a {@link net.dv8tion.jda.api.entities.Guild Guild} is muted
     */
    MessageCreateBuilder getGuildMutedMessage(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send when a {@link TextChannel TextChannel} is muted.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when a {@link TextChannel TextChannel} is muted
     */
    MessageCreateBuilder getChannelMutedMessage(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send when a {@link net.dv8tion.jda.api.entities.User User} is muted.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when a {@link net.dv8tion.jda.api.entities.User User} is muted
     */
    MessageCreateBuilder getUserMutedMessage(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send when the user input has a syntax error.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when the user input has a syntax error
     */
    MessageCreateBuilder getSyntaxErrorMessage(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send when a parameter constraint fails.
     *
     * @param context    the corresponding {@link CommandContext}
     * @param constraint the corresponding {@link ConstraintDefinition} that failed
     *
     * @return a {@link Message} to send when a parameter constraint fails
     */
    MessageCreateBuilder getConstraintFailedMessage(@NotNull CommandContext context, @NotNull ConstraintDefinition constraint);

    /**
     * Gets a {@link Message} to send when a command still has a cooldown.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when a command still has a cooldown
     */
    MessageCreateBuilder getCooldownMessage(@NotNull CommandContext context, long ms);

    /**
     * Gets a {@link Message} to send when the channel type isn't suitable for the command.
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when the channel type isn't suitable for the command
     */
    MessageCreateBuilder getWrongChannelTypeMessage(@NotNull CommandContext context);

    /**
     * Gets a {@link Message} to send when the command execution failed.
     *
     * @param context   the corresponding {@link CommandContext}
     * @param exception the Exception that made the command execution fail
     *
     * @return a {@link Message} to send when the command execution failed
     */
    MessageCreateBuilder getCommandExecutionFailedMessage(@NotNull CommandContext context, @NotNull Exception exception);

    /**
     * Gets a {@link Message} to send when a text command gets invoked but the
     * {@link CommandRegistrationPolicy CommandRegistrationPolicy} is set to
     * {@link CommandRegistrationPolicy#MIGRATING
     * CommandRegistrationPolicy.MIGRATING}
     *
     * @param context the corresponding {@link CommandContext}
     *
     * @return a {@link Message} to send when a text command gets invoked
     */
    MessageCreateBuilder getSlashCommandMigrationMessage(@NotNull CommandContext context);
}
