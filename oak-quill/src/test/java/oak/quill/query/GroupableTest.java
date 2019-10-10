package oak.quill.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static oak.quill.Quill.from;
import static oak.quill.query.Customers.customers;
import static oak.quill.query.Orders.orders;
import static oak.quill.query.Shippers.shippers;
import static org.assertj.core.api.Assertions.assertThat;

class GroupableTest {
  @Test
  @DisplayName("should group by floor age")
  void shouldGroupByFlooredAge() {
    final var pets = new Pet[]{
      new Pet("Barley", 8.3),
      new Pet("Boots", 4.9),
      new Pet("Whiskey", 1.5),
      new Pet("Daisy", 4.3)
    };

    final var query = from(pets)
      .select(pet -> pet.age)
      .groupBy(Math::floor)
      .select((age, ages) -> new Object() {
          double key = age;
          long count = ages.size();
          double min = min(ages);
          double max = max(ages);
        }
      )
      .select(result -> String.format("key:%s,count:%s,min:%s,max:%s", result.key, result.count, result.min, result.max));

    assertThat(query).containsExactly(
      "key:8.0,count:1,min:8.3,max:8.3",
      "key:4.0,count:2,min:4.3,max:4.9",
      "key:1.0,count:1,min:1.5,max:1.5"
    );
  }

  @Test
  @DisplayName("should group by country ordered by count")
    // (since it's the group-key)
  void shouldGroupByCountry() {
    final var query = from(customers)
      .groupBy(customer -> customer.country)
      .select((cntry, customs) -> new Object() {
        long count = customs.size();
        String country = cntry;
      })
      .select(grouped -> String.format("%d;%s", grouped.count, grouped.country));

    assertThat(query).contains(
      "3;Argentina",
      "2;Austria",
      "2;Belgium",
      "9;Brazil",
      "3;Canada",
      "2;Denmark",
      "2;Finland",
      "11;France",
      "11;Germany",
      "1;Ireland",
      "3;Italy",
      "5;Mexico",
      "1;Norway",
      "1;Poland",
      "2;Portugal",
      "5;Spain",
      "2;Sweden",
      "2;Switzerland",
      "7;UK",
      "13;USA",
      "4;Venezuela"
    );
  }

  @Test
  @DisplayName("should group by shipper-name counting number of orders")
  void shouldGroupByShipperNameCountingOrders() {
    final class ShippedOrder {
      private final Order order;
      private final String shipperName;

      @Contract(pure = true)
      private ShippedOrder(Order order, @NotNull Shipper shipper) {
        this.shipperName = shipper.name;
        this.order = order;
      }
    }

    final var query = from(orders)
      .join(from(shippers))
      .on((order, shipper) -> order.shipperId == shipper.id)
      .select(ShippedOrder::new)
      .groupBy(joined -> joined.shipperName)
      .select((name, orders) ->
        String.format("%s;%d",
          name,
          orders.size()
        )
      );

    assertThat(query).contains(
      "Federal Shipping;68",
      "Speedy Express;54",
      "United Package;74"
    );
  }

  @Test
  @DisplayName("should group by country having count of customer-id greater than 5")
  void shouldGroupByCountryHavingCountGreaterThan5() {
    final var query =
      from(customers)
        .groupBy(customer -> customer.country)
        .having((cntry, customers) -> customers.size() > 5)
        .select((country, customers) ->
          String.format("%d;%s",
            customers.size(),
            country
          )
        );

    assertThat(query).contains(
      "9;Brazil",
      "11;France",
      "11;Germany",
      "7;UK",
      "13;USA"
    );
  }

  @Test
  @DisplayName("should group by country and city")
  void shouldGroupByCountryAndCity() {
    final var query =
      from(customers)
        .groupBy(customer -> customer.country, customer -> customer.city)
        .having((country, city, customers) -> customers.size() >= 2)
        .select((country, city, customers) ->
          String.format("%d;%s;%s",
            customers.size(),
            country.trim(),
            city.trim()
          )
        );

    assertThat(query).contains(
      "6;UK;London",
      "4;Brazil;São Paulo",
      "3;Argentina;Buenos Aires",
      "2;Portugal;Lisboa",
      "3;Spain;Madrid",
      "3;Brazil;Rio de Janeiro",
      "5;Mexico;México D.F.",
      "2;France;Nantes",
      "2;France;Paris",
      "2;USA;Portland"
    );
  }
}
