package io.alpenglow.artoo.lance.test.query;

import io.alpenglow.artoo.lance.query.Cursor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CursorTest {
  @Test
  @DisplayName("should map an old value to new one")
  void shouldMapAnOldValueToNewOne() throws Throwable {
    record Old(int value) {}
    record New(String value) {}

    var fetched = Cursor
      .open(new Old(314))
      .map(it -> new New("" + it.value))
      .fetch();

    assertThat(fetched).isEqualTo(new New("314"));
  }

  @Test
  @DisplayName("should map an old value to new one")
  void shouldMapAnOldValueToNull() throws Throwable {
    record Old(int value) {}

    var fetched = Cursor
      .open(new Old(314))
      .map(it -> null)
      .fetch();

    assertThat(fetched).isNull();
  }
}
