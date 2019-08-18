package oak.collect.seq;

import org.junit.jupiter.api.Test;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LocalSequenceTest {
  @Test
  void shouldIterate() {
    final var sequence = new LocalSequence<Integer>();

    range(0, 40000).forEach(sequence::add);

    for (var item : sequence) System.out.println(item);

    assertThat(sequence.size()).isEqualTo(4000);
  }
}
