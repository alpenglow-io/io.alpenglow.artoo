package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class AverageableTest {
  @Test
  @DisplayName("should reduce doubles and integers to same average")
  void shouldReduceDoublesAverage() {
    final var doublesAvg = Many.from(78.0, 92.0, 100.0, 37.0, 81.0).average().yield();
    final var integersAvg = Many.from(78, 92, 100, 37, 81).average().yield();

    final var expected = 77.6;
    assertThat(doublesAvg).isEqualTo(expected);
    assertThat(integersAvg).isEqualTo(expected);
  }

  @Test
  @DisplayName("should reduce to average even with nullables")
  void shouldReduceAverageWithNullables() {
    final var average = Many.fromAny(null, 10007L, 37L, 399846234235L).average().yield();

    assertThat(average).isEqualTo(133282081426.33333);
  }

  @Test
  @DisplayName("should reduce to average with a selector")
  void shouldReduceAverageWithSelector() {
    final var average = Many.from("apple", "banana", "mango", "orange", "passionfruit", "grape").average(String::length).yield();

    assertThat(average).isEqualTo(6.5);
  }

  @Test
  @DisplayName("should be null average since there's no numbers")
  void shouldBeNullSinceNoNumbers() {
    for (final var ignored : Many.from("apple", "banana", "mango", "orange", "passionfruit", "grape").average()) {
      fail();
    }
  }

  @Test
  void shouldFail() {
    final var cursor = Many.from("apple", null, "banana").average(this::tryCount).cursor();

    assertThat(cursor.hasCause()).isTrue();
    System.out.println(cursor.cause().getMessage());
  }

  private int tryCount(final String value) {
    if (value.endsWith("ana")) throw new IllegalArgumentException("Can't get length of Italian-like words");
    return value.length();
  }
}
