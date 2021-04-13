package io.artoo.ddd.domain.event;

import io.artoo.ddd.domain.Domain;
import io.artoo.ddd.domain.util.Lettering;
import io.artoo.ladylake.type.SimpleName;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Consumer;

import static io.vertx.core.Future.succeededFuture;

public interface EventBus {
  Future<Void> emit(Domain.Event.Log event);

  <E extends Domain.Event> Future<Void> on(Class<E> event, Consumer<Domain.Event.Log> consumer);

  static EventBus create(io.vertx.core.eventbus.EventBus eventBus) {
    return new InMemory(eventBus);
  }

  final class InMemory implements EventBus, Lettering {
    private final io.vertx.core.eventbus.EventBus eventBus;

    private InMemory(final io.vertx.core.eventbus.EventBus eventBus) {this.eventBus = eventBus;}

    @Override
    public Future<Void> emit(final Domain.Event.Log log) {
      eventBus.publish(log.eventName(), JsonObject.mapFrom(log));
      return succeededFuture();
    }

    @Override
    public <E extends Domain.Event> Future<Void> on(final Class<E> event, final Consumer<Domain.Event.Log> consumer) {
      eventBus.<JsonObject>localConsumer(, message -> consumer.accept(message.body().mapTo(EventLog.class)));
      return succeededFuture();
    }
  }
}

