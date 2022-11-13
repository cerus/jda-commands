package com.github.kaktushose.jda.commands.dispatching.sender.impl;

import com.github.kaktushose.jda.commands.dispatching.sender.ReplyCallback;
import java.util.Collection;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link ReplyCallback} that can handle any type of event. More formally, this callback can handle
 * any event that has a {@link MessageChannel} linked to it.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see ReplyCallback
 * @see InteractionReplyCallback
 * @since 2.3.0
 */
public class TextReplyCallback implements ReplyCallback {

    private final MessageChannel channel;
    private final Collection<ActionRow> actionRows;

    /**
     * Constructs a new {@link ReplyCallback}.
     *
     * @param channel    the corresponding {@link TextChannel}
     * @param actionRows a {@link Collection} of {@link ActionRow ActionRows to send}
     */
    public TextReplyCallback(final MessageChannel channel, final Collection<ActionRow> actionRows) {
        this.channel = channel;
        this.actionRows = actionRows;
    }

    @Override
    public void sendMessage(@NotNull final String message, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.channel.sendMessage(message).addComponents(this.actionRows).queue(success);
    }

    @Override
    public void sendMessage(@NotNull final MessageCreateData message, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.channel.sendMessage(message).addComponents(this.actionRows).queue(success);
    }

    @Override
    public void sendMessage(@NotNull final MessageEmbed embed, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.channel.sendMessageEmbeds(embed).addComponents(this.actionRows).queue(success);
    }

    @Override
    public void deferReply(final boolean ephemeral) {
    }

}
