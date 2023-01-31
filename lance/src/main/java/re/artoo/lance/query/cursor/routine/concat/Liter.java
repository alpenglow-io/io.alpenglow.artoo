package re.artoo.lance.query.cursor.routine.concat;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Probe<T> next;

  Liter(final Probe<T> probe) {this.next = probe;}

  @Override
  public final TryFunction1<T[], Cursor<T>> onArray() {
    return prev -> Cursor.chain(Cursor.open(prev), next);
  }

  @Override
  public TryFunction1<Probe<T>, Cursor<T>> onSource() {
    return prev -> Cursor.chain(prev, next);
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.chain(Cursor.from(prev), next);
  }
}
