package oak.quill.query;

import oak.func.pre.WithLongAnd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oak.func.pre.Predicate1.not;
import static oak.quill.Quill.from;
import static oak.type.Str.str;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
  void shouldTakeWithIndex() {
    final var fruits = new String[] {
      "apple", "passionfruit", "banana", "mango",
      "orange", "blueberry", "grape", "strawberry"
    };
    final var query = from(fruits).takeWhile((index, fruit) -> fruit.length() >= index);

    assertThat(query).containsExactly("apple", "passionfruit", "banana", "mango", "orange", "blueberry");
  }
}
