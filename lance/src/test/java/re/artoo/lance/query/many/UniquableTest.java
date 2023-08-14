package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.cursor.Fetch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static re.artoo.lance.query.Many.from;

public class UniquableTest {
  @Test
  @DisplayName("should get element at index 4 or default if out pseudo bound")
  public void shouldGetElementAt4() throws Throwable {
    final String[] names = {
      "Hartono, Tommy",
      "Adams, Terry",
      "Andersen, Henriette Thaulow",
      "Hedlund, Magnus",
      "Ito, Shu"
    };

    assertThat(Many.from(names).at(4)).containsExactly("Ito, Shu");
  }

  @Test
  @DisplayName("should get first element")
  public void shouldGetFirst() throws Throwable {
    final var first = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first();

    assertThat(first).containsOnly(9);
  }

  @Test
  @DisplayName("should get first even number")
  public void shouldGetFirstEvenNumber() {
    final var first = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first(number -> number % 2 == 0);

    assertThat(first).containsOnly(34);
  }

  @Test
  @DisplayName("should get last element")
  public void shouldGetLast() {
    final var last = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).last().otherwise(-1);

    assertThat(last).isEqualTo(19);
  }

  @Test
  @DisplayName("should get last even number")
  public void shouldGetLastEvenNumber() {

    final var last = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).last(number -> number % 2 == 0).otherwise(-1);

    assertThat(last).isEqualTo(12);
  }

  @Test
  @DisplayName("should find a single element only")
  public void shouldFindASingleElementOnly() {
    final var singled = from(9).single();

    assertThat(singled).containsOnly(9);
  }

  @Test
  @DisplayName("should find a single element according to condition")
  public void shouldFindSingleByCondition() {
    final var singleEven = from(9, 34, 65, 87, 435, 3, 83, 23).single(number -> number % 2 == 0);

    assertThat(singleEven).containsOnly(34);
  }

  @Test
  @DisplayName("should throw fetch-exception on more than one single element has been found")
  public void shouldThrowIfThereIsMoreThanSingleElementOnCondition() {
    assertThrows(
      Fetch.Exception.class,
      () -> from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19).single(number -> number < 20).cursor().next()
    );

    assertThrows(
      Fetch.Exception.class,
      () -> from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19).single().cursor().next()
    );
  }

  @Test
  public void shouldBeLast() throws Throwable {
    final var first = from(1, 23.4, 'A', "Hi there", 5)
      .ofType(String.class)
      .select(it -> it.toUpperCase())
      .last();

    assertThat(first).containsOnly("HI THERE");
  }
}
