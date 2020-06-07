package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.value.Int32;
import io.artoo.lance.value.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

class UniquableTest {
  @Test
  @DisplayName("should get element at index 4 or default if out of bound")
  void shouldGetElementAt4() {
    final String[] names = {
      "Hartono, Tommy",
      "Adams, Terry",
      "Andersen, Henriette Thaulow",
      "Hedlund, Magnus",
      "Ito, Shu"
    };

    assertThat(from(names).at(4)).isEqualTo(One.from("Ito, Shu"));

    assertThat(from(names).at(-1).or(Text.Empty)).isEqualTo(Text.Empty);
  }

  @Test
  @DisplayName("should get first element or default if none")
  void shouldGetFirstOrDefaultIfNone() {

    for (final var value : Many.from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first())
      assertThat(value.eval()).isEqualTo(9);

    for (final var value : from((Integer) null).first().or(Int32.ZERO))
      assertThat(value.eval()).isEqualTo(Int32.ZERO);
  }

  @Test
  @DisplayName("should get first even number or default if none")
  void shouldGetFirstEvenNumberOrDefaultIfNone() {

    for (final var value : Many.from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first(number -> number.eval() % 2 == 0))
      assertThat(value).isEqualTo(34);
    for (final var value : Many.from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19).first(number -> number.eval() % 2 == 0).or(Int32.ZERO))
      assertThat(value).isEqualTo(Int32.ZERO);
  }

  @Test
  @DisplayName("should get last element or default if none")
  void shouldGetLastOrDefaultIfNone() {
    final var last = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).last();

    for (final var value : last) assertThat(value.eval()).isEqualTo(19);

    final var otherwise = from((Integer) null).last().or(Int32.ZERO);

    for (final var value : otherwise) assertThat(value).isEqualTo(Int32.ZERO);
  }

  @Test
  @DisplayName("should get last even number or default if none")
  void shouldGetLastEvenNumberOrDefaultIfNone() {

    final var last = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).last(number -> number.eval() % 2 == 0);
    for (final var value : last)
      assertThat(value.eval()).isEqualTo(12);

    for (final var value : Many.from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19).last(number -> number.eval() % 2 == 0).or(Int32.ZERO))
      assertThat(value).isEqualTo(Int32.ZERO);
  }

  @Test
  @DisplayName("should get single element")
  void shouldGetSingle() {

    for (final var value : Many.from(9).single())
      assertThat(value.eval()).isEqualTo(9);

    for (final var value : Many.from(9, 34, 65, 87, 435, 3, 83, 23).single(number -> number.eval() % 2 == 0))
      assertThat(value.eval()).isEqualTo(34);
  }

  @Test
  @DisplayName("should fail if there's more than single element")
  void shouldFailIfThereIsMoreThanSingleElement() {
    final var many = Many.from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19);

    assertThat(many.single()).isEmpty();

    assertThat(many.single(number -> number.eval() < 20)).isEmpty();
  }

  @Test
  @DisplayName("should get negative number since it fails")
  void shouldGetNegativeNumberSinceItFails() {
    final var many = Many.from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19);

    for (final var value : many.single().or(Int32.ZERO))
      assertThat(value).isEqualTo(Int32.ZERO);

    for (final var value : many.single(number -> number.eval() < 20).or(Int32.ZERO))
      assertThat(value).isEqualTo(Int32.ZERO);
  }
}
