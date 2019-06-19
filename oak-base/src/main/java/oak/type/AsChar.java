package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import oak.func.sup.CharSupplier;
import oak.func.sup.StringSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface AsChar extends Iterable<Character>, CharSupplier, Functional.Sup {
  char get();

  @Override
  default char getAsChar() { return get(); }

  @NotNull
  @Override
  default Iterator<Character> iterator() { return Cursor.once(get()); }
}
