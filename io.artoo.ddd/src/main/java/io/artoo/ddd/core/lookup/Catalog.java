package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.lance.value.Symbol;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Catalog {
  View view(Id id);

  Catalog project(Id correlationId, Domain.Tract tract, String tractName);
  default Catalog project(Id correlationId, Domain.Tract tract) {
    return project(correlationId, tract, null);
  }
}

final class InMemory implements Catalog {
  private final List<Materialization> materialization;

  InMemory() { this(new CopyOnWriteArrayList<>()); }

  private InMemory(List<Materialization> materialization) {
    this.materialization = materialization;
  }

  @Override
  public View view(Id id) {
    return new Query(this, id);
  }

  @Override
  public Catalog project(Id correlationId, Domain.Tract tract, String tractName) {
    return null;
  }

  private record Materialization(Symbol materializationId, String tractName, Domain.Tract tract, Id viewId, String viewAlias, Instant persistedAt) {
    Materialization(Id viewId, Domain.Tract tract, String stateAlias) {
      this(Symbol.unique(), tract.$name(), tract, viewId, stateAlias, Instant.now());
    }
  }
}
