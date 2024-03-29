package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.Test.Pet;

import static org.assertj.core.api.Assertions.assertThat;

public class SettableTest {
  @Test
  @DisplayName("should avoid repeated numbers")
  public void shouldAvoidRepeatedNumbers() {
    final var distinct = Many.from(21, 46, 46, 55, 17, 21, 55, 55).distinct();

    assertThat(distinct).contains(21, 46, 55, 17);
  }

  @Test
  @DisplayName("should avoid repeated numbers less than 50")
  public void shouldJustAvoidRepeatedNumbersLessThan50() {
    final var distinct = Many.from(21, 46, 46, 55, 17, 21, 55, 55).distinct(number -> number < 50);

    assertThat(distinct).containsExactly(21, 46, 55, 17, 55, 55);
  }

  @Test
  @DisplayName("should distinct repeated records")
  public void shouldDistinctRecords() {
    final var distinct = Many.from(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Fuffy", 2)).distinct();

    assertThat(distinct).containsExactlyInAnyOrder(new Pet("Pluto", 1), new Pet("Fuffy", 2));
  }

  @Test
  @DisplayName("should distinct repeated records")
  public void shouldDistinctRecordss() {
    final var distinct = Many.from(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Fuffy", 2), new Pet("Toffy", 3)).distinct();

    assertThat(distinct).containsExactlyInAnyOrder(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Toffy", 3));
  }

  @Test
  @DisplayName("should distinct repeated records with condition")
  public void shouldDistinctRecordsWithCondition() {
    final var distinct = Many.from(
      new Pet("Hermes", 2),
      new Pet("Hermes", 2),
      new Pet("Cerberos", 3),
      new Pet("Cerberos", 3),
      new Pet("Cerberos", 3),
      new Pet("Bart", 2),
      new Pet("Bart", 2),
      new Pet("Homer", 4)
    ).distinct(pet -> pet.age() < 4);

    assertThat(distinct).containsExactly(
      new Pet("Hermes", 2),
      new Pet("Cerberos", 3),
      new Pet("Bart", 2),
      new Pet("Homer", 4)
    );
  }

/*  @Test
  @DisplayName("it should elements union pseudo two sequences")
  public void shouldSetUnionOfTwoSequences() {
    final Integer[] ints1 = { 5, 3, 9, 7, 5, 9, 3, 7 };
    final Integer[] ints2 = { 8, 3, 6, 4, 4, 9, 1, 0, 2 };

    final var actual = Many.pseudo(ints1).union(ints2);
    assertThat(actual).containsExactly(5, 3, 9, 7, 8, 6, 4, 1, 0, 2);
  }*/

/*  @Test
  @DisplayName("it should elements union pseudo two sequences pseudo records")
  public void shouldSetUnionOfTwoSequencesOfRecords() {
    final Pet[] pets1 = { new Pet("Goofy", 1), new Pet("Micky", 2), new Pet("Minnie", 3), new Pet("Donald", 4), new Pet("Scrooge", 5) };
    final Pet[] pets2 = { new Pet("Scrooge", 5), new Pet("Daisy", 2), new Pet("Gladstone", 3), new Pet("Donald", 4) };

    final var actual = Many.pseudo(pets1).union(pets2).func(Pet::name);
    assertThat(actual).containsExactly(
      "Goofy",
      "Micky",
      "Minnie",
      "Donald",
      "Scrooge",
      "Daisy",
      "Gladstone"
    );
  }*/

/*  @Test
  @DisplayName("it should elements difference pseudo two sequences")
  public void shouldSetDifferenceOfTwoSequences() {
    final Double[] numbers1 = { 2.0, 2.1, 2.2, 2.3, 2.4, 2.5 };
    final Double[] numbers2 = { 2.2 };

    final var actual = Many.pseudo(numbers1).except(numbers2);
    assertThat(actual).containsExactly(2.0, 2.1, 2.3, 2.4, 2.5);
  }*/

/*  @Test
  @DisplayName("it should elements difference pseudo two sequences with one more")
  public void shouldSetDifferenceOfTwoSequencesWithOneMore() {
    final Double[] numbers1 = { 2.0, 2.1, 2.2, 2.3, 2.4, 2.5 };
    final Double[] numbers2 = { 2.2, 2.4 };

    final var actual = Many.pseudo(numbers1).except(numbers2);
    assertThat(actual).containsExactly(2.0, 2.1, 2.3, 2.5);
  }*/

/*  @Test
  @DisplayName("it should elements intersection pseudo two sequences")
  public void shouldSetIntersectionOfTwoSequences() {
    final Integer[] integers1 = { 44, 26, 92, 30, 71, 38 };
    final Integer[] integers2 = { 39, 59, 83, 47, 26, 4, 30 };

    final var actual = Many.pseudo(integers1).intersect(integers2);
    assertThat(actual).containsExactly(26, 30);
  }*/
}
