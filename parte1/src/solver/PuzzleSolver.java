package solver;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import algorithm.PuzzleSort;
import file.InputException;
import file.InputText;
import file.OutputText;
import puzzle.InvalidParameterSizeException;
import puzzle.Puzzle;
import puzzle.Puzzle.Piece;

public class PuzzleSolver {
	public static void main(String[] args) {
		final String inputFile = args[0];
	    final String OutputTextFile = args[1];
	    final Path inputPath = Paths.get(inputFile);
	    final Path OutputTextPath = Paths.get(OutputTextFile);
	    final InputText input= new InputText(StandardCharsets.UTF_8);
	    final OutputText output= new OutputText(new OpenOption[] { CREATE, APPEND },StandardCharsets.UTF_8);
	    final String separator="\t";
	    final String regEx="^[A-Za-z0-9\\s_.,:;!?\"\'ÀÁÈÉÌÍÒÓÙÚàáèéìíòóùú]{1}$";
	    final int position= 1;
	    
	    //LETTURA INIZIALE DA FILE DI INPUT
	    input.readContent(inputPath,separator);
	    try{
		    if(!input.isEmpty() && !input.match(regEx,position))//se il file non è vuoto -> eseguo il controllo
		    	throw new InputException("The input format is invalid!");
		    else
		    {  
			    //CREAZIONE E RIEMPIMENTO DI PUZZLE
			    final Puzzle puzzle= new Puzzle();//è un reference final ad un oggetto Puzzle
			    for(int index=0; index < input.size(); ++index)
			    {
			    	if(input.getLine(index).size() != Piece.NUMBER_OF_FIELDS)
			    		throw new InvalidParameterSizeException("The line "+(index+1)+" is invalid!");//index+1 perchè negli editor di testo le righe sono indicate a partire da 1, così risulterà più facile correggere il file di input
			    	else
			    		puzzle.add( new Piece(input.getLine(index)));
			    }
		
			    puzzle.setDimensions();
			    
			    //COPIA E ORDINAMENTO DEI PEZZI, SI ASSUME CHE puzzle SIA RISOLVIBILE
			    final PuzzleSort dictionary = new PuzzleSort(puzzle);
			    dictionary.sort();
			    
			    //INSERIMENTO IN PUZZLE DEI PEZZI ORDINATI
			    puzzle.setPieces(dictionary.toList());

			    //SCRITTURA FINALE SU FILE DI OUTPUT
			    output.writeContent(OutputTextPath,puzzle.toString());
			    output.writeContent(OutputTextPath,"\n\n");
			    output.writeContent(OutputTextPath,puzzle.toMatrix());
			    output.writeContent(OutputTextPath,"\n\n");
			    output.writeContent(OutputTextPath,puzzle.getRows()+"x"+puzzle.getColumns());
			  }
	   }
	   catch(final InputException e)
	    {
		   System.out.println(e);
	    }
		catch(final InvalidParameterSizeException e)
	    {
			System.out.println(e);
		}
	}
}
