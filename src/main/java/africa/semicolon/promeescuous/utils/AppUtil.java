package africa.semicolon.promeescuous.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AppUtil {
	
	private static final String TEMPLATE_FILE_PATH  = "C:\\Users\\USER\\IdeaProjects\\promeescuous\\promiscuous\\src\\main\\resources\\templates\\index.html";
	
	
	public static String getMailTemplate() throws URISyntaxException, IOException {
		Path filePath = Paths.get(new URI(TEMPLATE_FILE_PATH));
		List<String> templateFileLines = Files.readAllLines(filePath);
		return String.join("", templateFileLines);
	}
	
	public static String generateActivationLink(){
		return "";
	}
}
