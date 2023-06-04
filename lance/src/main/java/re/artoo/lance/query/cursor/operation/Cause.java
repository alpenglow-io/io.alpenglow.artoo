package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Cause<ELEMENT> implements Cursor<ELEMENT> {
  private final Throwable throwable;
  private boolean hasElement = true;

  public Cause(Throwable throwable) {this.throwable = throwable;}

  @Override
  public boolean hasElement() throws Throwable {
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      throw throwable;
    } finally {
      hasElement = false;
    }
  }
}
