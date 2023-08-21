package re.artoo.lance.value;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import re.artoo.lance.experimental.value.Late;

import static org.assertj.core.api.Assertions.assertThat;

public class LateTest {
  @Disabled
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
