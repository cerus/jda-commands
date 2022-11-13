package com.github.kaktushose.jda.commands.dispatching.sender;

import com.github.kaktushose.jda.commands.embeds.EmbedDTO;
import com.github.kaktushose.jda.commands.interactions.components.Component;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generic interface holding edit methods. Uses a {@link EditCallback} to edit the replies.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see ReplyAction
 * @see EditCallback
 * @since 2.3.0
 */
public interface EditAction {

    /**
     * Edits the original message the button is attached to.
     *
     * @param message the new message
     */
    default void edit(@NotNull final String message) {
        this.edit(message, (Consumer<Message>) null);
    }

    /**
     * Edits the original message the button is attached to using the specified format string and arguments.
     *
     * @param format the new message
     * @param args   Arguments referenced by the format specifiers in the format string. If there are more arguments than
     *               format specifiers, the extra arguments are ignored. The number of arguments is variable and may be
     *               zero.
     *
     * @throws java.util.IllegalFormatException If a format string contains an illegal syntax, a format specifier that
     *                                          is incompatible with the given arguments, insufficient arguments given
     *                                          the format string, or other illegal conditions.
     */
    default void edit(@NotNull final String format, @NotNull final Object... args) {
        this.edit(String.format(format, args));
    }

    /**
     * Edits the original message the button is attached to.
     *
     * @param builder the new {@code EmbedBuilder}
     */
    default void edit(@NotNull final EmbedBuilder builder) {
        this.edit(builder, null);
    }

    /**
     * Edits the original message the button is attached to.
     *
     * @param embedDTO the new {@link EmbedDTO}
     */
    default void edit(@NotNull final EmbedDTO embedDTO) {
        this.edit(embedDTO, null);
    }

    /**
     * Edits the original message the button is attached to. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param message the new message
     * @param success the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void edit(@NotNull final String message, @Nullable final Consumer<Message> success) {
        this.getEditCallback().editMessage(message, success);
    }

    /**
     * Edits the original message the button is attached to. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder the new {@link EmbedBuilder}
     * @param success the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void edit(@NotNull final EmbedBuilder builder, @Nullable final Consumer<Message> success) {
        this.getEditCallback().editMessage(builder, success);
    }

    /**
     * Edits the original message the button is attached to. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param builder the new {@link MessageEditBuilder}
     * @param success the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void edit(@NotNull final MessageEditBuilder builder, @Nullable final Consumer<Message> success) {
        this.getEditCallback().editMessage(builder, success);
    }

    /**
     * Edits the original message the button is attached to. This method also allows to access the JDA RestAction
     * consumer.
     *
     * @param embedDTO the new {@link EmbedDTO}
     * @param success  the JDA RestAction success consumer
     *
     * @see <a href="https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html">JDA RestAction Documentation</a>
     */
    default void edit(@NotNull final EmbedDTO embedDTO, @Nullable final Consumer<Message> success) {
        this.getEditCallback().editMessage(embedDTO, success);
    }

    /**
     * Edits the buttons attached to the message without changing the message itself. The buttons must be defined in the
     * same {@link com.github.kaktushose.jda.commands.annotations.CommandController CommandController} as the referring
     * {@link com.github.kaktushose.jda.commands.annotations.Command Command}.
     *
     * @param components the {@link Component Components} to edit
     *
     * @return the current instance for fluent interface
     */
    EditAction editComponents(@NotNull Component... components);

    /**
     * Deletes the original message the button was attached to.
     */
    default EditAction deleteOriginal() {
        this.getEditCallback().deleteOriginal();
        return this;
    }

    /**
     * Removes all components form the original message.
     *
     * @return the current instance for fluent interface
     */
    default EditAction clearComponents() {
        this.getEditCallback().editComponents();
        return this;
    }

    /**
     * No-op acknowledgement of this interaction. See
     * {@link net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback#deferEdit() IMessageEditCallback#deferEdit()}
     * for details.
     */
    default EditAction deferEdit() {
        this.getEditCallback().deferEdit();
        return this;
    }

    /**
     * Gets the {@link EditCallback} to use.
     *
     * @return the {@link EditCallback}
     */
    @NotNull
    EditCallback getEditCallback();

}
