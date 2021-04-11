package io.artoo.ddd.event;

import io.artoo.ddd.Domain;
import io.artoo.ddd.util.Lettering;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Consumer;

import static io.vertx.core.Future.succeededFuture;

public interface EventBus {
  <E extends Domain.Event> Future<Void> emit(E event);

  <E extends Domain.Event> Future<Void> when(Class<E> event, Consumer<E> consumer);

  static EventBus create(io.vertx.core.eventbus.EventBus eventBus) {
    return new InMemory(eventBus);
  }
}

final class InMemory implements EventBus, Lettering {
  private final io.vertx.core.eventbus.EventBus eventBus;

  InMemory(final io.vertx.core.eventbus.EventBus eventBus) {this.eventBus = eventBus;}

  @Override
  public <E extends Domain.Event> Future<Void> emit(final E event) {
    eventBus.publish(asKebabCase(event.getClass()), JsonObject.mapFrom(event));
    return succeededFuture();
  }

  @Override
  public <E extends Domain.Event> Future<Void> when(final Class<E> event, final Consumer<E> consumer) {
    eventBus.<JsonObject>localConsumer(asKebabCase(event), message -> consumer.accept(message.body().mapTo(event)));
    return succeededFuture();
  }
}

