package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public interface View extends Many<View.Preview> {
  record Preview(Id correlationId, Domain.Tract... tracts) {}

  void materialize(Id correlationId, Suppl.Uni<? extends Domain.Tract> tract);
  void materialize(Id correlationId, Func.Unary<Domain.Tract> tract);
}

final class Reflect implements View {
  private final Catalog catalog;
  private final Id id;

  Reflect(Catalog catalog, Id id) {
    this.catalog = catalog;
    this.id = id;
  }

  @Override
  public void materialize(Id correlationId, Suppl.Uni<? extends Domain.Tract> tract) {
    catalog.project(correlationId, )
  }

  @Override
  public void materialize(Id correlationId, Func.Unary<Domain.Tract> tract) {

  }

  @Override
  public Cursor<Preview> cursor() {
    return null;
  }
}
