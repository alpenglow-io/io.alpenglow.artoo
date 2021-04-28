package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountableTest {
  @Test
  @DisplayName("should count 3 on three elements")
  public void shouldCount() {
    final var count = Many.from(1, 2, 3).count().otherwise(-1);

    assertThat(count).isEqualTo(3);
  }

  @Test
  @DisplayName("should count 0 on none elements")
  public void shouldCountNone() {
    final var count = Many.empty().count().otherwise(-1);

    assertThat(count).isEqualTo(0);
  }

  @Test
  @DisplayName("should count 2 on even elements")
  public void shouldCountEven() {
    assertThat(Many.from(1, 2, 3, 4).count(it -> it % 2 == 0).otherwise(-1)).isEqualTo(2);
  }

  @Test
  @DisplayName("should count non nullable elements only")
  public void shouldCountNonNullableOnly() {
    final var count = Many.from(1, 2, null, "hi there", true, null).count().otherwise(-1);

    assertThat(count).isEqualTo(4);
  }
}
