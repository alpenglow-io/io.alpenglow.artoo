package io.artoo.lance.query.many;

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

    assertThat(from(names).at(4).yield()).isEqualTo("Ito, Shu");
  }

  @Test
  @DisplayName("should get first element")
  void shouldGetFirst() {
    final var first = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first().yield();

    assertThat(first).isEqualTo(9);
  }

  @Test
  @DisplayName("should get first even number")
  void shouldGetFirstEvenNumber() {
    final var first = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first(number -> number % 2 == 0).yield();

    assertThat(first).isEqualTo(34);
  }

  @Test
  @DisplayName("should get last element")
  void shouldGetLast() {
    final var last = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).last().yield();

    assertThat(last).isEqualTo(19);
  }

  @Test
  @DisplayName("should get last even number")
  void shouldGetLastEvenNumber() {

    final var last = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).last(number -> number % 2 == 0).yield();

    assertThat(last).isEqualTo(12);
  }

  @Test
  @DisplayName("should find a single element only")
  void shouldFindASingleElementOnly() {
    final var singleElement = from(9).single().yield();
    assertThat(singleElement).isEqualTo(9);

    final var singleEven = from(9, 34, 65, 87, 435, 3, 83, 23).single(number -> number % 2 == 0).yield();
    assertThat(singleEven).isEqualTo(34);
  }

  @Test
  @DisplayName("should be empty if there's more than single element")
  void shouldEmptyIfThereIsMoreThanSingleElement() {
    final var many = from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19);

    assertThat(many.single()).isEmpty();

    assertThat(many.single(number -> number < 20)).isEmpty();
  }
}
