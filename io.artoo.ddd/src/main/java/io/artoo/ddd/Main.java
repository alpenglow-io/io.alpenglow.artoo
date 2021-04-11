package io.artoo.ddd;

import io.artoo.ddd.aggregate.Item;
import io.artoo.ddd.command.CommandBus;
import io.artoo.ddd.event.EventBus;
import io.artoo.ddd.event.EventStore;
import io.artoo.ddd.repository.Items;
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

    final var items = Items.from(eventStore);

    final var shop = Shop.service(items, commandBus);

    final var id = UUID.randomUUID();

    commandBus.send(new Item.Command.Put(id));
    commandBus.send(new Item.Command.Take(id, Instant.now()));
    commandBus.send(new Item.Command.Pay(id, Instant.now()));

    Thread.sleep(1000);
    final var oldItem = Item.from(eventStore.historyOf(Item.class, id));
    out.println(oldItem);
    items.save(oldItem);
    final var newItem = Item.from(eventStore.historyOf(Item.class, id));
    out.println(newItem);
  }
}
