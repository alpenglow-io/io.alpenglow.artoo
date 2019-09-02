package oak.type;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static oak.quill.single.Nullable.of;
import static org.assertj.core.api.Assertions.assertThat;

class AnyTest {
  @Test
  @Disabled
  void shouldFilter() {
    class Value implements Any<String> {
      @Override
      public String get() { return "value"; }
    }

    final var value = new Value();

    assertThat(value.filter(it -> it.equals("value"))).isEqualTo(of(value));
  }
}
