package io.artoo.ddd;

import io.artoo.ddd.ordering.Order;
import io.artoo.ddd.domain.bus.CommandBus;
import io.artoo.ddd.domain.event.EventBus;
import io.artoo.ddd.domain.EventStore;
import io.artoo.ddd.ordering.Orders;
import io.artoo.ddd.service.Shop;
import io.vertx.core.Vertx;

import java.time.Instant;
import java.util.UUID;

import static java.lang.System.out;

public enum Main {;

  public static void main(String[] args) throws InterruptedException {
    final var vertx = Vertx.vertx();
    final var messageBus = vertx.eventBus();

    final var commandBus = CommandBus.create(messageBus);
    final var eventBus = EventBus.create(messageBus);
    final var eventStore = EventStore.create(eventBus);

    final var items = Orders.from(eventStore);

    final var shop = Shop.service(items, commandBus);

    final var id = UUID.randomUUID();

    commandBus.send(new Order.Command.Put(id));
    commandBus.send(new Order.Command.Take(id, Instant.now()));
    commandBus.send(new Order.Command.Pay(id, Instant.now()));

    Thread.sleep(1000);
    final var oldItem = Order.from(eventStore.historyOf(Order.class, id));
    out.println(oldItem);
    items.save(oldItem);
    final var newItem = Order.from(eventStore.historyOf(Order.class, id));
    out.println(newItem);
  }
}
