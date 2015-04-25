package client;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;

import algorithm.PuzzleSort;
import algorithm.PuzzleSortInterface;
import file.InputText;
import file.OutputText;
import puzzle.Puzzle;
import puzzle.Puzzle.Piece;

import java.rmi.RemoteException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import puzzle.InvalidParameterSizeException;
import file.InputException;
import server.InvalidArgumentException;
import algorithm.InterruptNotifyException;

class PuzzleSolverClient{
	
	public static void main(String[] args){
	try{
		if(args.length != 3)
			throw new InvalidArgumentException("EXCEPTION: the number of arguments is not valid");
		else
		{
			final String servername= "rmi://localhost/"+args[2];
			final String inputFile = args[0];
		    final String OutputTextFile = args[1];
		    final Path inputPath = Paths.get(inputFile);
		    final Path OutputTextPath = Paths.get(OutputTextFile);
		    final InputText input= new InputText(StandardCharsets.UTF_8);
		    final OutputText output= new OutputText(new OpenOption[] { CREATE, APPEND },StandardCharsets.UTF_8);
		    final String separator="\t";
		    final String regEx="^[A-Za-z0-9\\s_.,:;!)(-?\"\'ÀÁÈÉÌÍÒÓÙÚàáèéìíòóùú]{1}$";
		    final int position= 1;
		    
		    //LETTURA INIZIALE DA FILE DI INPUT
		    input.readContent(inputPath,separator);
		    if(!input.isEmpty() && !input.match(regEx,position))//se il file non è vuoto -> eseguo il controllo
		    	throw new InputException("EXCEPTION: The input format is invalid!");
		    else
		    {  
			    //CREAZIONE E RIEMPIMENTO DI PUZZLE
			    final Puzzle puzzle= new Puzzle();
			    for(int index=0; index < input.size(); ++index)
			    {
			    	if(input.getLine(index).size() != Piece.NUMBER_OF_FIELDS)
			    		throw new InvalidParameterSizeException("EXCEPTION: The line "+(index+1)+" is invalid!");//index+1 perchè negli editor di testo le righe sono indicate a partire da 1, così risulterà più facile correggere il file di input
			    	else
			    		puzzle.add( new Piece(input.getLine(index)));
			    }
			    puzzle.setDimensions();
			    
		   		PuzzleSortInterface dictionary= (PuzzleSortInterface) Naming.lookup(servername);
			    
			    //ORDINAMENTO ED INSERIMENTO IN PUZZLE DEI PEZZI ORDINATI. SI ASSUME CHE puzzle SIA RISOLVIBILE
			    puzzle.setPieces(dictionary.sort(puzzle));

			    //SCRITTURA FINALE SU FILE DI OUTPUT
			    output.writeContent(OutputTextPath,puzzle.toString());
			    output.writeContent(OutputTextPath,"\n\n");
			    output.writeContent(OutputTextPath,puzzle.toMatrix());
			    output.writeContent(OutputTextPath,"\n\n");
			    output.writeContent(OutputTextPath,puzzle.getRows()+"x"+puzzle.getColumns());
			  }
			}
		}
	    catch(InterruptNotifyException e)
	    {
	    	System.err.println(e);
	    }
	    catch(NoSuchObjectException e)
        {
            System.err.println("EXCEPTION: there is no such object in the RMI registry");
            System.err.println(e.getMessage());		            	
        }
	    catch(RemoteException e)
	   	{//quando non esiste un server attivo, oppure la connessione viene troncata
            System.err.println("EXCEPTION: error while connecting to the server");
            System.err.println(e.getMessage());
    	}
    	catch (NotBoundException e)
    	{//quando cerco nel registro RMI un nome che non esiste
            System.err.println("EXCEPTION: tried to lookup or unbind in the registry a name that has no associated binding");
            System.err.println(e.getMessage());
        }
		catch(final InputException e)
	    {
		   System.err.println(e);
	    }
		catch(final InvalidParameterSizeException e)
	    {
			System.err.println(e);
		}
		catch(InvalidArgumentException e)
        {
            System.err.println(e);
        }
        catch(Exception e){//qualsiasi tipo di altra eccezione non catturata precedentemente
            System.err.println("EXCEPTION: Generic exception");
            System.err.println(e.getMessage());
        }
    }
}
