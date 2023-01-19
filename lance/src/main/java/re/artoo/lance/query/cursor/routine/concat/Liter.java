package re.artoo.lance.query.cursor.routine.concat;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Inquiry;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Inquiry<T> next;

  Liter(final Inquiry<T> inquiry) {this.next = inquiry;}

  @Override
  public final TryFunction1<T[], Cursor<T>> onArray() {
    return prev -> Cursor.chain(Cursor.open(prev), next);
  }

  @Override
  public TryFunction1<Inquiry<T>, Cursor<T>> onSource() {
    return prev -> Cursor.chain(prev, next);
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.chain(Cursor.iteration(prev), next);
  }
}
