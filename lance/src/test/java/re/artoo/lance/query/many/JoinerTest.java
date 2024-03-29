package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static re.artoo.lance.query.Many.from;

public class JoinerTest implements re.artoo.lance.Test {
  @Test
  @DisplayName("should join two many naturally")
  public void shouldJoinNaturally() {
    final var customers1 = new Customer[]{
      CUSTOMERS[1],
      CUSTOMERS[2],
      CUSTOMERS[3],
      CUSTOMERS[5],
      CUSTOMERS[7],
      CUSTOMERS[11],
      CUSTOMERS[13]
    };

    final var customers2 = new Customer[]{
      CUSTOMERS[2],
      CUSTOMERS[4],
      CUSTOMERS[5],
      CUSTOMERS[11],
      CUSTOMERS[13],
      CUSTOMERS[17],
      CUSTOMERS[19]
    };
/*
    final var customers =
      from(customers1)
        .outerJoin(from(customers2))
        .select(Pair::first);

    assertThat(customers).containsExactly(
      CUSTOMERS[2],
      CUSTOMERS[5],
      CUSTOMERS[11],
      CUSTOMERS[13]
    );*/
  }

  @Test
  @DisplayName("should join two many")
  public void shouldJoinManies() {
    record OrderCustomer(long orderId, String name) {
    }

    var customers = from(CUSTOMERS);
    //for (int i = 0; i < 1_000; i++) customers = customers.concat(from(CUSTOMERS));
/*
    final var orderCustomers =
      from(ORDERS)
        .outerJoin(customers)
        .on((order, customer) -> order.customerId() == customer.id())
        .select(joined -> joined.let((order, customer) -> new OrderCustomer(order.id(), customer.name())))
        .order().by(OrderCustomer::name).by(OrderCustomer::orderId, desc);

    assertThat(orderCustomers).first().isEqualTo(new OrderCustomer(10308, "Ana Trujillo Emparedados y helados"));*/
  }
}
