package io.artoo.lance.query.internal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SelectTest {
  @Test
  void shouldReturnMappedValueAndNewIndex() {
    record Element(String value) {}

    final var select = new Select.Indexed<Element, String>((index, element) -> element.value + " world");

    final var actual = select.apply(new Element("Hello"));
    assertThat(actual.func()).isNotEqualTo(select);
    assertThat(actual.returning()).isEqualTo("Hello world");
  }
}
