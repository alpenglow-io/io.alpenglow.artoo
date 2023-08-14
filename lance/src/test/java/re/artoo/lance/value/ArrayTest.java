package re.artoo.lance.value;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class ArrayTest {
  @Test
  void shouldConcatElements() {
    var list1 = new ArrayList<Integer>();
    for (int index = 0; index < 200_000; index++) {
      list1.add(index);
    }
    var list2 = new ArrayList<Integer>();
    for (int index = 200_000; index < 400_000; index++) {
      list2.add(index);
    }
    var array = Array.of(list1.toArray(Integer[]::new));

    var concated = array.concat(list2.toArray(Integer[]::new));
    assertThat(concated).endsWith(399_999);
    assertThat(concated.map(it -> it * 2)).endsWith(399_999 * 2);
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
