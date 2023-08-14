package re.artoo.lance.scope;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import re.artoo.lance.func.TryRunnable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LocalTest {
  @Test
  @Disabled
  public void shouldGetValue() throws InterruptedException {
    final var value = Let.lazy(() -> 0);

    final var pool = Executors.newFixedThreadPool(5);

    final var atomic = new AtomicInteger(0);
    final var threads = List.<TryRunnable>of(
      () -> atomic.set(value.let(it -> atomic.get() + 1)),
      () -> atomic.set(value.let(it -> atomic.get() + 2)),
      () -> atomic.set(value.let(it -> atomic.get() + 3)),
      () -> atomic.set(value.let(it -> atomic.get() + 4)),
      () -> atomic.set(value.let(it -> atomic.get() + 5)),
      () -> atomic.set(value.let(it -> atomic.get() + 6)),
      () -> atomic.set(value.let(it -> atomic.get() + 7)),
      () -> atomic.set(value.let(it -> atomic.get() + 8)),
      () -> atomic.set(value.let(it -> atomic.get() + 9)),
      () -> atomic.set(value.let(it -> atomic.get() + 10))
    );

    pool.invokeAll(threads);
    final var terminated = pool.awaitTermination(10, SECONDS);

    assertThat(atomic.get()).isEqualTo(55);
  }
}
