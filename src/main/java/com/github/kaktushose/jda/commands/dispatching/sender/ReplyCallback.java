package com.github.kaktushose.jda.commands.dispatching.sender;

import com.github.kaktushose.jda.commands.embeds.EmbedDTO;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generic reply callback used to reply to {@link com.github.kaktushose.jda.commands.dispatching.GenericEvent GenericEvents}.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see EditCallback
 * @see com.github.kaktushose.jda.commands.dispatching.sender.impl.TextReplyCallback TextReplyCallback
 * @see com.github.kaktushose.jda.commands.dispatching.sender.impl.InteractionReplyCallback InteractionReplyCallback
 * @since 2.3.0
 */
public interface ReplyCallback {

    /**
     * Sends a message to the TextChannel where the command was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param message   the {@link String} message to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    void sendMessage(@NotNull String message, boolean ephemeral, @Nullable Consumer<Message> success);

    /**
     * Sends a message to the TextChannel where the command was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param embed     the {@link MessageEmbed} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    void sendMessage(@NotNull MessageEmbed embed, boolean ephemeral, @Nullable Consumer<Message> success);

    /**
     * Sends a message to the TextChannel where the command was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder   the {@link EmbedBuilder} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void sendMessage(@NotNull final EmbedBuilder builder, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.sendMessage(builder.build(), ephemeral, success);
    }

    /**
     * Sends a message to the TextChannel where the command was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder   the {@link MessageCreateBuilder} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void sendMessage(@NotNull final MessageCreateBuilder builder, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.sendMessage(builder.build(), ephemeral, success);
    }

    /**
     * Sends a message to the TextChannel where the command was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param data      the {@link MessageCreateData} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void sendMessage(@NotNull final MessageCreateData data, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.sendMessage(data, ephemeral, success);
    }

    /**
     * Sends a message to the TextChannel where the command was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param embedDTO  the {@link EmbedDTO} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void sendMessage(@NotNull final EmbedDTO embedDTO, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.sendMessage(embedDTO.toMessageEmbed(), ephemeral, success);
    }

    /**
     * Acknowledge this interaction and defer the reply to a later time.
     * See {@link net.dv8tion.jda.api.interactions.callbacks.IReplyCallback#deferReply(boolean) IReplyCallback#deferReply(boolean)}
     * for details.
     *
     * @param ephemeral whether to send an ephemeral reply
     */
    void deferReply(boolean ephemeral);

}
