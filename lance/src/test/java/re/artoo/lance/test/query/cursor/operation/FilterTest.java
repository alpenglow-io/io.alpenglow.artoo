package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.Open;
import re.artoo.lance.query.cursor.operation.Filter;
import re.artoo.lance.query.cursor.operation.PresenceOnly;

import static org.assertj.core.api.Assertions.assertThat;


class FilterTest {
  @Test
  @DisplayName("should filter elements")
  void shouldFilterElements() throws Throwable {
    var filter =
      new Filter<>(
        new Open<>("Luke", null, "I'm", "your", null, "father", null, null, null, "fool"),
        (index, element) -> element != null && element.length() == 4
      );

    assertThat(filter.fetch()).isEqualTo("Luke");
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo("your");
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo("fool");
    assertThat(filter.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should filter with present-only elements")
  void shouldFilterWithPresentOnlyElements() throws Throwable {
    var filter =
      new Filter<>(
        new PresenceOnly<>(
          new Open<>("Luke", null, "I'm", "your", null, "father", null, null, null, "fool") // all null elements will be ignored by the presence-only cursor
        ),
        (index, element) -> element.length() == 4
      );

    assertThat(filter.fetch()).isEqualTo("Luke");
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo("your");
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo("fool");
    assertThat(filter.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should filter numbers less than operation with index")
  void shouldFilterNumberLessThanOperation() throws Throwable {
    var filter =
      new Filter<>(
        new PresenceOnly<>(
          new Open<>(0, null, 30, null, 20, null, 15, 90, 85, 40, 75)
        ),
        (index, number) -> number <= index * 10
      );

    assertThat(filter.fetch()).isEqualTo(0);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(20);
    assertThat(filter.fetch()).isEqualTo(15);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.fetch()).isEqualTo(40);
    assertThat(filter.fetch()).isEqualTo(null);
    assertThat(filter.canFetch()).isFalse();
  }
}
