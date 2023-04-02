package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Next;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

import java.util.HashSet;
import java.util.Set;

public record Distinct<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom, Set<ELEMENT> set, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Distinct(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, Atom.reference(), new HashSet<>(), condition);
  }
  @Override
  public boolean hasNext() {
    if (atom.isNotFetched()) return true;
    if (!probe.hasNext()) return false;

    while (probe.hasNext() && atom.isFetched()) {
      try {
        var next = probe.next();
        if (next instanceof Next.Success<ELEMENT> it && (!condition.invoke(it.index(), it.element()) || !set.contains(it.element()))) {
          atom.element(it);
          set.add(it.element());
        } else if (next instanceof Next.Failure<ELEMENT> || next instanceof Next.Nothing) {
          atom.element(next);
        }
      } catch (Throwable throwable) {
        atom.element(Next.failure(throwable));
      }
    }

    return atom.isNotFetched();
  }

  @Override
  public Next<ELEMENT> next() {
    return hasNext() ? atom.elementThenFetched() : Next.failure("distinct", "distinctable");
  }
}
