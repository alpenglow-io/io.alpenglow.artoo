package io.artoo.lance.media;

import io.artoo.lance.func.Func;

public interface Json {
  interface Schema {
    interface Field<T, R> extends Func.MaybeFunction<T, R> {
/*
      enum text implements Field<String, String> { field; }
      enum numb implements Field<String, String> { field; }
      enum bool implements Field<String, String> { field; }
      enum date implements Field<String, String> { field; }
      enum arry implements Field<String, String> { field; }
*/
    }

  }
}
