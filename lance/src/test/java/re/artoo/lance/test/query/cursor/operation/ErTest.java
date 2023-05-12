package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Er;
import re.artoo.lance.query.cursor.operation.Open;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErTest {
  @Test
  @DisplayName("should fetch elements with an error")
  void shouldFetchWithError() {
    var cursor = new Er<>(new Open<>(), "No elements found", IllegalStateException::new);

    assertThatThrownBy(cursor::next)
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("No elements found");
  }
}
