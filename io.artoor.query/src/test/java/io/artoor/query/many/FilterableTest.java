package io.artoor.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.artoor.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

class FilterableTest {
  @Test
  @DisplayName("should filter words with length 3")
  void shouldFilterWordsWithLength3() {
    final var query = from("the", "quick", "brown", "fox", "jumps").where(word -> word.length() == 3);

    assertThat(query).containsOnly("the", "fox");
  }

  @Test
  @DisplayName("should filter words with length less than 6")
  void shouldFilterWordsWithLengthLessThan6() {
    final String[] fruits = {"apple", "passionfruit", "banana", "mango", "orange", "blueberry", "grape", "strawberry"};

    final var query = from(fruits).where(fruit -> fruit.length() < 6);

    assertThat(query).containsOnly("apple", "mango", "grape");
  }

  @Test
  @DisplayName("should get all numbers that are less than or equal to index by 10")
  void shouldGetNumbersLessOrEqualThanIndexBy10() {
    final Integer[] numbers = {0, 30, 20, 15, 90, 85, 40, 75};

    final var query = from(numbers).where((index, number) -> number <= index * 10);

    assertThat(query).containsOnly(0, 20, 15, 40);
  }

  @Test
  @DisplayName("should get all strings from a generic array")
  void shouldGetAllStrings() {
    final Object[] objects = {"apple", "passionfruit", 10.2F, 12L, "banana", LocalTime.now(), LocalDateTime.now(), 2};

    final var query = from(objects).ofType(String.class);

    assertThat(query).containsOnly("apple", "passionfruit", "banana");
  }
}
