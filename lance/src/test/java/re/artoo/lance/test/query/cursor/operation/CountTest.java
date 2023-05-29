package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Empty;
import re.artoo.lance.query.cursor.operation.Filter;
import re.artoo.lance.query.cursor.operation.Fold;
import re.artoo.lance.query.cursor.operation.Open;

import static org.assertj.core.api.Assertions.assertThat;
import static re.artoo.lance.query.cursor.operation.Filter.presenceOnly;

class CountTest {
  @Test
  @DisplayName("should count elements")
  void shouldCount() {
    var cursor =
      new Fold<>(
        new Filter<>(
          new Open<>(1, null, 2, null, 3, null, 4, null),
          presenceOnly()
        ),
        0,
        (index, count, element) -> count + 1
      );

    assertThat(cursor.next()).isEqualTo(4);
    assertThat(cursor.hasNext()).isFalse();
  }

  @Test
  void shouldBeDefaultString() {
    var cursor = new Fold<>(Empty.Default, "default", (index, folded, element) -> folded + 1);

    assertThat(cursor.next()).isEqualTo("default");
    assertThat(cursor.hasNext()).isFalse();
  }
}
