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

     */
  }
}