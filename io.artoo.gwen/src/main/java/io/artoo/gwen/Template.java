package io.artoo.gwen;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
public @interface Template {
  Class<Element> value();
}
