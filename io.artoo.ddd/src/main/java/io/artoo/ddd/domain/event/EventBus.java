package io.artoo.ddd.domain.event;

import io.artoo.ddd.domain.Domain;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Consumer;

import static io.artoo.ladylake.type.SimpleName.simpleNameOf;
import static io.vertx.core.Future.succeededFuture;

public interface EventBus {
  Future<Void> emit(Domain.EventMessage event);

  <E extends Domain.Event> Future<Void> on(Class<E> event, Consumer<Domain.EventMessage> consumer);

  static EventBus create(io.vertx.core.eventbus.EventBus eventBus) {
    return new InMemory(eventBus);
  }

  final class InMemory implements EventBus {
    private final io.vertx.core.eventbus.EventBus eventBus;

    private InMemory(final io.vertx.core.eventbus.EventBus eventBus) {this.eventBus = eventBus;}

    @Override
    public Future<Void> emit(final Domain.EventMessage message) {
      eventBus.publish(message.event().$name(), JsonObject.mapFrom(message));
      return succeededFuture();
    }

    @Override
    public <E extends Domain.Event> Future<Void> on(final Class<E> event, final Consumer<Domain.EventMessage> consumer) {
      eventBus.<JsonObject>localConsumer(simpleNameOf(event).toString(), message -> consumer.accept(message.body().mapTo(Domain.EventMessage.class)));
      return succeededFuture();
    }
  }
}

