package oak.collect.query.aggregate;

import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.from;
import static org.assertj.core.api.Assertions.assertThat;

class ExpressionTest {
  @Test
  void shouldAccumulateWithExpression() {
    final var expression = new Expression<>(
      from(1, 2, 3, 4),
      0,
      it -> it % 2 == 0,
      (acc, it) -> acc + it
    );

    assertThat(expression.get()).isEqualTo(6);
  }
}
