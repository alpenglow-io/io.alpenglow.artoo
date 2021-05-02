package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Id;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Many;

@SuppressWarnings("unchecked")
public interface Lookup<R extends Record, L extends Lookup<R, L>> extends Suppl.Uni<R[]> {
  default Many<R> asQueryable() {
    return Many.from(this);
  }

  default L attach(R... records) { return attach(Many.from(records)); }
  L attach(Many<R> records);
  <T> T fetch(Id id, Func.Uni<? super Many<R>, ? extends T> fetch);
  boolean match(Id id, Pred.Uni<? super Many<R>> match);
}
