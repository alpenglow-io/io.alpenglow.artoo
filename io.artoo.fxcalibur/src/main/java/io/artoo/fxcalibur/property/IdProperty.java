package io.artoo.fxcalibur.property;

public interface IdProperty extends ParentProperty {
  static IdProperty id(String id) {
    return it -> it.setId(id);
  }
}
