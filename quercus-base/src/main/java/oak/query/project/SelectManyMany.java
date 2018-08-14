package oak.query.project;

import oak.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

final class SelectManyMany<R> implements Queryable<R> {
  private final Queryable<Queryable<R>> projection;

  SelectManyMany(final Queryable<Queryable<R>> projection) {
    this.projection = projection;
  }

  @Override
  public final Iterator<R> iterator() {
    final var flatMapped = new ArrayList<R>();
    for (final var some : projection) {
      for (final var it : some) {
        flatMapped.add(it);
      }
    }
    return flatMapped.iterator();
  }
}
