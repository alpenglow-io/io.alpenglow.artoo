package re.artoo.lance.value;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import re.artoo.lance.experimental.value.Values;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class ValuesTest {
  @Test
  public void shouldPushValuesOnEmpty() {
    final var empty = Values.<Integer>empty();

    final var pushed = empty.push(1);
    assertThat(pushed.pop())
      .isInstanceOf(Integer.class)
      .isEqualTo(1);
  }

  @Test
  public void shouldPushValuesAndPopByThreads() throws InterruptedException {
    final var values = Values.<Integer>lock();

    final var pool = Executors.newFixedThreadPool(20);

    final var pushes = List.of(
      () -> values.push(1),
      () -> values.push(2),
      () -> values.push(3),
      () -> values.push(4),
      () -> values.push(5),
      () -> values.push(6),
      () -> values.push(7),
      () -> values.push(8),
      () -> values.push(9),
      (Callable<Values<Integer>>) () -> values.push(10)
    );

    pool.invokeAll(pushes);

    final var pops = List.of(
      values::pop,
      values::pop,
      values::pop,
      values::pop,
      values::pop,
      values::pop,
      values::pop,
      values::pop,
      values::pop,
      (Callable<Integer>) values::pop
    );

    pool.invokeAll(pops);
    pool.awaitTermination(20, SECONDS);

    assertThat(values.pop()).isNull();
  }
}
