package re.artoo.lance.value;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.value.steam.None;
import re.artoo.lance.value.steam.Some;

public sealed interface Steam<ELEMENT> extends Iterable<ELEMENT> permits Some, None {
  @SafeVarargs
  static <ELEMENT> Steam<ELEMENT> of(Puff<ELEMENT>... puffs) {
    return new Some<>(puffs);
  }
  @SuppressWarnings("unchecked")
  private static <ELEMENT> Steam<ELEMENT> none() {
    return (Steam<ELEMENT>) None.Companion;
  }
  default Steam<ELEMENT> concat(Steam<ELEMENT> steam) {
    return switch (this) {
      case None ignored when steam instanceof Some<ELEMENT> tail -> tail;
      case Some<ELEMENT> head when steam instanceof Some<ELEMENT> tail -> new Some<>(head.puffs(), tail.puffs());
      default -> this;
    };
  }

  default Steam<ELEMENT> head() {
    return switch (this) {
      case Some<ELEMENT> some -> new Some<>(some.puffs()[0]);
      case None ignored -> this;
    };
  }

  default Steam<ELEMENT> tail() {
    return switch (this) {
      case Some<ELEMENT> some when some.puffs().length > 1 -> new Some<>(1, some.puffs());
      case Some<ELEMENT> ignored -> Steam.none();
      case None ignored -> this;
    };
  }

  default <TARGET, Q extends TARGET> Steam<Q> map(TryFunction1<? super ELEMENT, Q> operation) throws Throwable {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head -> Steam.of(head.step().<Q>then(unit -> unit.map(operation))).concat(some.tail().<TARGET, Q>map(operation));
      default -> Steam.none();
    };
  }

  public static void main(String[] args) throws Throwable {
    Steam.of(Puff.initial(12)).map(it -> it * 2);
  }
}

