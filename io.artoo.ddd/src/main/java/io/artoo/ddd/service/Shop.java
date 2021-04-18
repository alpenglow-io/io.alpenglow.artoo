package io.artoo.ddd.service;

import io.artoo.ddd.ordering.Order;
import io.artoo.ddd.domain.bus.CommandBus;
import io.artoo.ddd.ordering.Orders;

public interface Shop {
  static Shop service(Orders orders, CommandBus commandBus) {
    return new Service(orders, commandBus);
  }

  void stock(Order.Command.Put command);
  void take(Order.Command.Take command);
  void pay(Order.Command.Pay command);
}

final class Service implements Shop {
  private final Orders orders;

  Service(final Orders orders, final CommandBus commandBus) {
    this.orders = orders;
    commandBus
      .when(Order.Command.Put.class, this::stock)
      .when(Order.Command.Take.class, this::take)
      .when(Order.Command.Pay.class, this::pay);
  }

  @Override
  public void stock(final Order.Command.Put command) {
    orders.save(Order.create().put(command.uuid()));
  }

  @Override
  public void take(final Order.Command.Take command) {
    orders.save(orders.findBy(command.uuid()).take(command.uuid(), command.when()));
  }

  @Override
  public void pay(final Order.Command.Pay command) {
    orders.save(orders.findBy(command.uuid()).pay(command.uuid(), command.when()));
  }
}
