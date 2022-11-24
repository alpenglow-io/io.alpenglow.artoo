package lance.test.scope;

import lance.scope.Late;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LateTest {
  @Test
  public void shouldSetAValueOnce() {
    final var late = Late.<String>init();

    late.set(() -> "A Value");
    late.set(() -> "New Value");

    late.get(it ->
      assertThat(it)
        .isNotEqualTo("New Value")
        .isEqualTo("A Value")
    );
  }
}
