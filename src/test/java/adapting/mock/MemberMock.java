package adapting.mock;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.attribute.IPermissionContainer;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemberMock implements Member {

    private final String name;
    private final long id;

    public MemberMock(final String name, final long id) {
        this.name = name;
        this.id = id;
    }

    @NotNull
    @Override
    public User getUser() {
        return null;
    }

    @NotNull
    @Override
    public Guild getGuild() {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissions() {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissions(@NotNull final GuildChannel channel) {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit() {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit(@NotNull final GuildChannel channel) {
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull final Permission... permissions) {
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull final Collection<Permission> collection) {
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull final GuildChannel channel, @NotNull final Permission... permissions) {
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull final GuildChannel channel, @NotNull final Collection<Permission> permissions) {
        return false;
    }

    @Override
    public boolean canSync(@NotNull final IPermissionContainer targetChannel, @NotNull final IPermissionContainer syncSource) {
        return false;
    }

    @Override
    public boolean canSync(@NotNull final IPermissionContainer channel) {
        return false;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return null;
    }

    @NotNull
    @Override
    public OffsetDateTime getTimeJoined() {
        return null;
    }

    @Override
    public boolean hasTimeJoined() {
        return false;
    }

    @Nullable
    @Override
    public OffsetDateTime getTimeBoosted() {
        return null;
    }

    @Override
    public boolean isBoosting() {
        return false;
    }

    @Nullable
    @Override
    public OffsetDateTime getTimeOutEnd() {
        return null;
    }

    @Nullable
    @Override
    public GuildVoiceState getVoiceState() {
        return null;
    }

    @NotNull
    @Override
    public List<Activity> getActivities() {
        return null;
    }

    @NotNull
    @Override
    public OnlineStatus getOnlineStatus() {
        return null;
    }

    @NotNull
    @Override
    public OnlineStatus getOnlineStatus(@NotNull final ClientType clientType) {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<ClientType> getActiveClients() {
        return null;
    }

    @Nullable
    @Override
    public String getNickname() {
        return this.name;
    }

    @NotNull
    @Override
    public String getEffectiveName() {
        return this.name;
    }

    @Nullable
    @Override
    public String getAvatarId() {
        return null;
    }

    @Nullable
    @Override
    public String getAvatarUrl() {
        return Member.super.getAvatarUrl();
    }

    @NotNull
    @Override
    public String getEffectiveAvatarUrl() {
        return Member.super.getEffectiveAvatarUrl();
    }

    @NotNull
    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Nullable
    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public int getColorRaw() {
        return 0;
    }

    @Override
    public boolean canInteract(@NotNull final Member member) {
        return false;
    }

    @Override
    public boolean canInteract(@NotNull final Role role) {
        return false;
    }

    @Override
    public boolean canInteract(@NotNull final RichCustomEmoji richCustomEmoji) {
        return false;
    }

    @Override
    public boolean isOwner() {
        return false;
    }

    @Override
    public boolean isPending() {
        return false;
    }

    @Nullable
    @Override
    public DefaultGuildChannelUnion getDefaultChannel() {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick() {
        return Member.super.kick();
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@Nullable final String reason) {
        return Member.super.kick(reason);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> mute(final boolean mute) {
        return Member.super.mute(mute);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> deafen(final boolean deafen) {
        return Member.super.deafen(deafen);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyNickname(@Nullable final String nickname) {
        return Member.super.modifyNickname(nickname);
    }

    @NotNull
    @Override
    public String getAsMention() {
        return null;
    }

    @Override
    public void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        Member.super.formatTo(formatter, flags, width, precision);
    }

    @NotNull
    @Override
    public String getId() {
        return Member.super.getId();
    }

    @Override
    public long getIdLong() {
        return this.id;
    }

    @NotNull
    @Override
    public OffsetDateTime getTimeCreated() {
        return Member.super.getTimeCreated();
    }
}
