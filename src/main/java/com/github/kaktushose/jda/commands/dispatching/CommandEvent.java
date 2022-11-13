package com.github.kaktushose.jda.commands.dispatching;

import com.github.kaktushose.jda.commands.JDACommands;
import com.github.kaktushose.jda.commands.dispatching.sender.EditAction;
import com.github.kaktushose.jda.commands.dispatching.sender.ReplyAction;
import com.github.kaktushose.jda.commands.dispatching.sender.ReplyCallback;
import com.github.kaktushose.jda.commands.dispatching.sender.impl.InteractionReplyCallback;
import com.github.kaktushose.jda.commands.dispatching.sender.impl.TextReplyCallback;
import com.github.kaktushose.jda.commands.embeds.help.HelpMessageFactory;
import com.github.kaktushose.jda.commands.interactions.components.Buttons;
import com.github.kaktushose.jda.commands.interactions.components.Component;
import com.github.kaktushose.jda.commands.reflect.CommandDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import org.jetbrains.annotations.NotNull;

/**
 * This class is a subclass of {@link GenericEvent}.
 * It provides some additional features for sending messages and also grants
 * access to the {@link CommandDefinition} object which describes the command that is executed.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see GenericEvent
 * @see ReplyAction
 * @see EditAction
 * @since 1.0.0
 */
public class CommandEvent extends GenericEvent implements ReplyAction {

    private final CommandDefinition command;
    private final CommandContext context;
    private final List<ActionRow> actionRows;
    private ReplyCallback replyCallback;

    /**
     * Constructs a CommandEvent.
     *
     * @param command the underlying {@link CommandDefinition} object
     * @param context the {@link CommandContext}
     */
    public CommandEvent(@NotNull final CommandDefinition command, @NotNull final CommandContext context) {
        super(context.getEvent());
        this.command = command;
        this.context = context;
        this.actionRows = new ArrayList<>();
        if (context.isSlash()) {
            this.replyCallback = new InteractionReplyCallback(context.getInteractionEvent(), this.actionRows);
        } else {
            this.replyCallback = new TextReplyCallback(this.getChannel(), this.actionRows);
        }
    }

    /**
     * Sends the generic help message via the
     * {@link com.github.kaktushose.jda.commands.dispatching.sender.MessageSender MessageSender}
     */
    public void sendGenericHelpMessage() {
        this.getJdaCommands().getImplementationRegistry().getMessageSender().sendGenericHelpMessage(
                this.context,
                this.getHelpMessageFactory().getGenericHelp(this.getJdaCommands().getCommandRegistry().getControllers(), this.context).build()
        );
    }

    /**
     * Sends the specific help message for this command via the
     * {@link com.github.kaktushose.jda.commands.dispatching.sender.MessageSender MessageSender}
     */
    public void sendSpecificHelpMessage() {
        this.getJdaCommands().getImplementationRegistry().getMessageSender().sendSpecificHelpMessage(
                this.context,
                this.getHelpMessageFactory().getSpecificHelp(this.context).build()
        );
    }

    /**
     * Replies to this event with the generic help embed.
     */
    public void replyGenericHelp() {
        this.reply(this.getHelpMessageFactory().getGenericHelp(this.getJdaCommands().getCommandRegistry().getControllers(), this.context));
    }

    /**
     * Replies to this event with the specific help embed.
     */
    public void replySpecificHelp() {
        this.reply(this.getHelpMessageFactory().getSpecificHelp(this.context));
    }

    /**
     * Replies to this event with the generic help embed.
     *
     * @param ephemeral whether to send an ephemeral reply
     */
    public void replyGenericHelp(final boolean ephemeral) {
        this.reply(this.getHelpMessageFactory().getGenericHelp(this.getJdaCommands().getCommandRegistry().getControllers(), this.context), ephemeral);
    }

    /**
     * Replies to this event with the specific help embed.
     *
     * @param ephemeral whether to send an ephemeral reply
     */
    public void replySpecificHelp(final boolean ephemeral) {
        this.reply(this.getHelpMessageFactory().getSpecificHelp(this.context), ephemeral);
    }

    @Override
    public CommandEvent with(@NotNull final Component... components) {
        final List<ItemComponent> items = new ArrayList<>();
        for (final Component component : components) {
            if (!(component instanceof Buttons)) {
                return this;
            }
            final Buttons buttons = (Buttons) component;
            buttons.getButtons().forEach(button -> {
                final String id = String.format("%s.%s", this.command.getMethod().getDeclaringClass().getSimpleName(), button.getId());
                this.command.getController().getButtons()
                        .stream()
                        .filter(it -> it.getId().equals(id))
                        .findFirst()
                        .map(it -> it.toButton().withDisabled(!button.isEnabled()))
                        .ifPresent(items::add);
            });
        }
        if (items.size() > 0) {
            this.actionRows.add(ActionRow.of(items));
        }
        return this;
    }

    /**
     * Get the {@link CommandDefinition} object which describes the command that is executed.
     *
     * @return the underlying {@link CommandDefinition} object
     */
    public CommandDefinition getCommandDefinition() {
        return this.command;
    }

    /**
     * Get the {@link JDACommands} object.
     *
     * @return the {@link JDACommands} object
     */
    public JDACommands getJdaCommands() {
        return this.context.getJdaCommands();
    }

    /**
     * Get the registered {@link HelpMessageFactory} object.
     *
     * @return the registered {@link HelpMessageFactory} object
     */
    public HelpMessageFactory getHelpMessageFactory() {
        return this.getJdaCommands().getImplementationRegistry().getHelpMessageFactory();
    }

    /**
     * Get the {@link CommandContext} object.
     *
     * @return the registered {@link CommandContext} object
     */
    public CommandContext getCommandContext() {
        return this.context;
    }

    /**
     * Gets the {@link InteractionHook}. The {@link InteractionHook} is only available if the underlying event was a
     * {@link net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent SlashCommandInteractionEvent}.
     *
     * @return an {@link Optional} holding the {@link InteractionHook}.
     */
    public Optional<InteractionHook> getInteractionHook() {
        return Optional.ofNullable(this.context.getInteractionEvent()).map(GenericCommandInteractionEvent::getHook);
    }

    /**
     * Gets the {@link ActionRow ActionRows} already added to the reply.
     *
     * @return a possibly-empty {@link List} of {@link ActionRow ActionRows}.
     */
    public List<ActionRow> getActionRows() {
        return new ArrayList<>(this.actionRows);
    }

    @Override
    public @NotNull ReplyCallback getReplyCallback() {
        return this.replyCallback;
    }

    /**
     * Sets the {@link ReplyCallback} used to send replies to this event.
     *
     * @param replyCallback the {@link ReplyCallback} to use
     */
    public void setReplyCallback(final ReplyCallback replyCallback) {
        this.replyCallback = replyCallback;
    }

    @Override
    public boolean isEphemeral() {
        return this.command.isEphemeral();
    }
}
