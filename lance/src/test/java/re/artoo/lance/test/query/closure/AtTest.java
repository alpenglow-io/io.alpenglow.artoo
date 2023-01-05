package re.artoo.lance.test.query.closure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.closure.At;

import static org.assertj.core.api.Assertions.assertThat;

class AtTest {
  /**
   * By analysing this test, it seems quite obvious the harness of a mutable function:
   * although it may be understandable what the closure does, it may be not that gettable
   * why the result is inconsistent with the same element.
   * In order to be a maintainable function, this should be immutable and not for the
   * sake of immutability or pureness, but for a better and reliable result with no
   * gray feasibility.
   */
  @Test
  @DisplayName("should retrieve element at defined index")
  void shouldIndexAt() throws Throwable {
    var at = new At<Integer>(2);
    int[] ints = {1, 2, 3, 4};

    assertThat(at.invoke(ints[0])).isNull();
    assertThat(at.invoke(ints[1])).isNull();
    assertThat(at.invoke(ints[2])).isEqualTo(3);
    assertThat(at.invoke(ints[2])).isNull();
  }
}
