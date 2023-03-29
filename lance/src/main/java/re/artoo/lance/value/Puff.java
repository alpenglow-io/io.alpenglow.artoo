package re.artoo.lance.value;

import re.artoo.lance.func.TryFunction1;

import static java.lang.System.out;

public interface Puff<ELEMENT> {
  static <ELEMENT> Puff<ELEMENT> initial(ELEMENT element) {
    return new Sync<>(Scope.of(element), scope -> scope);
  }

  <TARGET> Puff<TARGET> then(TryFunction1<? super Scope<? extends ELEMENT>, ? extends Scope<? extends TARGET>> operation) throws Throwable;

  ELEMENT apply();

  public static void main(String[] args) throws Throwable {
    var steps = Puff.initial(12)
      .then(scope -> scope.map(it -> it * 2))
      .then(scope -> scope.peek(out::println));
    out.println("Hellooo!");
    steps.apply();
  }
}

record Sync<SOURCE, THEN>(Scope<SOURCE> initial, TryFunction1<? super Scope<? extends SOURCE>, ? extends Scope<? extends THEN>> operation) implements Puff<THEN> {
  @Override
  public <TARGET> Puff<TARGET> then(TryFunction1<? super Scope<? extends THEN>, ? extends Scope<? extends TARGET>> anotherOperation) {
    return new Sync<>(initial, this.operation.then(anotherOperation));
  }

  @Override
  public THEN apply() {
    return operation.apply(initial).otherwise(RuntimeException::new);
  }
}
