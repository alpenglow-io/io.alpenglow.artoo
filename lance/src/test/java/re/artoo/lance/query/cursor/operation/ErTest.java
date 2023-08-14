package re.artoo.lance.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErTest {
  @Test
  @DisplayName("should throw an error on fetching elements")
  void shouldThrowAnError() {
    var cursor = new Er<>(new Open<>(), "No elements found", IllegalStateException::new);

    assertThatThrownBy(cursor::next)
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("No elements found");
  }
}
