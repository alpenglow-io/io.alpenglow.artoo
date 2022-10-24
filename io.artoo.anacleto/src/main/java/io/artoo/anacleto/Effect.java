package io.artoo.anacleto;

public interface Effect<MESSAGE> {
  Behaviour<MESSAGE> effect(Behaviour<MESSAGE> behaviour);

  static <MESSAGE> Effect<MESSAGE> become(Behaviour<MESSAGE> next) {
    return current -> next;
  }

  static <MESSAGE> Effect<MESSAGE> remain() {
    return current -> current;
  }

  static <MESSAGE> Effect<MESSAGE> leave() {
    return become(message -> Log.<MESSAGE, Effect<MESSAGE>>info("Dead letter %s", message).then(Effect::remain));
  }
}
