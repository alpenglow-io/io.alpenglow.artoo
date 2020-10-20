package io.artoo.lance.type;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ValueTest {
  @Test
  void shouldGetValue() throws InterruptedException {
    final var value = Value.readWrite(0);

    final var pool = Executors.newFixedThreadPool(5);

    final var threads = List.of(
      () -> value.put(value.tryGet() + 1),
      () -> value.put(value.tryGet() + 2),
      () -> value.put(value.tryGet() + 3),
      () -> value.put(value.tryGet() + 4),
      () -> value.put(value.tryGet() + 5),
      () -> value.put(value.tryGet() + 6),
      () -> value.put(value.tryGet() + 7),
      () -> value.put(value.tryGet() + 8),
      () -> value.put(value.tryGet() + 9),
      (Callable<Value<Integer>>) () -> value.put(value.tryGet() + 10)
    );

    pool.invokeAll(threads);
    pool.awaitTermination(5, SECONDS);

    assertThat(value).isEqualTo(55);
  }
}
