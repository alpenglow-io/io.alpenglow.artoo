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
    final var value = Value.<Integer>late();

    final var pool = Executors.newFixedThreadPool(5);

    final var threads = List.of(
      () -> value.set(value.tryGet() + 1),
      () -> value.set(value.tryGet() + 2),
      () -> value.set(value.tryGet() + 3),
      () -> value.set(value.tryGet() + 4),
      () -> value.set(value.tryGet() + 5),
      () -> value.set(value.tryGet() + 6),
      () -> value.set(value.tryGet() + 7),
      () -> value.set(value.tryGet() + 8),
      () -> value.set(value.tryGet() + 9),
      (Callable<Value<Integer>>) () -> value.set(value.tryGet() + 10)
    );

    pool.invokeAll(threads);
    pool.awaitTermination(10, SECONDS);

    assertThat(value).isEqualTo(55);
  }
}
