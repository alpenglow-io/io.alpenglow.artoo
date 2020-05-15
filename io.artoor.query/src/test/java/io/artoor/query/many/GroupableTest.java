package io.artoor.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoor.query.Many.from;
import static io.artoor.query.many.TestData.CUSTOMERS;
import static io.artoor.query.many.TestData.ORDERS;
import static io.artoor.query.many.TestData.SHIPPERS;
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
      .select(Pet::age)
      .groupBy(Math::floor)
      .select((age, ages) ->
        String.format(
          "key:%s,count:%s,min:%s,max:%s",
          age,
          ages.count().asInt(),
          ages.min().asDouble(),
          ages.max().asDouble()
        )
      );

    assertThat(query).containsExactly(
      "key:8.0,count:1,min:8.3,max:8.3",
      "key:4.0,count:2,min:4.3,max:4.9",
      "key:1.0,count:1,min:1.5,max:1.5"
    );
  }

  // (since it's the group-key)
  @Test
  @DisplayName("should group by country ordered by count")
  void shouldGroupByCountry() {
    final var query = from(CUSTOMERS)
      .groupBy(Customer::country)
      .select((country, customers) ->
        String.format(
          "%d;%s",
          customers.count().asInt(),
          country
        )
      );

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

    final var query =
      from(ORDERS)
        .join(SHIPPERS).on((order, shipper) -> order.shipperId() == shipper.id())
        .select(arm -> arm.as((order, shipper) -> new ShippedOrder(order, shipper.name())))
        .groupBy(ShippedOrder::shipperName)
        .select((name, orders) ->
          String.format("%s;%d",
            name,
            orders.count().asInt()
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
    final var query = from(CUSTOMERS)
      .groupBy(Customer::country)
      .having((cntry, customers) -> customers.count().asInt() > 5)
      .select(bag -> bag.as((country, customers) ->
        String.format("%d;%s",
          customers.count().asInt(),
          country
        )
      ));

    assertThat(query).contains(
      "9;Brazil",
      "11;France",
      "11;Germany",
      "7;UK",
      "13;USA"
    );
  }

/*  @Test
  @DisplayName("should group by country and city")
  void shouldGroupByCountryAndCity() {
    final var query =
      from(Customers.customers)
        .groupBy(Customer::country, Customer::city)
        .having((country, city, customers) -> customers.count().asInt() >= 2)
        .select(bag -> bag.as((country, city, customers) ->
          String.format("%d;%s;%s",
            customers.size(),
            country.trim(),
            city.trim()
          )
        );

    Assertions.assertThat(query).contains(
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
  }*/
}
