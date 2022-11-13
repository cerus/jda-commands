package com.github.kaktushose.jda.commands.dispatching.sender.impl;

import com.github.kaktushose.jda.commands.dispatching.sender.ReplyCallback;
import java.util.Collection;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link ReplyCallback} that can handle interaction events. More formally, this callback can handle
 * any event that is a subtype of {@link IReplyCallback}.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see ReplyCallback
 * @see TextReplyCallback
 * @since 2.3.0
 */
public class InteractionReplyCallback implements ReplyCallback {

    private final IReplyCallback event;
    private final Collection<ActionRow> actionRows;

    /**
     * Constructs a new {@link ReplyCallback}.
     *
     * @param event      the corresponding event
     * @param actionRows a {@link Collection} of {@link ActionRow ActionRows to send}
     */
    public InteractionReplyCallback(@NotNull final IReplyCallback event, @NotNull final Collection<ActionRow> actionRows) {
        this.event = event;
        this.actionRows = actionRows;
    }

    @Override
    public void sendMessage(@NotNull final String message, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.initialReply(ephemeral, hook -> hook.sendMessage(message).addComponents(this.actionRows).queue(success));
    }

    @Override
    public void sendMessage(@NotNull final MessageCreateData message, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.initialReply(ephemeral, hook -> hook.sendMessage(message).addComponents(this.actionRows).queue(success));
    }

    @Override
    public void sendMessage(@NotNull final MessageEmbed embed, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.initialReply(ephemeral, hook -> hook.sendMessageEmbeds(embed).addComponents(this.actionRows).queue(success));
    }

    @Override
    public void deferReply(final boolean ephemeral) {
        this.event.deferReply(ephemeral).queue();
    }

    private void initialReply(final boolean ephemeral, final Consumer<InteractionHook> consumer) {
        if (!this.event.isAcknowledged()) {
            this.event.deferReply().setEphemeral(ephemeral).queue(consumer);
            return;
        }
        consumer.accept(this.event.getHook().setEphemeral(ephemeral));
    }
}
