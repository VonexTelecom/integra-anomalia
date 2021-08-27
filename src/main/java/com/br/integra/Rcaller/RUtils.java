package com.br.integra.Rcaller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class RUtils {
    
	public static String getScriptContent() throws IOException, URISyntaxException {
        URI rScriptUri = RUtils.class.getClassLoader()
            .getResource("script.R")
            .toURI();
        Path inputScript = Paths.get(rScriptUri);
        return Files.lines(inputScript)
            .collect(Collectors.joining());
    }
}
