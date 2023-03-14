package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.query.Many;

public interface Peekable<ELEMENT> extends Queryable<ELEMENT> {
  default Many<ELEMENT> peek(TryConsumer1<? super ELEMENT> peek) {
    return () -> cursor().peek(peek);
  }

  default Many<ELEMENT> peek(TryIntConsumer1<? super ELEMENT> peek) {
    return () -> cursor().peek(peek);
  }

  default Many<ELEMENT> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

