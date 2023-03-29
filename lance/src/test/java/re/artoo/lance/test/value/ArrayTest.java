package re.artoo.lance.test.value;

import org.junit.jupiter.api.Test;
import re.artoo.lance.value.Array;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ArrayTest {
  @Test
  void shouldConcatElements() {
    var array = Array.of(1, 2, 3);

    assertThat(array.concat(4, 5, 6)).containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  void shouldMakeSomeArrayOperations() {
    var array = Array.of(1, 2, 3, 4);

    assertThat(array.join()).isEqualTo("1,2,3,4");
    assertThat(array.join('#')).isEqualTo("1#2#3#4");
    assertThat(array.join("Hello")).isEqualTo("1Hello2Hello3Hello4");
    assertThat(array.map(it -> it * 2)).containsOnly(2, 4, 6, 8);
    assertThat(array.flatMap(it -> Array.of(it, 8))).containsOnly(1, 8, 2, 8, 3, 8, 4, 8);
    assertThat(array.fold(1, Integer::sum)).isEqualTo(11);
    assertThat(array.reduce(Integer::sum).orElseThrow()).isEqualTo(10);
  }

  @Test
  void shouldHaveNoIssueWithBigSequence() {
    var array = Array.ofArray(IntStream.range(0, 30).boxed().toArray(Integer[]::new));
    var array1 = Array.ofArray(IntStream.range(0, 150).boxed().toArray(Integer[]::new));
    assertThat(array.flatMap(it -> array1).reduce(Integer::sum).orElseThrow()).isGreaterThan(1000);
  }
}
