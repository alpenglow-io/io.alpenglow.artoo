package re.artoo.lance.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.experimental.value.Lazy;
import re.artoo.lance.func.TrySupplier1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

class LazyTest {
  static final class Initialization implements TrySupplier1<String> {
    private int called = 0;

    @Override
    public String invoke() throws Throwable {
      try {
        return called > 0 ? "Hello World Again" : "Hello World";
      } finally {
        called++;
      }
    }
  }

  private final Initialization initialization = new Initialization();

  @Test
  @DisplayName("should be initialized once")
  void shouldBeInitializeOnce() {
    final var lazy = Lazy.value(initialization);

    assertThat(lazy)
      .matches(it -> it.value().equals("Hello World"))
      .matches(it -> it.value().equals("Hello World"));
    assertThat(initialization.called).isEqualTo(1);
  }
}
