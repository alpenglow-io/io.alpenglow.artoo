package io.artoo.ddd.service;

import io.artoo.ddd.item.Item;
import io.artoo.ddd.domain.bus.CommandBus;
import io.artoo.ddd.item.Items;

public interface Shop {
  static Shop service(Items items, CommandBus commandBus) {
    return new Service(items, commandBus);
  }

  void stock(Item.Command.Put command);
  void take(Item.Command.Take command);
  void pay(Item.Command.Pay command);
}

final class Service implements Shop {
  private final Items items;

  Service(final Items items, final CommandBus commandBus) {
    this.items = items;
    commandBus
      .when(Item.Command.Put.class, this::stock)
      .when(Item.Command.Take.class, this::take)
      .when(Item.Command.Pay.class, this::pay);
  }

  @Override
  public void stock(final Item.Command.Put command) {
    items.save(Item.create().put(command.uuid()));
  }

  @Override
  public void take(final Item.Command.Take command) {
    items.save(items.findBy(command.uuid()).take(command.uuid(), command.when()));
  }

  @Override
  public void pay(final Item.Command.Pay command) {
    items.save(items.findBy(command.uuid()).pay(command.uuid(), command.when()));
  }
}
