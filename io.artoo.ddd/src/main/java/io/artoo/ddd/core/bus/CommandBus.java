package io.artoo.ddd.core.bus;

import io.artoo.ddd.core.Domain;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.function.Consumer;

import static io.artoo.ladylake.type.SimpleName.simpleNameOf;
import static io.vertx.core.Future.succeededFuture;

public interface CommandBus {
  <C extends Domain.Command> Future<Void> send(C command);

  <C extends Domain.Command> CommandBus when(Class<C> command, Consumer<C> handler);

  static CommandBus create(EventBus eventBus) {
    return new InMemory(eventBus);
  }

  final class InMemory implements CommandBus {
    private final EventBus eventBus;

    private InMemory(final EventBus eventBus) {this.eventBus = eventBus;}

    @Override
    public <C extends Domain.Command> Future<Void> send(final C command) {
      eventBus.publish(command.$name(), JsonObject.mapFrom(command));
      return succeededFuture();
    }

    @Override
    public <C extends Domain.Command> CommandBus when(final Class<C> command, final Consumer<C> handler) {
      eventBus.<JsonObject>localConsumer(simpleNameOf(command).toString(), message -> handler.accept(message.body().mapTo(command)));
      return this;
    }
  }
}
