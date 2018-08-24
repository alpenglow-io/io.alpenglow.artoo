package oak.collect.query.aggregate;

import oak.collect.query.Queryable;
import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.asQueryable;
import static org.assertj.core.api.Java6Assertions.assertThat;

class SeedTest {
  @Test
  void shouldReduce() {
    final Queryable<Integer> array = asQueryable(1, 2, 3);
    final Seed<Integer, Integer> seed = new Seed<>(array, 0, (it, acc) -> it + acc);
    assertThat(seed).containsExactly(6);
  }
}
