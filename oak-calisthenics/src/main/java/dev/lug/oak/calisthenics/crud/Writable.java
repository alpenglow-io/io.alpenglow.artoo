package dev.lug.oak.calisthenics.crud;

public interface Writable<T, R, I extends Iterable<T>> extends Iterable<T> {
  Id add(R record);
  I remove(Id id);
}
