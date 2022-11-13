package com.github.kaktushose.jda.commands.dispatching.sender;

import com.github.kaktushose.jda.commands.embeds.EmbedDTO;
import com.github.kaktushose.jda.commands.interactions.components.Buttons;
import com.github.kaktushose.jda.commands.interactions.components.Component;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generic interface holding reply methods. Uses a {@link ReplyCallback} to send the replies.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see EditAction
 * @see ReplyCallback
 * @since 2.3.0
 */
public interface ReplyAction {

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param message the message to send
     */
    default void reply(@NotNull final String message) {
        this.reply(message, this.isEphemeral(), (Consumer<Message>) null);
    }

    /**
     * Sends a formatted message using the specified format string and arguments to the TextChannel where the button was called.
     *
     * @param format the message to send
     * @param args   Arguments referenced by the format specifiers in the format string. If there are more arguments than
     *               format specifiers, the extra arguments are ignored. The number of arguments is variable and may be
     *               zero.
     *
     * @throws java.util.IllegalFormatException If a format string contains an illegal syntax, a format specifier that
     *                                          is incompatible with the given arguments, insufficient arguments given
     *                                          the format string, or other illegal conditions.
     */
    default void reply(@NotNull final String format, @NotNull final Object... args) {
        this.reply(String.format(format, args), this.isEphemeral());
    }

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param message the {@code Message} to send
     */
    default void reply(@NotNull final MessageCreateData message) {
        this.reply(message, this.isEphemeral(), null);
    }

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param builder the {@code MessageBuilder} to send
     */
    default void reply(@NotNull final MessageCreateBuilder builder) {
        this.reply(builder, this.isEphemeral(), null);
    }


    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param builder the {@code EmbedBuilder} to send
     */
    default void reply(@NotNull final EmbedBuilder builder) {
        this.reply(builder, this.isEphemeral(), null);
    }

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param embedDTO the {@link EmbedDTO} to send
     */
    default void reply(@NotNull final EmbedDTO embedDTO) {
        this.reply(embedDTO, this.isEphemeral(), null);
    }

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param message the message to send
     */
    default void reply(@NotNull final String message, final boolean ephemeral) {
        this.reply(message, ephemeral, (Consumer<Message>) null);
    }

    /**
     * Sends a formatted message using the specified format string and arguments to the TextChannel where the button was called.
     *
     * @param format    the message to send
     * @param ephemeral whether to send an ephemeral reply
     * @param args      Arguments referenced by the format specifiers in the format string. If there are more arguments than
     *                  format specifiers, the extra arguments are ignored. The number of arguments is variable and may be
     *                  zero.
     *
     * @throws java.util.IllegalFormatException If a format string contains an illegal syntax, a format specifier that
     *                                          is incompatible with the given arguments, insufficient arguments given
     *                                          the format string, or other illegal conditions.
     */
    default void reply(@NotNull final String format, final boolean ephemeral, @NotNull final Object... args) {
        this.reply(String.format(format, args), ephemeral);
    }

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param message   the {@code Message} to send
     * @param ephemeral whether to send an ephemeral reply
     */
    default void reply(@NotNull final MessageCreateData message, final boolean ephemeral) {
        this.reply(message, ephemeral, null);
    }

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param builder   the {@code MessageBuilder} to send
     * @param ephemeral whether to send an ephemeral reply
     */
    default void reply(@NotNull final MessageCreateBuilder builder, final boolean ephemeral) {
        this.reply(builder, ephemeral, null);
    }


    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param builder   the {@code EmbedBuilder} to send
     * @param ephemeral whether to send an ephemeral reply
     */
    default void reply(@NotNull final EmbedBuilder builder, final boolean ephemeral) {
        this.reply(builder, ephemeral, null);
    }

    /**
     * Sends a message to the TextChannel where the button was called.
     *
     * @param embedDTO  the {@link EmbedDTO} to send
     * @param ephemeral whether to send an ephemeral reply
     */
    default void reply(@NotNull final EmbedDTO embedDTO, final boolean ephemeral) {
        this.reply(embedDTO, ephemeral, null);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param message the {@link String} message to send
     * @param success the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final String message, @Nullable final Consumer<Message> success) {
        this.reply(message, this.isEphemeral(), success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param message the {@link Message} to send
     * @param success the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final MessageCreateData message, @Nullable final Consumer<Message> success) {
        this.reply(message, this.isEphemeral(), success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder the {@link EmbedBuilder} to send
     * @param success the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final EmbedBuilder builder, @Nullable final Consumer<Message> success) {
        this.reply(builder, this.isEphemeral(), success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder the {@link MessageCreateBuilder} to send
     * @param success the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final MessageCreateBuilder builder, @Nullable final Consumer<Message> success) {
        this.reply(builder, this.isEphemeral(), success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param embedDTO the {@link EmbedDTO} to send
     * @param success  the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final EmbedDTO embedDTO, @Nullable final Consumer<Message> success) {
        this.reply(embedDTO, this.isEphemeral(), success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param message   the {@link String} message to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final String message, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.getReplyCallback().sendMessage(message, ephemeral, success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param message   the {@link Message} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final MessageCreateData message, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.getReplyCallback().sendMessage(message, ephemeral, success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder   the {@link EmbedBuilder} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final EmbedBuilder builder, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.getReplyCallback().sendMessage(builder, ephemeral, success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder   the {@link MessageCreateBuilder} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final MessageCreateBuilder builder, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.getReplyCallback().sendMessage(builder, ephemeral, success);
    }

    /**
     * Sends a message to the TextChannel where the button was called. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param embedDTO  the {@link EmbedDTO} to send
     * @param ephemeral whether to send an ephemeral reply
     * @param success   the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void reply(@NotNull final EmbedDTO embedDTO, final boolean ephemeral, @Nullable final Consumer<Message> success) {
        this.getReplyCallback().sendMessage(embedDTO, ephemeral, success);
    }

    /**
     * Adds an {@link ActionRow} to the reply and adds the passed {@link Component Components} to it.
     * For buttons, they must be defined in the same
     * {@link com.github.kaktushose.jda.commands.annotations.CommandController CommandController} as the referring
     * {@link com.github.kaktushose.jda.commands.annotations.Command Command}.
     *
     * @param components the {@link Component Components} to add
     *
     * @return the current instance for fluent interface
     */
    ReplyAction with(@NotNull Component... components);

    /**
     * Adds an {@link ActionRow} to the reply and adds the passed {@link Component Components} to it.
     * The buttons must be defined in the same
     * {@link com.github.kaktushose.jda.commands.annotations.CommandController CommandController} as the referring
     * {@link com.github.kaktushose.jda.commands.annotations.Command Command}. This will enable all buttons. To add
     * disabled buttons, use {@link #with(Component...)}.
     *
     * @param buttons the id of the buttons to add
     *
     * @return the current instance for fluent interface
     */
    default ReplyAction withButtons(@NotNull final String... buttons) {
        this.with(Buttons.enabled(buttons));
        return this;
    }

    /**
     * Acknowledge this interaction and defer the reply to a later time.
     * See {@link net.dv8tion.jda.api.interactions.callbacks.IReplyCallback#deferReply(boolean) IReplyCallback#deferReply(boolean)}
     * for details.
     */
    default ReplyAction deferReply() {
        this.getReplyCallback().deferReply(this.isEphemeral());
        return this;
    }

    /**
     * Gets the {@link ReplyCallback} to use.
     *
     * @return the {@link ReplyCallback}
     */
    @NotNull
    ReplyCallback getReplyCallback();

    /**
     * Whether to reply ephemeral.
     *
     * @return {@code true} if replies should be ephemeral
     */
    boolean isEphemeral();

}
