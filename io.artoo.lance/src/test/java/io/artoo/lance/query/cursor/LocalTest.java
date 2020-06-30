package io.artoo.lance.query.cursor;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LocalTest {
  @Test
  void shouldIterateAllElements() {
    final Iterable<Integer> integers = () -> Cursor.local(1, 2, 3, 4);

    assertThat(integers).containsExactly(1, 2, 3, 4);
  }

  @Test
  void shouldNotIterateOnEmpty() {
    final Iterable<Integer> integers = Cursor::local;

    assertThat(integers).isEmpty();
  }

  @Test
  void shouldReplaceNext() {
    assertThat(Cursor.local(1).next(2).next()).isEqualTo(2);
  }

  @Test
  void shouldReplaceMoreNext() {
    assertThat(Cursor.local(1, 2, 3).next(4, 5, 6).next()).isEqualTo(4);
  }

  @Test
  void shouldReplaceCause() {
    final var cause = new IllegalAccessException();

    assertThat(Cursor.local().cause(cause).cause()).isEqualTo(cause);
  }

  @Test
  @DisplayName("should append a new element")
  void shouldAppendNext() {
    final var cursor = Cursor.local(1, 2, 3).append(4).append(5).append(6);

    final Iterable<Integer> iterable = () -> cursor;

    assertThat(iterable).containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  @Disabled
  void shouldIterateOnBigList() {
    List<Integer> list = new ArrayList<>();
    var bound = Short.MAX_VALUE * Byte.MAX_VALUE * Byte.MAX_VALUE;
    for (var it = 0; it < bound; it++) {
      var i = it * 2;
      Integer integer = i;
      list.add(integer);
    }

    final Iterable<Integer> integers = () -> Cursor.local(list.toArray(new Integer[]{}));

    assertThat(integers).isNotEmpty();
  }
}
