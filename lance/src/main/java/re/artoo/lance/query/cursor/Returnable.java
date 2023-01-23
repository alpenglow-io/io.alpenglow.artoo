package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Returnable<ELEMENT> extends Inquiry<ELEMENT> permits Cursor {
  default <RETURN> Cursor<RETURN> actually(TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    return null;
  }
}

final class Actually<ELEMENT, RETURN> implements Cursor<RETURN> {
  private final Inquiry<? extends ELEMENT> inquiry;
  private final TryIntFunction1<? super ELEMENT, ? extends RETURN> returns;

  Actually(Inquiry<? extends ELEMENT> inquiry, TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    this.inquiry = inquiry;
    this.returns = returns;
  }

  @Override
  public <TO> TO as(Routine<RETURN, TO> routine) {
    return null;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super RETURN, ? extends R> fetch) throws Throwable {
    return inquiry.;
  }

  @Override
  public boolean hasNext() {
    return false;
  }
}
