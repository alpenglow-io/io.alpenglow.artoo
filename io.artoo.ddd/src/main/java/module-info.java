module io.artoo.ddd {
  requires io.artoo.lance;

  requires org.jetbrains.annotations;
  requires io.vertx.core;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires io.artoo.ladylake;

  opens io.artoo.ddd.forum to com.fasterxml.jackson.databind;

  exports io.artoo.ddd.core;

}
