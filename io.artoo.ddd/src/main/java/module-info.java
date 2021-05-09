module io.artoo.ddd {
  requires java.desktop;
  requires java.logging;
  requires java.naming;
  requires java.sql;
  requires java.xml;

  requires io.artoo.lance;
  requires io.artoo.ladylake;

  requires org.jetbrains.annotations;
  requires io.vertx.core;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires org.hsqldb;

  opens io.artoo.ddd.forum to com.fasterxml.jackson.databind;

  exports io.artoo.ddd.core;

}
