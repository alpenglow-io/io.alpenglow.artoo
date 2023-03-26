package re.artoo.lance.value;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.value.steam.None;
import re.artoo.lance.value.steam.Some;

import static java.lang.System.out;

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
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head -> Steam.of(head.step().<Q>then(scope -> scope.map(operation))).concat(some.tail().<TARGET, Q>map(operation));
      default -> Steam.none();
    };
  }

  default Steam<ELEMENT> peek(TryConsumer1<? super ELEMENT> operation) throws Throwable {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head -> Steam.of(head.step().<ELEMENT>then(scope -> scope.peek(operation))).concat(some.tail().peek(operation));
      default -> Steam.none();
    };
  }

  public static void main(String[] args) throws Throwable {
    Steam.of(Puff.initial(12), Puff.initial(13), Puff.initial(14), Puff.initial(15))
      .peek(out::println)
      .map(it -> it * 2)
      .peek(out::println)
      .map(Object::toString)
      .map(it -> it + "mq")
      .forEach(out::println);
  }
}

