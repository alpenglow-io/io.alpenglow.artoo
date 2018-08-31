package oak.collect.query.aggregate;

import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.from;
import static org.assertj.core.api.Java6Assertions.assertThat;

class SeedTest {
  @Test
  void shouldAccumulateWithSeed() {
    final var sequence = from(1, 2, 3);
    final Seed<Integer, Integer> seed = new Seed<>(sequence, 0, (it, acc) -> it + acc);
    assertThat(seed).containsExactly(6);
  }
}
