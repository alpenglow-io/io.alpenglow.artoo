package io.artoo.frost;

import io.artoo.frost.dropbox.Dropbox;
import io.artoo.frost.ui.Folders;

import static io.artoo.frost.scene.Scene.scene;

public class FrostCli {
  public static void main(String[] args) {
    scene().get(it ->
      it.open(
        it.sectionBorder(location -> location
          .top(
            it.label("Something nice over here!")
          )
          .left(
            Folders.dropbox(
              it,
              Dropbox.client(
                System.getenv("DBX_ACCESS_TOKEN")
              )
            )
          )
          .center(
            it.textArea()
          )
          .bottom(
            it.buttonClose()
          )
        )
      )
    );
  }
}
