package oak.quill.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static oak.quill.Quill.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class UniquableSuiteTest {
  private final Queryable<Integer> integers = from(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

  @Test
  void shouldRetrieveFirstElement() {
    assertThat(integers.first()).contains(1);
  }

  @Test
  void shouldRetrieveNone() {
    assertThat(from().first()).isEmpty();
  }

  @Test
  void shouldBe() {
    assertThat(integers.first(it -> it % 2 == 0)).containsOnly(2);
  }
}
