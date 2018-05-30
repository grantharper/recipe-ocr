package org.grantharper.recipe.serializer

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors

class AddPageIdToText
{

  static void main(String[] args)
  {
    Path targetDir = Paths.get("data/saved/text")
    Files.list(targetDir).forEach({ path ->
      String filenameWithExtension = path.getFileName()
      String filename = FileUtils.removeExtension(filenameWithExtension)
      List<String> contents = Files.lines(path).collect(Collectors.toList())
      List<String> updatedFile = new ArrayList<>()
      updatedFile.add(filename)
      updatedFile.addAll(contents)
      Files.write(path, updatedFile, Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

    })
  }

}
