package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.query.Many;

public interface Triggerable<ELEMENT> extends Queryable<ELEMENT> {
  default Many<ELEMENT> fire(TryConsumer1<? super ELEMENT> peek) {
    return () -> cursor().peek(peek);
  }

  default Many<ELEMENT> fire(TryIntConsumer1<? super ELEMENT> peek) {
    return () -> cursor().peek(peek);
  }

  default Many<ELEMENT> trap(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

