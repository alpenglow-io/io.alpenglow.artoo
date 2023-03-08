package re.artoo.lance.test.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.One;

import static org.assertj.core.api.Assertions.assertThat;

public class CountableTest {
  @Test
  @DisplayName("should count 3 on three elements")
  public void shouldCount() {
    final var count = Many.from(1, 2, 3).count();

    assertThat(count).containsExactly(3);
  }

  @Test
  @DisplayName("should count 0 on none elements")
  public void shouldCountNone() {
    final var count = Many.empty().count();

    assertThat(count).containsExactly(0);
  }

  @Test
  @DisplayName("should count 2 on even elements")
  public void shouldCountEven() {
    final var count = Many.from(1, 2, 3, 4).count(it -> it % 2 == 0);

    assertThat(count).containsExactly(2);
  }

  @Test
  @DisplayName("should count non nullable elements only")
  public void shouldCountNonNullableOnly() {
    final var count = Many.from(1, 2, null, "hi there", true, null)
      .coalesce()
      .count();

    assertThat(count).containsExactly(4);
  }
}
