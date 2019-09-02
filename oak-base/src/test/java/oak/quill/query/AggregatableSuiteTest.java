package oak.quill.query;

import org.junit.jupiter.api.Test;

import static oak.func.fun.Function1.identity;
import static oak.quill.Quill.from;
import static org.assertj.core.api.Assertions.assertThat;

class AggregatableSuiteTest {
  @Test
  void shouldFindMax() {
    assertThat(from(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).max()).containsOnly(10);
    assertThat(from(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).min()).containsOnly(1);
    assertThat(from(1, 1, 1, 1, 1, 1).sum(identity())).containsOnly(6);
  }
}
