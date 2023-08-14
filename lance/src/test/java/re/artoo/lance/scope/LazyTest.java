package re.artoo.lance.scope;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import re.artoo.lance.func.TrySupplier1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class LazyTest {
  private final TrySupplier1<String> initialization = (TrySupplier1<String>) mock(TrySupplier1.class);

  @Test
  void shouldInitializatedLater() {
    when(initialization.get()).thenReturn("Hello World");

    final var lazy = Lazy.value(initialization);

    assertThat(lazy.value()).isEqualTo("Hello World");

    verify(initialization, atMostOnce()).get();
  }
}
