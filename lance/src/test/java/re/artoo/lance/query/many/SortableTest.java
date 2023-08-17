package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.many.Sortable.by;

import java.util.Objects;

import static java.lang.System.*;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static re.artoo.lance.query.Many.from;

public class SortableTest implements re.artoo.lance.Test {
  @Test
  @DisplayName("should order by hashcode asc")
  public void shouldOrderByHashcode() {
    final var ordered = Many.from(4, 3, 2, 1).order(by::ascending);

    assertThat(ordered).containsExactly(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should order by hashcode desc")
  public void shouldOrderByDescendingHashcode() {
    final var ordered = Many.from(1, 2, 3, 4).order(by::descending);

    assertThat(ordered).containsExactly(4, 3, 2, 1);
  }

  @Test
  @DisplayName("should order by string desc")
  public void shouldOrderByDescendingString() {
    final var ordered = Many.from("A a", "b B", "C c", "D D").select(it -> it.toUpperCase()).order(by::descending);

    assertThat(ordered.cursor()).toIterable().containsExactly("D D", "C C", "B B", "A A");
  }

  @Test
  @DisplayName("should order by hashcode on big pseudo data elements")
  public void shouldOrderByHashcodeOnBigSet() {
    final var ints = range(0, 1_000).map(it -> 999 - it).boxed().toArray(Integer[]::new);
    final var expected = range(0, 1_000).boxed().toArray(Integer[]::new);

    final var actual = from(ints).order(by::ascending);

    assertThat(actual).containsExactly(expected);
  }

  @Test
  @DisplayName("should order by country and id descending")
  public void shouldOrderByCountry() {
    final var customers = new Customer[]{
      CUSTOMERS[62],
      CUSTOMERS[76],
      CUSTOMERS[50],
      CUSTOMERS[20],
      CUSTOMERS[59],
      CUSTOMERS[12],
      CUSTOMERS[54],
      CUSTOMERS[64],
    };

    final var names = from(customers)
      .order(
        by -> by.ascending(Customer::country),
        by -> by.descending(Customer::id)
      );

    assertThat(names).containsExactly(
      CUSTOMERS[64],
      CUSTOMERS[54],
      CUSTOMERS[12],
      CUSTOMERS[59],
      CUSTOMERS[20],
      CUSTOMERS[76],
      CUSTOMERS[50],
      CUSTOMERS[62]
    );
  }

  @Test
  @DisplayName("should order by country ascending, name descending, id descending")
  public void shouldOrderByCountryAndId() {
    final var customers = new Customer[]{
      CUSTOMERS[47],
      CUSTOMERS[64],
      CUSTOMERS[46],
      CUSTOMERS[54],
      CUSTOMERS[33],
      CUSTOMERS[12],
      CUSTOMERS[35],
    };

    final var countries =
      from(customers)
        .select(it -> {
          out.println(it);
          return it;
        })
        .order(
          by -> by.ascending(Customer::country),
          by -> by.descending(customer -> customer.name().toUpperCase()),
          by -> by.descending(Customer::id)
        );

    assertThat(countries.cursor()).toIterable().containsExactly(
      CUSTOMERS[64], // 64  	Rancho grande  	Sergio Gutiérrez  	Av. del Libertador 900  	Buenos Aires  	1010  	Argentina
      CUSTOMERS[54], // 54  	Océano Atlántico Ltda.  	Yvonne Moncada  	Ing. Gustavo Moncada 8585 Piso 20-A  	Buenos Aires  	1010  	Argentina
      CUSTOMERS[12], // 12  	Cactus Comidas para llevar  	Patricio Simpson  	Cerrito 333  	Buenos Aires  	1010  	Argentina
      CUSTOMERS[47],
      CUSTOMERS[46],
      CUSTOMERS[35],
      CUSTOMERS[33]
    );
  }

  @Test
  @DisplayName("should order a non-trivial ascending and descending arrangement")
  public void shouldOrderAComplexAscendingAndDescendingArrangement() {
    final var customers = new Customer[]{
      CUSTOMERS[17],
      CUSTOMERS[4],
      CUSTOMERS[11],
      CUSTOMERS[14],
      CUSTOMERS[36],
      CUSTOMERS[42],
      CUSTOMERS[54],
      CUSTOMERS[91],
    };

    final var ordered =
      from(customers)
        .order(
          by -> by.descending(Customer::contact),
          by -> by.ascending(Customer::city),
          by -> by.descending(Customer::postalCode),
          by -> by.ascending(Customer::country),
          by -> by.descending(Customer::id)
        );

    assertThat(ordered).containsExactly(
      CUSTOMERS[91],
      CUSTOMERS[54],
      CUSTOMERS[42],
      CUSTOMERS[36],
      CUSTOMERS[14],
      CUSTOMERS[11],
      CUSTOMERS[4],
      CUSTOMERS[17]
    );
  }
}
