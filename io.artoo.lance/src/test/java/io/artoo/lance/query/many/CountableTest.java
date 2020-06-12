package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.value.Int32;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountableTest {
  @Test
  @DisplayName("should count 3 on three elements")
  void shouldCount() {
    assertThat(Many.from(1, 2, 3).count().eval()).isEqualTo(3);
  }

  @Test
  @DisplayName("should count 0 on none elements")
  void shouldCountNone() {
    assertThat(Many.none().count().eval()).isEqualTo(0);
  }

  @Test
  @DisplayName("should count 2 on even elements")
  void shouldCountEven() {
    assertThat(Many.from(1, 2, 3, 4).count(Int32::isEven).eval()).isEqualTo(2);
  }
}
