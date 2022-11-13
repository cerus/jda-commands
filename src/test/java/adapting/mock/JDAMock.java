package adapting.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.sticker.StickerPack;
import net.dv8tion.jda.api.entities.sticker.StickerSnowflake;
import net.dv8tion.jda.api.entities.sticker.StickerUnion;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.DirectAudioController;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.restaction.GuildAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.cache.CacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JDAMock implements JDA {

    public static final User USER = new UserMock("user", 0);

    @Override
    public CacheRestAction<User> retrieveUserById(@NotNull final String id) {
        if (id.equals(USER.getId())) {
            return new CacheRestActionMock<>(USER);
        }
        throw ErrorResponseException.create(ErrorResponse.UNKNOWN_USER, new Response(new IllegalArgumentException(), new HashSet<>()));
    }

    @NotNull
    @Override
    public CacheRestAction<User> retrieveUserById(final long id) {
        return null;
    }

    @NotNull
    @Override
    public List<User> getUsersByName(@NotNull String name, final boolean ignoreCase) {
        String username = USER.getName();
        if (ignoreCase) {
            username = username.toUpperCase();
            name = name.toUpperCase();
        }
        if (name.equals(username)) {
            return new ArrayList<User>() {{
                this.add(USER);
            }};
        }
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public Status getStatus() {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<GatewayIntent> getGatewayIntents() {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<CacheFlag> getCacheFlags() {
        return null;
    }

    @Override
    public boolean unloadUser(final long l) {
        return false;
    }

    @Override
    public long getGatewayPing() {
        return 0;
    }

    @NotNull
    @Override
    public JDA awaitStatus(@NotNull final JDA.Status status, @NotNull final Status... statuses) throws InterruptedException {
        return null;
    }

    @Override
    public int cancelRequests() {
        return 0;
    }

    @NotNull
    @Override
    public ScheduledExecutorService getRateLimitPool() {
        return null;
    }

    @NotNull
    @Override
    public ScheduledExecutorService getGatewayPool() {
        return null;
    }

    @NotNull
    @Override
    public ExecutorService getCallbackPool() {
        return null;
    }

    @NotNull
    @Override
    public OkHttpClient getHttpClient() {
        return null;
    }

    @NotNull
    @Override
    public DirectAudioController getDirectAudioController() {
        return null;
    }

    @Override
    public void addEventListener(@NotNull final Object... objects) {

    }

    @Override
    public void removeEventListener(@NotNull final Object... objects) {

    }

    @NotNull
    @Override
    public List<Object> getRegisteredListeners() {
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
    public CommandCreateAction upsertCommand(@NotNull final CommandData commandData) {
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
    public GuildAction createGuild(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> createGuildFromTemplate(@NotNull final String s, @NotNull final String s1, @Nullable final Icon icon) {
        return null;
    }

    @NotNull
    @Override
    public CacheView<AudioManager> getAudioManagerCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<User> getUserCache() {
        return null;
    }

    @NotNull
    @Override
    public List<Guild> getMutualGuilds(@NotNull final User... users) {
        return null;
    }

    @NotNull
    @Override
    public List<Guild> getMutualGuilds(@NotNull final Collection<User> collection) {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<Guild> getGuildCache() {
        return null;
    }

    @NotNull
    @Override
    public Set<String> getUnavailableGuilds() {
        return null;
    }

    @Override
    public boolean isUnavailable(final long l) {
        return false;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<Role> getRoleCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<ScheduledEvent> getScheduledEventCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<PrivateChannel> getPrivateChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public CacheRestAction<PrivateChannel> openPrivateChannelById(final long userId) {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<RichCustomEmoji> getEmojiCache() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<StickerUnion> retrieveSticker(@NotNull final StickerSnowflake stickerSnowflake) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<StickerPack>> retrieveNitroStickerPacks() {
        return null;
    }

    @NotNull
    @Override
    public IEventManager getEventManager() {
        return null;
    }

    @Override
    public void setEventManager(@Nullable final IEventManager iEventManager) {

    }

    @NotNull
    @Override
    public SelfUser getSelfUser() {
        return null;
    }

    @NotNull
    @Override
    public Presence getPresence() {
        return null;
    }

    @NotNull
    @Override
    public ShardInfo getShardInfo() {
        return null;
    }

    @NotNull
    @Override
    public String getToken() {
        return null;
    }

    @Override
    public long getResponseTotal() {
        return 0;
    }

    @Override
    public int getMaxReconnectDelay() {
        return 0;
    }

    @Override
    public void setRequestTimeoutRetry(final boolean b) {

    }

    @Override
    public boolean isAutoReconnect() {
        return false;
    }

    @Override
    public void setAutoReconnect(final boolean b) {

    }

    @Override
    public boolean isBulkDeleteSplittingEnabled() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdownNow() {

    }

    @NotNull
    @Override
    public AccountType getAccountType() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<ApplicationInfo> retrieveApplicationInfo() {
        return null;
    }

    @NotNull
    @Override
    public JDA setRequiredScopes(@NotNull final Collection<String> collection) {
        return null;
    }

    @NotNull
    @Override
    public String getInviteUrl(@Nullable final Permission... permissions) {
        return null;
    }

    @NotNull
    @Override
    public String getInviteUrl(@Nullable final Collection<Permission> collection) {
        return null;
    }

    @Nullable
    @Override
    public ShardManager getShardManager() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Webhook> retrieveWebhookById(@NotNull final String s) {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<StageChannel> getStageChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<ThreadChannel> getThreadChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<Category> getCategoryCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<TextChannel> getTextChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<NewsChannel> getNewsChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<ForumChannel> getForumChannelCache() {
        return null;
    }
}
