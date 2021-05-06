package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;

import static io.artoo.ddd.core.lookup.Materializations.*;

public interface View extends Many<Domain.Tract> {

  void materialize(Suppl.Uni<? extends Domain.Tract> tract);

  void materialize(Func.Unary<Domain.Tract> tract);
}

final class Materialized implements View {
  private final Materializations materializations;
  private final String name;

  Materialized(final Materializations materializations, final String name) {
    this.materializations = materializations;
    this.name = name;
  }

  @Override
  public Cursor<Domain.Tract> cursor() {
    return materializations
      .where(it -> it.$name().equals(name))
      .cursor();
  }

  @Override
  public void materialize(Suppl.Uni<? extends Domain.Tract> tract) {
    this.materializations.submit(tract.get());
  }

  @Override
  public void materialize(Func.Unary<Domain.Tract> tract) {
    this.materializations
      .where(it -> it.$name().equals(name))
      .select(tract::tryApply)
      .eventually(materializations::submit);
  }
}
