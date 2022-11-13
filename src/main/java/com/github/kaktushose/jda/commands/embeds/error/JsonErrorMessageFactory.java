package com.github.kaktushose.jda.commands.embeds.error;

import com.github.kaktushose.jda.commands.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.embeds.EmbedCache;
import com.github.kaktushose.jda.commands.embeds.EmbedDTO;
import com.github.kaktushose.jda.commands.reflect.CommandDefinition;
import com.github.kaktushose.jda.commands.reflect.ConstraintDefinition;
import com.github.kaktushose.jda.commands.settings.GuildSettings;
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
 * Subtype of {@link DefaultErrorMessageFactory} that can load the embeds from an {@link EmbedCache}.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see DefaultErrorMessageFactory
 * @see EmbedCache
 * @since 2.0.0
 */
public class JsonErrorMessageFactory extends DefaultErrorMessageFactory {

    private final EmbedCache embedCache;

    public JsonErrorMessageFactory(final EmbedCache embedCache) {
        this.embedCache = embedCache;
    }

    @Override
    public MessageCreateBuilder getCommandNotFoundMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("commandNotFound")) {
            return super.getCommandNotFoundMessage(context);
        }

        final GuildSettings settings = context.getSettings();

        final EmbedDTO embedDTO = this.embedCache.getEmbed("commandNotFound")
                .injectValue("prefix", settings.getPrefix())
                .injectValue("helpLabel", settings.getHelpLabels().stream().findFirst().orElse("help"));
        final MessageEmbed embed;

        if (context.getPossibleCommands().isEmpty()) {
            final EmbedBuilder builder = embedDTO.toEmbedBuilder();
            builder.getFields().removeIf(field -> "{commands}".equals(field.getValue()));
            embed = builder.build();
        } else {
            final StringBuilder sbPossible = new StringBuilder();
            context.getPossibleCommands().forEach(command ->
                    sbPossible.append(String.format("`%s`", command.getLabels().get(0))).append(", ")
            );
            embedDTO.injectValue("commands", sbPossible.substring(0, sbPossible.length() - 2));
            embed = embedDTO.toMessageEmbed();
        }

        return new MessageCreateBuilder().setEmbeds(embed);
    }

    @Override
    public MessageCreateBuilder getInsufficientPermissionsMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("insufficientPermissions")) {
            return super.getInsufficientPermissionsMessage(context);
        }

        final GuildSettings settings = context.getSettings();
        final CommandDefinition command = context.getCommand();
        final StringBuilder sbPermissions = new StringBuilder();
        command.getPermissions().forEach(permission -> sbPermissions.append(permission).append(", "));
        final String permissions = sbPermissions.toString().isEmpty() ? "N/A" : sbPermissions.substring(0, sbPermissions.length() - 2);

        return this.embedCache.getEmbed("insufficientPermissions")
                .injectValue("prefix", settings.getPrefix())
                .injectValue("label", command.getLabels().get(0))
                .injectValue("permissions", permissions)
                .toMessage();
    }

    @Override
    public MessageCreateBuilder getGuildMutedMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("guildMuted")) {
            return super.getGuildMutedMessage(context);
        }
        return this.embedCache.getEmbed("guildMuted").toMessage();
    }

    @Override
    public MessageCreateBuilder getChannelMutedMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("channelMuted")) {
            return super.getChannelMutedMessage(context);
        }
        return this.embedCache.getEmbed("channelMuted").toMessage();
    }

    @Override
    public MessageCreateBuilder getUserMutedMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("userMuted")) {
            return super.getUserMutedMessage(context);
        }
        return this.embedCache.getEmbed("userMuted").toMessage();
    }


    @Override
    public MessageCreateBuilder getSyntaxErrorMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("syntaxError")) {
            return super.getSyntaxErrorMessage(context);
        }
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

        return this.embedCache.getEmbed("syntaxError")
                .injectValue("usage", command.getMetadata().getUsage().replaceAll("\\{prefix}",
                        Matcher.quoteReplacement(context.getSettings().getPrefix()))
                )
                .injectValue("expected", expected)
                .injectValue("actual", actual)
                .toMessage();
    }

    @Override
    public MessageCreateBuilder getConstraintFailedMessage(@NotNull final CommandContext context, @NotNull final ConstraintDefinition constraint) {
        if (!this.embedCache.containsEmbed("constraintFailed")) {
            return super.getConstraintFailedMessage(context, constraint);
        }
        return this.embedCache.getEmbed("constraintFailed")
                .injectValue("message", constraint.getMessage())
                .toMessage();
    }

    @Override
    public MessageCreateBuilder getCooldownMessage(@NotNull final CommandContext context, final long ms) {
        if (!this.embedCache.containsEmbed("cooldown")) {
            return super.getCooldownMessage(context, ms);
        }
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        final long s = seconds % 60;
        final long m = (seconds / 60) % 60;
        final long h = (seconds / (60 * 60)) % 24;
        final String cooldown = String.format("%d:%02d:%02d", h, m, s);

        return this.embedCache.getEmbed("cooldown")
                .injectValue("cooldown", cooldown)
                .toMessage();
    }

    @Override
    public MessageCreateBuilder getWrongChannelTypeMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("wrongChannel")) {
            return super.getInsufficientPermissionsMessage(context);
        }
        return this.embedCache.getEmbed("wrongChannel").toMessage();
    }

    @Override
    public MessageCreateBuilder getCommandExecutionFailedMessage(@NotNull final CommandContext context, @NotNull final Exception exception) {
        if (!this.embedCache.containsEmbed("executionFailed")) {
            return super.getCommandExecutionFailedMessage(context, exception);
        }
        return this.embedCache.getEmbed("executionFailed")
                .injectValue("exception", exception.toString())
                .toMessage();
    }

    @Override
    public MessageCreateBuilder getSlashCommandMigrationMessage(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("migration")) {
            return super.getInsufficientPermissionsMessage(context);
        }
        return this.embedCache.getEmbed("migration").toMessage();
    }
}
