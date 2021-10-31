package io.artoo.lance.test.query.internal;

import io.artoo.lance.query.func.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SelectTest {
  @Test
  @DisplayName("should return mapped value and new index")
  void shouldReturnMappedValueAndNewIndex() {
    final var select = new Select.Indexed<String, String>((index, value) -> "%s %d".formatted(value, index + 1));

    final var applied1 = select.apply("Hello");
    final var applied2 = applied1.func().apply(applied1.returning());

    assertThat(applied1.returning()).isEqualTo("Hello 1");
    assertThat(applied2.returning()).isEqualTo("Hello 1 2");
  }
}
