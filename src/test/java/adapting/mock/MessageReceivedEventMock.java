package adapting.mock;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedEventMock extends MessageReceivedEvent {

    private final boolean isGuildEvent;

    public MessageReceivedEventMock(final boolean isGuildEvent) {
        super(new JDAMock(), 0, new MessageMock());
        this.isGuildEvent = isGuildEvent;
    }

    @Override
    public boolean isFromType(@NotNull final ChannelType type) {
        return this.isGuildEvent;
    }

    @Override
    public Guild getGuild() {
        return new GuildMock();
    }
}
