package lance.test.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static lance.query.Many.from;
import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;

public class UniquableTest {
  @Test
  @DisplayName("should get element at index 4 or default if out pseudo bound")
  public void shouldGetElementAt4() {
    final String[] names = {
      "Hartono, Tommy",
      "Adams, Terry",
      "Andersen, Henriette Thaulow",
      "Hedlund, Magnus",
      "Ito, Shu"
    };

    assertThat(from(names).at(4).otherwise("none")).isEqualTo("Ito, Shu");
  }

  @Test
  @DisplayName("should get first element")
  public void shouldGetFirst() {
    final var first = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first().otherwise(-1);

    assertThat(first).isEqualTo(9);
  }

  @Test
  @DisplayName("should get first even number")
  public void shouldGetFirstEvenNumber() {
    final var first = from(9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19).first(number -> number % 2 == 0).otherwise(-1);

    assertThat(first).isEqualTo(34);
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
    final var singled = from(9).single().otherwise(-1);

    assertThat(singled).isEqualTo(9);
  }

  @Test
  @DisplayName("should find a single element according to condition")
  public void shouldFindSingleByCondition() {
    final var singleEven = from(9, 34, 65, 87, 435, 3, 83, 23).single(number -> number % 2 == 0).otherwise(-1);

    assertThat(singleEven).isEqualTo(34);
  }

  @Test
  @DisplayName("should be empty if there's more than single element")
  public void shouldEmptyIfThereIsMoreThanSingleElement() {
    final var single = from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19)
      .single()
      .peek(out::println);

    assertThat(single).isEmpty();

    //assertThat(many.single(number -> number < 20)).isEmpty();
  }

  @Test
  @DisplayName("should be empty if there's more than single element on condition")
  public void shouldEmptyIfThereIsMoreThanSingleElementOnCondition() {
    final var single = from(9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19)
      .single(number -> number < 20);

    for (var it : single)
      out.println(it);

    assertThat(single).isEmpty();
  }

  @Test
  public void shouldBeFirst() {
    final var first = from(1, 23.4, 'A', "Hi there", 5)
      .ofType(String.class)
      .peek(out::println)
      .select(it -> it.toUpperCase())
      .peek(out::println)
      .last()
      .otherwise("none");

    assertThat(first).isEqualTo("HI THERE");
  }
}
