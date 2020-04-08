package trydent.desktop.property;

public interface IdProperty extends ParentProperty {
  static IdProperty id(String id) {
    return it -> it.setId(id);
  }
}
