package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Materializations extends Many<Domain.Tract> {
  record Materialization(String tractName, Domain.Tract tract, Instant persistedAt) {
    Materialization(Domain.Tract tract) {
      this(tract.$name(), tract, Instant.now());
    }
  }

  View view(String name);

  Materializations submit(Domain.Tract tract);
}

final class Catalog implements Materializations {
  private final List<Materialization> materializations;

  Catalog() {
    this(new CopyOnWriteArrayList<>());
  }

  private Catalog(List<Materialization> materializations) {
    this.materializations = materializations;
  }

  @Override
  public View view(String name) {
    return new Materialized(this, name);
  }

  @Override
  public Materializations submit(Domain.Tract tract) {
    this.materializations.add(new Materialization(tract));
    return this;
  }

  @Override
  public Cursor<Domain.Tract> cursor() {
    return Many
      .from(materializations)
      .select(Materialization::tract)
      .cursor();
  }
}
