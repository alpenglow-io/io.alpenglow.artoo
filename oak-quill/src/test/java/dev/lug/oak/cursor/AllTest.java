package dev.lug.oak.cursor;

import oak.cursor.All;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

class AllTest {
  @Test
  void shouldHaveNext() {
    assertThat(new All<>(new Integer[]{1, 2, 3}).hasNext()).isTrue();
  }

  @Test
  void shouldRetrieveNext() {
    assertThat(new All<>(new Integer[]{1, 2, 3}).next()).isEqualTo(1);
  }

  @Test
  void shouldIterate() {
    final int size = 1000;
    final Integer[] expected = range(0, size)
      .boxed()
      .collect(toList())
      .toArray(new Integer[size]);

    final var forward = new All<>(expected);

    final Integer[] array = range(0, size)
      .mapToObj(it -> forward.next())
      .collect(toList())
      .toArray(new Integer[size]);

    assertThat(array).isEqualTo(expected);
  }
}
