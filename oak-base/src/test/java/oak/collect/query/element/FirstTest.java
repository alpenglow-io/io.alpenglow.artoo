package oak.collect.query.element;

import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.Q;
import static org.assertj.core.api.Assertions.assertThat;

class FirstTest {
  @Test
  void shouldRetrieveFirstElement() {
    assertThat(Q(1, 2, 3).first()).contains(1);
  }
}
