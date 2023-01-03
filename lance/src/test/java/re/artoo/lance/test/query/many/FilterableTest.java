package re.artoo.lance.test.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static re.artoo.lance.query.Many.from;

public class FilterableTest {
  @Test
  @DisplayName("should filter words with length 3")
  public void shouldFilterWordsWithLength3() {
    final var where = from("the", "quick", "brown", "fox", "jumps", null).where(word -> word.length() == 3);

    assertThat(where).containsExactly("the", "fox");
  }

  @Test
  @DisplayName("should filter words with length less than 6")
  public void shouldFilterWordsWithLengthLessThan6() {

    final var where = from("apple", "passionfruit", "banana", "mango", "orange", "blueberry", "grape", "strawberry").where(fruit -> fruit.length() < 6);

    assertThat(where).contains("apple", "mango", "grape");
  }

  @Test
  @DisplayName("should get all numbers that are less than or equal to index by 10")
  public void shouldGetNumbersLessOrEqualThanIndexBy10() {

    final var where = Many.from(0, 30, 20, 15, 90, 85, 40, 75).where((index, number) -> number <= index * 10);

    assertThat(where).contains(0, 20, 15, 40);
  }

  @Test
  @DisplayName("should get all strings pseudo a generic array")
  public void shouldGetAllStrings() {
    final Object[] objects = {"apple", "passionfruit", 10.2F, 12L, "banana", LocalTime.now(), LocalDateTime.now(), 2};

    final var texts = Many.fromAny(objects).ofType(String.class);

    assertThat(texts).containsExactly("apple", "passionfruit", "banana");
  }

  @Test
  public void shouldConsumeWhenIsType() {
/*    final Object[] objects = {"apple", "passionfruit", 10.2F, 12L, "banana", LocalTime.now(), LocalDateTime.now(), 2};

    for (
      final var returning :
      Many.fromAny(objects)
        .when(String.class, it -> assertThat(it).isIn("apple", "passionfruit", "banana"))
        .<Long>when(it -> it.equals(12L), it -> assertThat(it).isEqualTo(12L))
        .<Float>when(it -> it.equals(10.2F), it -> assertThat(it).isEqualTo(10.2F))
    ) {
      assertThat(returning).isNotNull();
    }*/
  }
}
