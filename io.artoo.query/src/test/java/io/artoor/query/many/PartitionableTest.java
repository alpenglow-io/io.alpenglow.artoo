package io.artoo.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.artoo.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

class PartitionableTest {
  @Test
  @DisplayName("should take first 3 rows")
  void shouldTake3Rows() {
    final var query = from(59, 82, 70, 56, 92, 98, 85).take(3);

    assertThat(query).containsExactly(59, 82, 70);
  }

  @Test
  @DisplayName("should take all fruits until orange is met")
  void shouldTakeAllUntilOrange() {
    final var query = from("apple", "banana", "mango", "orange", "passionfruit", "grape").takeWhile(not("orange"::equals));

    assertThat(query).containsExactly("apple", "banana", "mango");
  }

  @Test
  @DisplayName("should take all fruits until fruit.length is greater than index")
  void shouldTakeWithIndex() {
    final var fruits = new String[]{
      "apple", "passionfruit", "banana", "mango",
      "orange", "blueberry", "grape", "strawberry"
    };
    final var query = from(fruits).takeWhile((index, fruit) -> fruit.length() >= index);

    assertThat(query).containsExactly("apple", "passionfruit", "banana", "mango", "orange", "blueberry");
  }

  @Test
  @DisplayName("should skip first three numbers")
  void shouldSkipFirst3() {
    final var numbers = new Integer[]{59, 82, 70, 56, 92, 98, 85};
    final var query = from(numbers).skip(3);

    assertThat(query).containsExactly(56, 92, 98, 85);
  }

  @Test
  @DisplayName("should skip while number is lesser or equal than 90")
  void shouldSkipWhileNumbersLesserOrEqualThan90() {
    final var numbers = new Integer[]{59, 82, 70, 56, 92, 98, 85};
    final var query = from(numbers).skipWhile(number -> number <= 90);

    assertThat(query).containsExactly(92, 98, 85);
  }

  @Test
  @DisplayName("should skip while amount is greater than the index by 1000")
  void shouldSkipWhileAmountGreaterThanIndexBy1000() {
    final var amounts = new Integer[]{5000, 2500, 9000, 8000, 6500, 4000, 1500, 5500};
    final var query = from(amounts).skipWhile((index, amount) -> amount > index * 1000);

    assertThat(query).containsExactly(4000, 1500, 5500);
  }
}
