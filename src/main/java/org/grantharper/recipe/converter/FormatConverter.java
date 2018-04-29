package org.grantharper.recipe.converter;

import java.io.IOException;
import java.nio.file.Path;

public interface FormatConverter
{

  Path convert(Path inputImage, Path outputDirectory) throws IOException;

}
