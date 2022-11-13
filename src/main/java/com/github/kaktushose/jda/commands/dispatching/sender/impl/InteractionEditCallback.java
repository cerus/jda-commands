package com.github.kaktushose.jda.commands.dispatching.sender.impl;

import com.github.kaktushose.jda.commands.dispatching.sender.EditCallback;
import com.github.kaktushose.jda.commands.dispatching.sender.ReplyCallback;
import java.util.Collection;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link EditCallback} that can handle interaction events. More formally, this callback can handle
 * any event that is a subtype of {@link IMessageEditCallback}.
 *
 * @author Kaktushose
 * @version 2.3.0
 * @see EditCallback
 * @since 2.3.0
 */
public class InteractionEditCallback implements EditCallback {

    private final IMessageEditCallback event;
    private final Collection<ActionRow> actionRows;

    /**
     * Constructs a new {@link ReplyCallback}.
     *
     * @param event      the corresponding event
     * @param actionRows a {@link Collection} of {@link ActionRow ActionRows to send}
     */
    public InteractionEditCallback(final IMessageEditCallback event, final Collection<ActionRow> actionRows) {
        this.event = event;
        this.actionRows = actionRows;
    }

    @Override
    public void editMessage(@NotNull final String message, @Nullable final Consumer<Message> success) {
        this.initialReply(hook -> this.send(hook.editOriginal(message), success));
    }

    @Override
    public void editMessage(@NotNull final MessageEditData message, @Nullable final Consumer<Message> success) {
        this.initialReply(hook -> this.send(hook.editOriginal(message), success));
    }

    @Override
    public void editMessage(@NotNull final MessageEmbed embed, @Nullable final Consumer<Message> success) {
        this.initialReply(hook -> this.send(hook.editOriginalEmbeds(embed), success));
    }

    @Override
    public void deleteOriginal() {
        this.initialReply(hook -> hook.retrieveOriginal().queue(message -> {
            if (message.isEphemeral()) {
                hook.editOriginalEmbeds().setComponents().setContent("*deleted*").queue();
                return;
            }
            hook.deleteOriginal().queue();
        }));
    }

    @Override
    public void editComponents(@NotNull final LayoutComponent @NotNull ... components) {
        this.initialReply(hook -> hook.editOriginalComponents(components).queue());
    }

    @Override
    public void deferEdit() {
        this.event.deferEdit().queue();
    }

    private void send(final WebhookMessageEditAction<Message> restAction, final Consumer<Message> success) {
        if (this.actionRows.size() > 0) {
            restAction.setComponents(this.actionRows).queue(success);
        }
        restAction.queue(success);
    }

    private void initialReply(final Consumer<InteractionHook> consumer) {
        if (!this.event.isAcknowledged()) {
            this.event.deferEdit().queue(consumer);
            return;
        }
        consumer.accept(this.event.getHook());
    }

}
