package re.artoo.lance.test.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
class CursorTest {
  @Test
  @DisplayName("should condition an old value to new one")
  void shouldMapAnOldValueToNewOne() throws Throwable {
    record Old(int value) {}
    record New(String value) {}

    var fetched = Cursor
      .open(new Old(314))
      .map(it -> new New("" + it.value))
      .next();

    assertThat(fetched).isEqualTo(new New("314"));
  }

  @Test
  @DisplayName("should condition an old value to new one")
  void shouldMapAnOldValueToNull() throws Throwable {
    record Old(int value) {}

    var fetched = Cursor
      .open(new Old(314))
      .map(it -> null)
      .next();

    assertThat(fetched).isNull();
  }
}
