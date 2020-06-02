package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.artoo.lance.query.Many.from;
import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;

class PartitionableTest {
  @Test
  @DisplayName("should take first 3 rows")
  void shouldTake3Rows() {
    final var taken = from(59, 82, 70, 56, 92, 98, 85).take(3);

    assertThat(taken).isEqualTo(Many.from(59, 82, 70));
  }

  @Test
  @DisplayName("should take all fruits until orange is met")
  void shouldTakeAllUntilOrange() {
    final var taken = from("apple", "banana", "mango", "orange", "passionfruit", "grape").takeWhile(not(text -> text.is("orange")));

    assertThat(taken).isEqualTo(Many.from("apple", "banana", "mango"));
  }

  @Test
  @DisplayName("should take all fruits until fruit.length is greater than index")
  void shouldTakeWithIndex() {
    final var query = from("apple", "passionfruit", "banana", "mango", "orange", "blueberry", "grape", "strawberry").takeWhile((index, fruit) -> fruit.length() >= index);

    assertThat(query).isEqualTo(Many.from("apple", "passionfruit", "banana", "mango", "orange", "blueberry"));
  }

  @Test
  @DisplayName("should skip first three numbers")
  void shouldSkipFirst3() {
    final var skipped = Many.from(59, 82, 70, 56, 92, 98, 85).skip(3);

    assertThat(skipped).isEqualTo(Many.from(56, 92, 98, 85));
  }

  @Test
  @DisplayName("should skip while number is lesser or equal than 90")
  void shouldSkipWhileNumbersLesserOrEqualThan90() {
    final var query = Many.from(59, 82, 70, 56, 92, 98, 85).skipWhile(number -> number.eval() <= 90);

    assertThat(query).isEqualTo(Many.from(92, 98, 85));
  }

  @Test
  @DisplayName("should skip while amount is greater than the index by 1000")
  void shouldSkipWhileAmountGreaterThanIndexBy1000() {
    final var query = Many.from(5000, 2500, 9000, 8000, 6500, 4000, 1500, 5500).skipWhile((index, amount) -> amount.eval() > index * 1000);

    assertThat(query).isEqualTo(Many.from(4000, 1500, 5500));
  }
}
