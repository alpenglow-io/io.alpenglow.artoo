package io.alpenglow.artoo.lance.test.func.tail;

import io.alpenglow.artoo.lance.func.tail.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SelectTest {
  @Test
  @DisplayName("should return mapped value and new index")
  void shouldReturnMappedValueAndNewIndex() {
    final var select = Select.with((index, value) -> "%s %d".formatted(value, index + 1));

    final var applied1 = select.apply("Hello");
    final var applied2 = applied1
      .next().apply(applied1.result())
      .next().apply(applied1.result());

    assertThat(applied1.result()).isEqualTo("Hello 1");
    assertThat(applied2.result()).isEqualTo("Hello 1 2");
  }
}
