package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;

import static org.assertj.core.api.Assertions.assertThat;


class FilterTest {
  @Test
  @DisplayName("should filter elements from cursor")
  void shouldFilterElements() throws Throwable {
    Cursor<Integer> cursor = Cursor.open(1, 2, 3, 4).filter(element -> element < 3);

    assertThat(cursor.fetch()).isEqualTo(1);
    assertThat(cursor.fetch()).isEqualTo(2);
    assertThat(cursor.canFetch()).isFalse();
  }

  @Test
  void shouldFilterWordsWithLength3() throws Throwable {
    Cursor<String> cursor = Cursor.open("the", "quick", "brown", "fox", "jumps", null).filter(it -> it.length() == 3);

    assertThat(cursor.fetch()).isEqualTo("the");
    assertThat(cursor.fetch()).isEqualTo("fox");
    assertThat(cursor.canFetch()).isFalse();
  }
}
