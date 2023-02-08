package re.artoo.lance.query.cursor.routine.concat;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Head;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Head<T> next;

  Liter(final Head<T> head) {this.next = head;}

  @Override
  public final TryFunction1<T[], Cursor<T>> onArray() {
    return prev -> Cursor.chain(Cursor.open(prev), next);
  }

  @Override
  public TryFunction1<Head<T>, Cursor<T>> onSource() {
    return prev -> Cursor.chain(prev, next);
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.chain(Cursor.from(prev), next);
  }
}
