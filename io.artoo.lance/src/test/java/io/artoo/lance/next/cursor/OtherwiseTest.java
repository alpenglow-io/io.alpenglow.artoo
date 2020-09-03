package io.artoo.lance.next.cursor;

import io.artoo.lance.next.Cursor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class OtherwiseTest {
  @Test
  @DisplayName("should retrieve another value when cursor has no next")
  void shouldRetrieveOrValue() {
    final var cursor = Cursor.<Integer>nothing().or(1);

    assertThat(cursor.next()).isEqualTo(1);
  }

  @Test
  @DisplayName("should not retrieve another value when cursor has next")
  void shouldNotRetriveOrValue() {
    final var cursor = Cursor.just(1).or(2);

    assertThat(cursor.next()).isEqualTo(1);
  }

  @Test
  @DisplayName("should retrieve another value when cursor has next as null")
  void shouldRetrieveOrValueWhenNull() {
    final var cursor = Cursor.just(null).or(1);

    assertThat(cursor.next()).isEqualTo(1);
  }

  @Test
  @DisplayName("should retrieve other values when cursor has nexts as nulls")
  void shouldRetrieveOrValuesWhenNulls() {
    final var cursor = Cursor.every(null, null, null).or(1, 2, 3);

    assertThat(cursor.next()).isEqualTo(1);
    assertThat(cursor.next()).isEqualTo(2);
    assertThat(cursor.next()).isEqualTo(3);
  }

  @Test
  @DisplayName("should not retrieve other values when cursor just has one next valid")
  void shouldNotRetrieveOrValuesWhenAtLeastOne() {
    final var cursor = Cursor.every(null, 4, null, null, null).or(1, 2, 3);

    assertThat(cursor.next()).isEqualTo(4);
    assertThat(cursor.hasNext()).isTrue();
    assertThat(cursor.next()).isEqualTo(null);
    assertThat(cursor.hasNext()).isFalse();
  }

  @Test
  @DisplayName("should raise an exception when cursor has no next")
  void shouldRaiseException() {
    final var cursor = Cursor.nothing().or("No values", IllegalStateException::new);

    assertThrows("No values", IllegalStateException.class, cursor::next);
    assertThrows("No values", IllegalStateException.class, cursor::fetch);
  }

  @Test
  @DisplayName("should not raise an exception when cursor has an or value")
  void shouldNotRaiseException() {
    final var cursor = Cursor.just(null).or("No values", IllegalStateException::new).or(1);

    assertThat(cursor.next()).isEqualTo(1);
  }
}
