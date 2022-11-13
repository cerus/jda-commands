package adapting.mock;

import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CacheRestActionMock<T> implements CacheRestAction<T> {

    private final T member;

    public CacheRestActionMock(final T member) {
        this.member = member;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return null;
    }

    @Override
    public void queue(@Nullable final Consumer<? super T> consumer, @Nullable final Consumer<? super Throwable> consumer1) {

    }

    @Override
    public T complete(final boolean b) throws RateLimitedException {
        return this.member;
    }

    @NotNull
    @Override
    public CompletableFuture<T> submit(final boolean b) {
        return null;
    }

    @NotNull
    @Override
    public CacheRestAction<T> setCheck(@Nullable final BooleanSupplier checks) {
        return null;
    }

    @NotNull
    @Override
    public CacheRestAction<T> useCache(final boolean useCache) {
        return null;
    }
}
