package io.artoo.anacleto;

@FunctionalInterface
public interface Behaviour<MESSAGE> {
  Effect<MESSAGE> behave(MESSAGE message);
}
