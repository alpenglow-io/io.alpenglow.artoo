package io.artoo.ddd.domain;

import io.artoo.lance.func.Func;
import io.artoo.lance.scope.Let;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public sealed interface Id extends Let<UUID> {
  static Id random() {
    return new Random();
  }

  record Random(UUID uuid) implements Id {
    public Random() { this(randomUUID()); };

    @Override
    public <R> R let(Func.Uni<? super UUID, ? extends R> func) {
      return func.apply(uuid);
    }
  }
}
