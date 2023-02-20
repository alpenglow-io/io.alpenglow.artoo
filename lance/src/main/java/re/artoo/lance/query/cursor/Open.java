package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;

import static re.artoo.lance.query.cursor.Pointer.alwaysMove;
import static re.artoo.lance.query.cursor.Pointer.neverMove;

public record Open<ELEMENT>(ELEMENT[] elements, Pointer pointer) implements Cursor<ELEMENT> {
  @SafeVarargs
  public Open(ELEMENT... elements) {
    this(elements, elements.length == 0 ? neverMove() : alwaysMove());
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? elements[pointer.indexNext()] : null;
  }

  @Override
  public Probe<ELEMENT> rewind() {
    return new Open<>(elements);
  }

  @Override
  public boolean canFetch() {
    boolean tickable = pointer.index() < elements.length && elements[pointer.index()] != null;
    while (pointer.index() < elements.length && !tickable) {
      pointer.next();
      tickable = elements[pointer.index()] != null;
    }
    return tickable;
  }
}
