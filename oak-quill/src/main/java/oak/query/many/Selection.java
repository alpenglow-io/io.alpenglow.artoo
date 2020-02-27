package oak.query.many;

import oak.func.$2.IntCons;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

final class Selection<R, M extends oak.query.Many<R>> implements oak.query.Many<R> {
  private final Queryable<M> selected;
  private final IntCons<? super R> peek;

  Selection(final Queryable<M> selected, final IntCons<? super R> peek) {
    this.selected = selected;
    this.peek = peek;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    for (final var many : selected) {
      var index = 0;
      for (var cursor = many.iterator(); cursor.hasNext(); index++) {
        var it = cursor.next();
        peek.acceptInt(index, it);
        array.add(it);
      }
    }
    return array.iterator();
  }
}
