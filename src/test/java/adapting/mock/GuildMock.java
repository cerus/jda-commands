package adapting.mock;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.VanityInvite;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.entities.sticker.StickerSnowflake;
import net.dv8tion.jda.api.entities.templates.Template;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.PrivilegeConfig;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.managers.GuildStickerManager;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.restaction.MemberAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import net.dv8tion.jda.api.requests.restaction.ScheduledEventAction;
import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.api.requests.restaction.pagination.BanPaginationAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.cache.MemberCacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
import net.dv8tion.jda.api.utils.concurrent.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildMock implements Guild {

    public static final Member MEMBER = new MemberMock("member", 1);
    public static final Role ROLE = new RoleMock("role", 2);
    public static final TextChannel TEXT_CHANNEL = new TextChannelMock("channel", 3);

    @NotNull
    @Override
    public CacheRestAction<Member> retrieveMemberById(@NotNull final String id) {
        if (id.equals(MEMBER.getId())) {
            return new CacheRestActionMock<>(MEMBER);
        }
        throw ErrorResponseException.create(ErrorResponse.UNKNOWN_USER, new Response(new IllegalArgumentException(), new HashSet<>()));
    }

    @NotNull
    @Override
    public CacheRestAction<Member> retrieveMemberById(final long id) {
        return null;
    }

    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByIds(final boolean b, @NotNull final long... longs) {
        return null;
    }

    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByPrefix(@NotNull final String s, final int i) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<ThreadChannel>> retrieveActiveThreads() {
        return null;
    }

    @NotNull
    @Override
    public CacheRestAction<ScheduledEvent> retrieveScheduledEventById(@NotNull final String id) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> moveVoiceMember(@NotNull final Member member, @Nullable final AudioChannel audioChannel) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyNickname(@NotNull final Member member, @Nullable final String s) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Integer> prune(final int i, final boolean b, @NotNull final Role... roles) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@NotNull final UserSnowflake userSnowflake, @Nullable final String s) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@NotNull final UserSnowflake user) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> ban(@NotNull final UserSnowflake user, final int deletionTimeframe, @NotNull final TimeUnit unit) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> unban(@NotNull final UserSnowflake userSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> timeoutUntil(@NotNull final UserSnowflake userSnowflake, @NotNull final TemporalAccessor temporalAccessor) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> removeTimeout(@NotNull final UserSnowflake userSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> deafen(@NotNull final UserSnowflake userSnowflake, final boolean b) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> mute(@NotNull final UserSnowflake userSnowflake, final boolean b) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> addRoleToMember(@NotNull final UserSnowflake userSnowflake, @NotNull final Role role) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> removeRoleFromMember(@NotNull final UserSnowflake userSnowflake, @NotNull final Role role) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(@NotNull final Member member, @Nullable final Collection<Role> collection, @Nullable final Collection<Role> collection1) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(@NotNull final Member member, @NotNull final Collection<Role> collection) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> transferOwnership(@NotNull final Member member) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<TextChannel> createTextChannel(@NotNull final String s, @Nullable final Category category) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<NewsChannel> createNewsChannel(@NotNull final String s, @Nullable final Category category) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<VoiceChannel> createVoiceChannel(@NotNull final String s, @Nullable final Category category) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<StageChannel> createStageChannel(@NotNull final String s, @Nullable final Category category) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<ForumChannel> createForumChannel(@NotNull final String name, @Nullable final Category parent) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<Category> createCategory(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public RoleAction createRole() {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<RichCustomEmoji> createEmoji(@NotNull final String s, @NotNull final Icon icon, @NotNull final Role... roles) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<GuildSticker> createSticker(@NotNull final String s, @NotNull final String s1, @NotNull final FileUpload fileUpload, @NotNull final Collection<String> collection) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> deleteSticker(@NotNull final StickerSnowflake stickerSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public ScheduledEventAction createScheduledEvent(@NotNull final String name, @NotNull final String location, @NotNull final OffsetDateTime startTime, @NotNull final OffsetDateTime endTime) {
        return null;
    }

    @NotNull
    @Override
    public ScheduledEventAction createScheduledEvent(@NotNull final String name, @NotNull final GuildChannel channel, @NotNull final OffsetDateTime startTime) {
        return null;
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyCategoryPositions() {
        return null;
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyTextChannelPositions() {
        return null;
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyVoiceChannelPositions() {
        return null;
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyTextChannelPositions(@NotNull final Category category) {
        return null;
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyVoiceChannelPositions(@NotNull final Category category) {
        return null;
    }

    @NotNull
    @Override
    public RoleOrderAction modifyRolePositions(final boolean b) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Command>> retrieveCommands() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Command>> retrieveCommands(final boolean withLocalizations) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Command> retrieveCommandById(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Command> upsertCommand(@NotNull final CommandData commandData) {
        return null;
    }

    @NotNull
    @Override
    public CommandListUpdateAction updateCommands() {
        return null;
    }

    @NotNull
    @Override
    public CommandEditAction editCommandById(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> deleteCommandById(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<IntegrationPrivilege>> retrieveIntegrationPrivilegesById(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<PrivilegeConfig> retrieveCommandPrivileges() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<EnumSet<Region>> retrieveRegions(final boolean b) {
        return null;
    }

    @NotNull
    @Override
    public MemberAction addMember(@NotNull final String s, @NotNull final UserSnowflake userSnowflake) {
        return null;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public void pruneMemberCache() {

    }

    @Override
    public boolean unloadMember(final long l) {
        return false;
    }

    @Override
    public int getMemberCount() {
        return 0;
    }

    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @Nullable
    @Override
    public String getIconId() {
        return null;
    }

    @NotNull
    @Override
    public Set<String> getFeatures() {
        return null;
    }

    @Nullable
    @Override
    public String getSplashId() {
        return null;
    }

    @Nullable
    @Override
    public String getVanityCode() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<VanityInvite> retrieveVanityInvite() {
        return null;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public DiscordLocale getLocale() {
        return null;
    }

    @Nullable
    @Override
    public String getBannerId() {
        return null;
    }

    @NotNull
    @Override
    public BoostTier getBoostTier() {
        return null;
    }

    @Override
    public int getBoostCount() {
        return 0;
    }

    @NotNull
    @Override
    public List<Member> getBoosters() {
        return null;
    }

    @Override
    public int getMaxMembers() {
        return 0;
    }

    @Override
    public int getMaxPresences() {
        return 0;
    }

    @NotNull
    @Override
    public RestAction<MetaData> retrieveMetaData() {
        return null;
    }

    @Nullable
    @Override
    public VoiceChannel getAfkChannel() {
        return null;
    }

    @Nullable
    @Override
    public TextChannel getSystemChannel() {
        return null;
    }

    @Nullable
    @Override
    public TextChannel getRulesChannel() {
        return null;
    }

    @Nullable
    @Override
    public TextChannel getCommunityUpdatesChannel() {
        return null;
    }

    @Nullable
    @Override
    public Member getOwner() {
        return null;
    }

    @Override
    public long getOwnerIdLong() {
        return 0;
    }

    @NotNull
    @Override
    public Timeout getAfkTimeout() {
        return null;
    }

    @Override
    public boolean isMember(@NotNull final UserSnowflake userSnowflake) {
        return false;
    }

    @NotNull
    @Override
    public Member getSelfMember() {
        return null;
    }

    @NotNull
    @Override
    public NSFWLevel getNSFWLevel() {
        return null;
    }

    @Nullable
    @Override
    public Member getMember(@NotNull final UserSnowflake userSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public List<Member> getMembersByEffectiveName(@NotNull String name, final boolean ignoreCase) {
        String username = MEMBER.getNickname();
        if (ignoreCase) {
            username = username.toUpperCase();
            name = name.toUpperCase();
        }
        if (name.equals(username)) {
            return new ArrayList<Member>() {{
                this.add(MEMBER);
            }};
        }
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public MemberCacheView getMemberCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<ScheduledEvent> getScheduledEventCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<StageChannel> getStageChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<ThreadChannel> getThreadChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<Category> getCategoryCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<TextChannel> getTextChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<NewsChannel> getNewsChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<ForumChannel> getForumChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public List<GuildChannel> getChannels(final boolean b) {
        return null;
    }

    @Nullable
    @Override
    public Role getRoleById(@NotNull final String id) {
        if (id.equals(ROLE.getId())) {
            return ROLE;
        }
        return null;
    }

    @NotNull
    @Override
    public List<Role> getRolesByName(@NotNull String name, final boolean ignoreCase) {
        String username = ROLE.getName();
        if (ignoreCase) {
            username = username.toUpperCase();
            name = name.toUpperCase();
        }
        if (name.equals(username)) {
            return new ArrayList<Role>() {{
                this.add(ROLE);
            }};
        }
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<Role> getRoleCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<RichCustomEmoji> getEmojiCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<GuildSticker> getStickerCache() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<RichCustomEmoji>> retrieveEmojis() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<RichCustomEmoji> retrieveEmojiById(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<GuildSticker>> retrieveStickers() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<GuildSticker> retrieveSticker(@NotNull final StickerSnowflake stickerSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public GuildStickerManager editSticker(@NotNull final StickerSnowflake stickerSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public BanPaginationAction retrieveBanList() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Ban> retrieveBan(@NotNull final UserSnowflake userSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Integer> retrievePrunableMemberCount(final int i) {
        return null;
    }

    @NotNull
    @Override
    public Role getPublicRole() {
        return null;
    }

    @Override
    public DefaultGuildChannelUnion getDefaultChannel() {
        return null;
    }

    @NotNull
    @Override
    public GuildManager getManager() {
        return null;
    }

    @Override
    public boolean isBoostProgressBarEnabled() {
        return false;
    }

    @NotNull
    @Override
    public AuditLogPaginationAction retrieveAuditLogs() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> leave() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> delete() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> delete(@Nullable final String s) {
        return null;
    }

    @NotNull
    @Override
    public AudioManager getAudioManager() {
        return null;
    }

    @NotNull
    @Override
    public Task<Void> requestToSpeak() {
        return null;
    }

    @NotNull
    @Override
    public Task<Void> cancelRequestToSpeak() {
        return null;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Invite>> retrieveInvites() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Template>> retrieveTemplates() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Template> createTemplate(@NotNull final String s, @Nullable final String s1) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Webhook>> retrieveWebhooks() {
        return null;
    }

    @NotNull
    @Override
    public List<GuildVoiceState> getVoiceStates() {
        return null;
    }

    @NotNull
    @Override
    public VerificationLevel getVerificationLevel() {
        return null;
    }

    @NotNull
    @Override
    public NotificationLevel getDefaultNotificationLevel() {
        return null;
    }

    @NotNull
    @Override
    public MFALevel getRequiredMFALevel() {
        return null;
    }

    @NotNull
    @Override
    public ExplicitContentLevel getExplicitContentLevel() {
        return null;
    }

    @NotNull
    @Override
    public Task<Void> loadMembers(@NotNull final Consumer<Member> consumer) {
        return null;
    }

    @Nullable
    @Override
    public TextChannel getTextChannelById(@NotNull final String id) {
        if (id.equals(TEXT_CHANNEL.getId())) {
            return TEXT_CHANNEL;
        }
        return null;
    }

    @NotNull
    @Override
    public List<TextChannel> getTextChannelsByName(@NotNull String name, final boolean ignoreCase) {
        String username = TEXT_CHANNEL.getName();
        if (ignoreCase) {
            username = username.toUpperCase();
            name = name.toUpperCase();
        }
        if (name.equals(username)) {
            return new ArrayList<TextChannel>() {{
                this.add(TEXT_CHANNEL);
            }};
        }
        return new ArrayList<>();
    }


    @Override
    public long getIdLong() {
        return 0;
    }
}
