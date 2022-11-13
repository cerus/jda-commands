package com.github.kaktushose.jda.commands.embeds.help;

import com.github.kaktushose.jda.commands.data.CommandList;
import com.github.kaktushose.jda.commands.dispatching.CommandContext;
import com.github.kaktushose.jda.commands.embeds.EmbedCache;
import com.github.kaktushose.jda.commands.reflect.CommandDefinition;
import com.github.kaktushose.jda.commands.reflect.CommandMetadata;
import com.github.kaktushose.jda.commands.reflect.ControllerDefinition;
import com.github.kaktushose.jda.commands.settings.GuildSettings;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Subtype of {@link DefaultHelpMessageFactory} that can load the embeds from an {@link EmbedCache}.
 *
 * @author Kaktushose
 * @version 2.0.0
 * @see DefaultHelpMessageFactory
 * @see EmbedCache
 * @since 2.0.0
 */
public class JsonHelpMessageFactory extends DefaultHelpMessageFactory {

    private final EmbedCache embedCache;

    public JsonHelpMessageFactory(final EmbedCache embedCache) {
        this.embedCache = embedCache;
    }

    @Override
    public MessageCreateBuilder getSpecificHelp(@NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("specificHelp")) {
            return super.getSpecificHelp(context);
        }

        final String prefix = Matcher.quoteReplacement(context.getContextualPrefix());
        final CommandDefinition command = context.getCommand();
        final CommandMetadata metadata = command.getMetadata();

        final List<String> labels = command.getLabels();
        final StringBuilder sbAliases = new StringBuilder();
        labels.subList(1, labels.size()).forEach(label -> sbAliases.append(label).append(", "));
        final String aliases = sbAliases.toString().isEmpty() ? "N/A" : sbAliases.substring(0, sbAliases.length() - 2);

        final StringBuilder sbPermissions = new StringBuilder();
        command.getPermissions().forEach(perm -> sbPermissions.append(perm).append(", "));
        final String permissions = sbPermissions.toString().isEmpty() ? "N/A" : sbPermissions.substring(0, sbPermissions.length() - 2);

        final EmbedBuilder builder = this.embedCache.getEmbed("specificHelp")
                .injectValue("prefix", prefix)
                .injectValue("label", command.getLabels().get(0))
                .injectValue("name", metadata.getName().replaceAll(this.prefixPattern, prefix))
                .injectValue("usage", metadata.getUsage().replaceAll(this.prefixPattern, prefix))
                .injectValue("aliases", aliases)
                .injectValue("description", metadata.getDescription().replaceAll(this.prefixPattern, prefix))
                .injectValue("permissions", permissions)
                .injectValue("category", metadata.getCategory().replaceAll(this.prefixPattern, prefix))
                .toEmbedBuilder();

        final StringBuilder sbCommands = new StringBuilder();
        final String name;
        if (command.isSuper()) {
            name = "Sub Commands:";
            final List<CommandDefinition> commands = command.getController().getSubCommands().stream().sorted().collect(Collectors.toList());
            commands.forEach(definition -> sbCommands.append(String.format("`%s`", definition.getLabels().get(0))).append(", "));
        } else {
            name = "Super Command:";
            final List<CommandDefinition> commands = command.getController().getSuperCommands().stream().sorted().collect(Collectors.toList());
            commands.forEach(definition -> sbCommands.append(String.format("`%s`", definition.getLabels().get(0))).append(", "));
        }
        final String commands = sbCommands.toString().isEmpty() ? "N/A" : sbCommands.substring(0, sbCommands.length() - 2);
        builder.addField(name, commands, false);

        return new MessageCreateBuilder().setEmbeds(builder.build());
    }

    @Override
    public MessageCreateBuilder getGenericHelp(@NotNull final Set<ControllerDefinition> controllers, @NotNull final CommandContext context) {
        if (!this.embedCache.containsEmbed("genericHelp")) {
            return super.getGenericHelp(controllers, context);
        }

        final String prefix = Matcher.quoteReplacement(context.getContextualPrefix());
        final GuildSettings settings = context.getSettings();
        final CommandList superCommands = new CommandList();
        controllers.forEach(definition -> superCommands.addAll(definition.getSuperCommands()));

        final EmbedBuilder builder = this.embedCache.getEmbed("genericHelp")
                .injectValue("prefix", prefix)
                .injectValue("helpLabel", settings.getHelpLabels().stream().findFirst().orElse("help"))
                .toEmbedBuilder();

        superCommands.getSortedByCategories().forEach((category, commands) -> {
            final StringBuilder sb = new StringBuilder();
            commands.forEach(command -> sb.append(String.format("`%s`", command.getLabels().get(0))).append(", "));
            builder.addField(category, sb.substring(0, sb.length() - 2), false);
        });

        return new MessageCreateBuilder().setEmbeds(builder.build());
    }

}
