package file;

import java.nio.charset.Charset;

public abstract class Text {
/****FIELDS****/
	private final Charset charset;//non voglio che sia modificabile una volta costruito
/****METHODS****/
	Text(final Charset charset){
		this.charset=charset;
	}
	public Charset getCharset(){
		return charset;
	}
}