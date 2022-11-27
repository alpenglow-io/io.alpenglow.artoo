package io.alpenglow.artoo.lance.test.query.many;

import io.alpenglow.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.alpenglow.artoo.lance.func.TryPredicate1.not;
import static io.alpenglow.artoo.lance.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

public class PartitionableTest {
  @Test
  @DisplayName("should take first 3 rows")
  public void shouldTake3Rows() {
    final var taken = from(59, 82, 70, 56, 92, 98, 85).take(3);

    assertThat(taken).containsExactly(59, 82, 70);
  }

  @Test
  @DisplayName("should take all fruits until orange is met")
  public void shouldTakeAllUntilOrange() {
    final var taken = from("apple", "banana", "mango", "orange", "passionfruit", "grape").takeWhile(not(text -> text.equals("orange")));

    assertThat(taken).containsExactly("apple", "banana", "mango");
  }

  @Test
  @DisplayName("should take all fruits until fruit.length is greater than index")
  public void shouldTakeWithIndex() {
    final var query = from("apple", "passionfruit", "banana", "mango", "orange", "blueberry", "grape", "strawberry").takeWhile((index, fruit) -> fruit.length() >= index);

    assertThat(query).containsExactly("apple", "passionfruit", "banana", "mango", "orange", "blueberry");
  }

  @Test
  @DisplayName("should skip first three numbers")
  public void shouldSkipFirst3() {
    final var skipped = Many.from(59, 82, 70, 56, 92, 98, 85).skip(3);

    assertThat(skipped).containsExactly(56, 92, 98, 85);
  }

  @Test
  @DisplayName("should skip while number is lesser or equal than 90")
  public void shouldSkipWhileNumbersLesserOrEqualThan90() {
    final var query = Many.from(59, 82, 70, 56, 92, 98, 85).skipWhile(number -> number <= 90);

    assertThat(query).containsExactly(92, 98, 85);
  }

  @Test
  @DisplayName("should skip while amount is greater than the index by 1000")
  public void shouldSkipWhileAmountGreaterThanIndexBy1000() {
    final var query = Many.from(5000, 2500, 9000, 8000, 6500, 4000, 1500, 5500).skipWhile((index, amount) -> amount > index * 1000);

    assertThat(query).containsExactly(4000, 1500, 5500);
  }
}
