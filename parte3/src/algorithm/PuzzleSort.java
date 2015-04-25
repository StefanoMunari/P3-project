package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;
import java.util.concurrent.Executors;


import puzzle.Puzzle;
import puzzle.Puzzle.Piece;

public class PuzzleSort extends UnicastRemoteObject implements PuzzleSortInterface{
/****FIELDS****/
	private List<Column> columns;
	private AtomicInteger monitor;
	private ExecutorService tPoolCached;

/****METHODS****/	

	public PuzzleSort() throws RemoteException{
		this.columns= new ArrayList<Column>();
		this.monitor= new AtomicInteger(0);
	}

	//SORTING METHODS
	@Override
	public synchronized List<Piece> sort(final Puzzle puzzle) throws RemoteException{
		this.tPoolCached= Executors.newCachedThreadPool();
		List<Piece> result= puzzle.toList();
		int puzzle_size= result.size();
		if(puzzle_size > 1)//se <=1 -> ordinato
		{
			this.sortColumns(rowSort(fetchFirstElement(buildFirstRow(new ArrayList<Piece>(),result))),result);//passo un reference non una copia
			return this.mergeColumns(result);//ritorno un oggetto serializable, quindi una copia
		}
		else
			return result;//ritorno un oggetto serializable, quindi una copia
	}

	private ArrayList<Piece> buildFirstRow(ArrayList<Piece> firstRow, final List<Piece> result){
		int i=0;
		while(i < result.size())
		{
			if(result.get(i).checkCardinalPoint("north",Piece.EMPTY_ID))
			{
				columns.add(new Column(result,monitor));//aggiungo una nuova colonna alla lista delle colonne
				firstRow.add(result.get(i));
				result.remove(i);//rimuovo dalla lista iniziale il nodo appena inserito in modo da non doverlo riconsiderare alla prossima iterazione
			}
			else
				++i;
		}
		return firstRow;
	}

	private ArrayList<Piece> fetchFirstElement(ArrayList<Piece> firstRow){
		boolean found=false;
		for(int index=0;!found && index < firstRow.size(); ++index)
			if(firstRow.get(index).checkCardinalPoint("west",Piece.EMPTY_ID))
			{	
				//swap
				final Piece p=firstRow.get(index);
				firstRow.set(index,firstRow.get(0));
				firstRow.set(0,p);
				found=true;
			}
		return firstRow;
	}

	private ArrayList<Piece> rowSort(ArrayList<Piece> firstRow){
		for(int sorted_index=0; sorted_index < firstRow.size()-1;++sorted_index)
			for(int match_index=1;match_index < firstRow.size(); ++match_index)
				if(firstRow.get(sorted_index).checkCardinalPoint("east", firstRow.get(match_index).getId()))
				{
					final Piece p=firstRow.get(sorted_index+1);//swap
					firstRow.set(sorted_index+1,firstRow.get(match_index));
					firstRow.set(match_index,p);
				}
		return firstRow;
	}

	private void sortColumns(final ArrayList<Piece> firstRow,final List<Piece> result) throws RemoteException
	{
		int index=0;
		for(final Piece p : firstRow){
			columns.get(index).add(p);//definisco il primo elemento della colonna
			tPoolCached.execute(columns.get(index));//faccio partire il thread che ordina la colonna
			++index;
		}
		try
		{
			synchronized(monitor)
			{
				while(monitor.get() != columns.size() )//controllo che tutti i thread abbiano ordinato la relativa colonna
				{
					monitor.wait();
				}
			}
		}
		catch(InterruptedException e)
		{
			System.err.println("EXCEPTION: thread interrupted");
			System.err.println(e.getMessage());
			throw new InterruptNotifyException("EXCEPTION: thread interrupted");
		}
		finally{
			result.clear();//svuoto la lista di pezzi disordinati
			monitor= new AtomicInteger(0);//reinizializzo monitor per un futuro utilizzo
			tPoolCached.shutdown();
		}
	}

	private List<Piece> mergeColumns(final List<Piece> result) throws RemoteException{
		int column_index=0;
		for(int row_index=0; row_index < columns.get(column_index).size(); ++row_index)//scorro una colonna
		{
			for(;column_index < columns.size();++column_index)//scorro una riga
				result.add(columns.get(column_index).get(row_index));
			column_index=0;
		}
		columns.clear();//elimino tutti gli oggetti di columns in modo da poterlo riutilizzare
		return result;
	}
}