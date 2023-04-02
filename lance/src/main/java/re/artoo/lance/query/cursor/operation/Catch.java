package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Next;
import re.artoo.lance.query.cursor.Probe;

public record Catch<ELEMENT>(Probe<ELEMENT> probe, TryConsumer1<? super Throwable> fallback) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Next<ELEMENT> next() {
    return probe.hasNext() ?
      switch (probe.next()) {
        case Next.Failure<ELEMENT> it -> fallback.selfAccept(it, it.exception());
        case Next.Success<ELEMENT> it -> it;
        case Next.Nothing it -> (Next<ELEMENT>) it;
      }
      : Next.failure("catch", "catchable");
  }
}
