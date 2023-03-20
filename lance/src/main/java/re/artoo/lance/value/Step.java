package re.artoo.lance.value;

import re.artoo.lance.func.TryFunction1;

import static java.lang.System.*;

public interface Step<ELEMENT> {
  static <ELEMENT> Step<ELEMENT> initial(ELEMENT element) {
    return new Sync<>(Unit.of(element), unit -> unit);
  }
  <TARGET> Step<? extends TARGET> then(TryFunction1<? super Unit<? extends ELEMENT>, ? extends Unit<? extends TARGET>> operation) throws Throwable;
  ELEMENT apply();

  public static void main(String[] args) throws Throwable {
    var steps = Step.initial(12)
      .then(unit -> unit.map(it -> it * 2))
      .then(unit -> unit.peek(out::println));
    out.println("Hellooo!");
      steps.apply();
  }
}

record Sync<SOURCE, THEN>(Unit<SOURCE> initial, TryFunction1<? super Unit<? extends SOURCE>, ? extends Unit<? extends THEN>> operation) implements Step<THEN> {
  @Override
  public <TARGET> Step<? extends TARGET> then(TryFunction1<? super Unit<? extends THEN>, ? extends Unit<? extends TARGET>> anotherOperation) {
    return new Sync<SOURCE, TARGET>(initial, this.operation.then(anotherOperation));
  }

  @Override
  public THEN apply() {
    return operation.apply(initial).otherwise(RuntimeException::new);
  }
}
