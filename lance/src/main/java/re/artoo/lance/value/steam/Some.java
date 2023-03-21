package re.artoo.lance.value.steam;

import re.artoo.lance.value.Steam;
import re.artoo.lance.value.Puff;

import java.util.Arrays;
import java.util.Iterator;

import static re.artoo.lance.value.steam.Elements.Companion;

public record Some<ELEMENT>(Puff<ELEMENT>[] puffs) implements Steam<ELEMENT> {
  @SafeVarargs
  public Some(Puff<ELEMENT>[] head, Puff<ELEMENT>... tail) {
    this(Companion.concat(head, tail));
  }
  @SafeVarargs
  public Some(Puff<ELEMENT> head, Puff<ELEMENT>... tail) {
    this(Companion.asArray(head), tail);
  }
  @SafeVarargs
  public Some(Puff<ELEMENT>[] head, Puff<ELEMENT> body, Puff<ELEMENT>... tail) {
    this(Companion.concat(head, Companion.concat(Companion.asArray(body), tail)));
  }
  @SafeVarargs
  public Some(int from, Puff<ELEMENT>... elements) {
    this(Companion.copy(from, elements));
  }
  public Some(Puff<ELEMENT>[] head, Puff<ELEMENT> tail) {
    this(head, Companion.asArray(tail));
  }

  public Puff<ELEMENT> step() { return puffs[0]; }

  @Override
  public String toString() {
    return "Some{" +
      "steps=" + Arrays.toString(puffs) +
      '}';
  }

  @Override
  public Iterator<ELEMENT> iterator() {
    return new N();
  }

  private final class N implements Iterator<ELEMENT> {
    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < puffs.length;
    }

    @Override
    public ELEMENT next() {
      return puffs[index++].apply();
    }
  }
}
