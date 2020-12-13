package io.artoo.frost.ui;

import com.dropbox.core.v2.files.FolderMetadata;
import com.googlecode.lanterna.gui2.Panel;
import io.artoo.frost.dropbox.Dropbox;
import io.artoo.frost.scene.Scene;
import io.artoo.frost.scene.element.Section;
import io.artoo.lance.fetcher.Cursor;

public interface Folders extends Section {
  static Folders dropbox(Scene scene, Dropbox dropbox) {
    return new Dbx(dropbox, scene);
  }

  final class Dbx implements Folders {
    private final Dropbox dropbox;
    private final Scene scene;

    Dbx(final Dropbox dropbox, final Scene scene) {
      this.dropbox = dropbox;
      this.scene = scene;
    }

    @Override
    public Cursor<Panel> cursor() {
      return
        scene.sectionVertical(
          dropbox
            .foldersOf("/docs")
            .select(FolderMetadata::getName)
            .select(String::trim)
            .order()
            .select(folder -> scene.button(folder))
        )
          .cursor();
    }
  }
}
