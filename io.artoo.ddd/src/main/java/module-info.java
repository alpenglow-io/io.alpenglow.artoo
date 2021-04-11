module io.artoo.ddd {
  requires io.artoo.lance;

  requires org.jetbrains.annotations;
  requires io.vertx.core;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;

  opens io.artoo.ddd.item to com.fasterxml.jackson.databind;

}
