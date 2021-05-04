package io.artoo.ddd;

import io.vertx.core.Vertx;

public enum Main {;

  public static void main(String[] args) throws InterruptedException {
    final var vertx = Vertx.vertx();
    final var messageBus = vertx.eventBus();

    /*
    final var commandBus = CommandBus.create(messageBus);
    final var eventBus = EventBus.create(messageBus);
    final var eventStore = Transaction.inMemory(eventBus);

    final var items = Orders.from(eventStore);

    final var shop = Shop.service(items, commandBus);

    final var stateId = UUID.randomUUID();

    commandBus.send(new Order.Command.Put(stateId));
    commandBus.send(new Order.Command.Take(stateId, Instant.now()));
    commandBus.send(new Order.Command.Pay(stateId, Instant.now()));

    Thread.sleep(1000);
    final var oldItem = Order.from(eventStore.historyOf(Order.class, stateId));
    out.println(oldItem);
    items.save(oldItem);
    final var newItem = Order.from(eventStore.historyOf(Order.class, stateId));
    out.println(newItem);

     */
  }
}
