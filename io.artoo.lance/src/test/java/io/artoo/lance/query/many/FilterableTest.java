package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.value.Int32;
import io.artoo.lance.value.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.artoo.lance.query.Many.from;
import static io.artoo.lance.value.Text.let;
import static org.assertj.core.api.Assertions.assertThat;

class FilterableTest {
  @Test
  @DisplayName("should filter words with length 3")
  void shouldFilterWordsWithLength3() {
    final var where = from("the", "quick", "brown", "fox", "jumps").where(word -> word.length() == 3);

    assertThat(where).contains(let("the"), let("fox"));
  }

  @Test
  @DisplayName("should filter words with length less than 6")
  void shouldFilterWordsWithLengthLessThan6() {

    final var where = from("apple", "passionfruit", "banana", "mango", "orange", "blueberry", "grape", "strawberry").where(fruit -> fruit.length() < 6);

    assertThat(where).contains(let("apple"), let("mango"), let("grape"));
  }

  @Test
  @DisplayName("should get all numbers that are less than or equal to index by 10")
  void shouldGetNumbersLessOrEqualThanIndexBy10() {

    final var where = Many.from(0, 30, 20, 15, 90, 85, 40, 75).where((index, number) -> number.eval() <= index * 10);

    assertThat(where).contains(Int32.let(0), Int32.let(20), Int32.let(15), Int32.let(40));
  }

  @Test
  @DisplayName("should get all strings from a generic array")
  void shouldGetAllStrings() {
    final Object[] objects = {"apple", "passionfruit", 10.2F, 12L, "banana", LocalTime.now(), LocalDateTime.now(), 2};

    final var texts = Many.fromAny(objects).ofType(Text.class);

    assertThat(texts).contains(let("apple"), let("passionfruit"), let("banana"));
  }
}
