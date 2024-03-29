package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static re.artoo.lance.query.Many.from;

class FilterableTest {
  @Test
  @DisplayName("should filter words with length 3")
  void shouldFilterWordsWithLength3() {
    final var where = from("the", "quick", "brown", "fox", "jumps").where(word -> word.length() == 3);

    assertThat(where).containsExactly("the", "fox");
  }

  @Test
  @DisplayName("should filter words with length less than 6")
  void shouldFilterWordsWithLengthLessThan6() {

    final var where = from("apple", "passionfruit", "banana", "mango", "orange", "blueberry", "grape", "strawberry").where(fruit -> fruit.length() < 6);

    assertThat(where).containsExactly("apple", "mango", "grape");
  }

  @Test
  @DisplayName("should get all numbers that are less than or equal to index by 10")
  void shouldGetNumbersLessOrEqualThanIndexBy10() {

    final var where = Many.from(0, 30, 20, 15, 90, 85, 40, 75).where((index, number) -> number <= index * 10);

    assertThat(where).containsExactly(0, 20, 15, 40);
  }

  @Test
  @DisplayName("should get all strings pseudo a generic array")
  void shouldGetAllStrings() {
    final Object[] objects = {"apple", "passionfruit", 10.2F, 12L, "banana", LocalTime.now(), LocalDateTime.now(), 2};

    final var texts = Many.fromAny(objects).ofType(String.class);

    assertThat(texts).containsExactly("apple", "passionfruit", "banana");
  }

}
