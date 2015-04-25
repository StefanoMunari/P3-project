package file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.ArrayList;

public class InputText extends Text{
/****FIELDS****/
	private LinkedList<ArrayList<String>> lines;
/****METHODS****/
	public InputText(final Charset charset){
		super(charset);
		lines= new LinkedList<ArrayList<String>>();
	}
	
	public void readContent(final Path inputPath,final String separator){
		try (BufferedReader reader = Files.newBufferedReader(inputPath,this.getCharset())) 
		{
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] l =line.split(separator);
				ArrayList<String> aux=new ArrayList<String>();
				for(String s : l)
					aux.add(s);
				lines.add(aux);
			}
		}
		catch (final IOException e) 
		{
			System.err.println(e);
		}
	}
	public boolean match(final String regex,final int position)
	{
		try{
			int index=1;
			for(final ArrayList<String> record : lines)
			{
				if(record.size() <= position)
					throw new InvalidIndexPosition("The position "+position+" doesn't exists in line "+index+"!");
				else
				{
					if(!record.get(position).matches(regex))
					{
						System.err.println("InvalidCharacter: "+record.get(position));
						return false;
					}
				}
				++index;
			}
			return true;
		}
		catch(final InputException e){
			System.out.println(e);
		}
		return false;
	}
	public int size() {
		return lines.size();
	}
	public ArrayList<String> getLine(final int record_index) {
		return new ArrayList<String>(lines.get(record_index));//ritorno una copia e NON un reference
	}
	public boolean isEmpty(){
		return lines.isEmpty();
	}
}
