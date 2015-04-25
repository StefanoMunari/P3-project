package file;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class OutputText extends Text {
/****FIELDS****/
	private OpenOption[] options;
/****METHODS****/
	public OutputText(final OpenOption[] options,final Charset charset){
		super(charset);
		this.options=options;
	}
	public void writeContent(final Path file,final String content) {
		try (BufferedWriter writer = Files.newBufferedWriter(file, this.getCharset(), options)) {
			writer.write(content);
		} 
		catch (IOException e) {
			System.err.println(e);
		}
	}
}
