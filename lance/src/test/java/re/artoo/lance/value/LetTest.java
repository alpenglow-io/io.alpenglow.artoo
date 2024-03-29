package re.artoo.lance.value;

import org.junit.jupiter.api.Test;
import re.artoo.lance.experimental.value.Let;

import static org.assertj.core.api.Assertions.assertThat;

public class LetTest {
  @Test
  public void shouldBeLazySet() {
    enum Dummy {
      $;
      int value = 0;
    }

    final var dummy = Dummy.$;

    final var lazy = Let.lazy(() -> dummy.value++);

    lazy.get(it -> assertThat(it).isEqualTo(0));
    assertThat(dummy.value).isEqualTo(1);
    lazy.get(it -> assertThat(it).isEqualTo(0));
  }
}
