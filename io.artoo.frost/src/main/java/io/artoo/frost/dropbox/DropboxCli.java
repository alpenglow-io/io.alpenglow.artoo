package io.artoo.frost.dropbox;

import com.dropbox.core.v2.files.FolderMetadata;

import static java.lang.System.out;

public enum DropboxCli {
  ;

  public static void main(String[] args) {
      final var accessToken = System.getenv("DBX_ACCESS_TOKEN");

      Dropbox.client(accessToken)
        .foldersOf("/docs")
        .select(FolderMetadata::getName)
        .order()
        .eventually(out::println);
  }
}
