package com.github.kaktushose.jda.commands.embeds.error;

import com.github.kaktushose.jda.commands.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.reflect.CommandDefinition;
import com.github.kaktushose.jda.commands.reflect.ConstraintDefinition;
import com.github.kaktushose.jda.commands.settings.GuildSettings;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link ErrorMessageFactory} with default embeds.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see JsonErrorMessageFactory
 * @since 2.0.0
 */
public class DefaultErrorMessageFactory implements ErrorMessageFactory {

    @Override
    public MessageCreateBuilder getCommandNotFoundMessage(@NotNull final CommandContext context) {
        final GuildSettings settings = context.getSettings();
        final EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Command Not Found")
                .setDescription(
                        String.format("```type %s%s to get a list of all available commands```",
                                settings.getPrefix(),
                                settings.getHelpLabels().stream().findFirst().orElse("help"))
                );
        if (!context.getPossibleCommands().isEmpty()) {
            final StringBuilder sbPossible = new StringBuilder();
            context.getPossibleCommands().forEach(command ->
                    sbPossible.append(String.format("`%s`", command.getLabels().get(0))).append(", ")
            );
            embed.addField("Similar Commands", sbPossible.substring(0, sbPossible.length() - 2), false);
        }
        return new MessageCreateBuilder().setEmbeds(embed.build());
    }

    @Override
    public MessageCreateBuilder getInsufficientPermissionsMessage(@NotNull final CommandContext context) {
        final GuildSettings settings = context.getSettings();
        final CommandDefinition command = context.getCommand();
        final StringBuilder sbPermissions = new StringBuilder();
        command.getPermissions().forEach(permission -> sbPermissions.append(permission).append(", "));
        final String permissions = sbPermissions.toString().isEmpty() ? "N/A" : sbPermissions.substring(0, sbPermissions.length() - 2);

        final MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Insufficient Permissions")
                .setDescription(String.format("`%s%s` requires specific permissions to be executed",
                        settings.getPrefix(),
                        command.getLabels().get(0)))
                .addField("Permissions:",
                        String.format("`%s`", permissions), false
                ).build();
        return new MessageCreateBuilder().setEmbeds(embed);
    }

    @Override
    public MessageCreateBuilder getGuildMutedMessage(@NotNull final CommandContext context) {
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Insufficient Permissions")
                .setDescription("This guild is muted!")
                .build()
        );
    }

    @Override
    public MessageCreateBuilder getChannelMutedMessage(@NotNull final CommandContext context) {
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Insufficient Permissions")
                .setDescription("This channel is muted!")
                .build()
        );
    }

    @Override
    public MessageCreateBuilder getUserMutedMessage(@NotNull final CommandContext context) {
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Insufficient Permissions")
                .setDescription("You are muted!")
                .build()
        );
    }

    @Override
    public MessageCreateBuilder getSyntaxErrorMessage(@NotNull final CommandContext context) {
        final String prefix = Matcher.quoteReplacement(context.getContextualPrefix());
        final StringBuilder sbExpected = new StringBuilder();
        final CommandDefinition command = Objects.requireNonNull(context.getCommand());
        final List<String> arguments = Arrays.asList(context.getInput());

        command.getActualParameters().forEach(parameter -> {

            String typeName = parameter.getType().getTypeName();
            if (typeName.contains(".")) {
                typeName = typeName.substring(typeName.lastIndexOf(".") + 1);
            }
            sbExpected.append(typeName).append(", ");
        });
        final String expected = sbExpected.toString().isEmpty() ? " " : sbExpected.substring(0, sbExpected.length() - 2);

        final StringBuilder sbActual = new StringBuilder();
        arguments.forEach(argument -> sbActual.append(argument).append(", "));
        final String actual = sbActual.toString().isEmpty() ? " " : sbActual.substring(0, sbActual.length() - 2);

        final MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Syntax Error")
                .setDescription(String.format("`%s`", command.getMetadata().getUsage().replaceAll(
                        "\\{prefix}", prefix))
                )
                .addField("Expected", String.format("`%s`", expected), false)
                .addField("Actual", String.format("`%s`", actual), false)
                .build();

        return new MessageCreateBuilder().setEmbeds(embed);
    }

    @Override
    public MessageCreateBuilder getConstraintFailedMessage(@NotNull final CommandContext context, @NotNull final ConstraintDefinition constraint) {
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Parameter Error")
                .setDescription(String.format("```%s```", constraint.getMessage()))
                .build()
        );
    }

    @Override
    public MessageCreateBuilder getCooldownMessage(@NotNull final CommandContext context, final long ms) {
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        final long s = seconds % 60;
        final long m = (seconds / 60) % 60;
        final long h = (seconds / (60 * 60)) % 24;
        final String cooldown = String.format("%d:%02d:%02d", h, m, s);
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Cooldown")
                .setDescription(String.format("You cannot use this command for %s!", cooldown))
                .build()
        );
    }

    @Override
    public MessageCreateBuilder getWrongChannelTypeMessage(@NotNull final CommandContext context) {
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Wrong Channel Type")
                .setDescription("This command cannot be executed in this type of channel!")
                .build()
        );
    }

    @Override
    public MessageCreateBuilder getCommandExecutionFailedMessage(@NotNull final CommandContext context, @NotNull final Exception exception) {
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Command Execution Failed")
                .setDescription(String.format("```%s```", exception))
                .build()
        );
    }

    @Override
    public MessageCreateBuilder getSlashCommandMigrationMessage(@NotNull final CommandContext context) {
        return new MessageCreateBuilder().setEmbeds(new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Deprecated")
                .setDescription("Text commands have been disabled. This command is now only available as a slash command. Type `/` to see a list of all available commands!")
                .build()
        );
    }
}
