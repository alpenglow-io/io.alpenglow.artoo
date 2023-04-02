package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Next;
import re.artoo.lance.query.cursor.Probe;

public record Er<ELEMENT, EXCEPTION extends RuntimeException>(Probe<ELEMENT> probe, String message, TryFunction2<? super String, ? super Throwable, ? extends EXCEPTION> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Next<ELEMENT> next() {
    try {
      return hasNext() ?
        switch (probe.next()) {
          case Next.Failure<ELEMENT> it -> throw fallback.invoke(message, it.exception());
          case Next.Success<ELEMENT> it -> it;
          case Next.Nothing it -> (Next<ELEMENT>) it;
        }
        : Next.failure("er", "erratic");
    } catch (Throwable throwable) {
      return Next.failure(throwable);
    }
  }
}
