package algorithm;

import java.util.ArrayList;
import java.util.List;

import puzzle.Puzzle;
import puzzle.Puzzle.Piece;

public class PuzzleSort{
/****FIELDS****/
	private List<Piece> result;
	private List<List<Piece>> columns;
	private int puzzle_size=0;

/****METHODS****/	
/*
PRE_PuzzleSort= 
	(puzzle é Puzzle risolvibile che contiene pezzi con punti cardinali sensati), 
	(result e columns sono 2 oggetti indefiniti), 
	(puzzle_size contiene gli n elementi del puzzle da ordinare con n=0)
*/
	public PuzzleSort(final Puzzle puzzle) {
		this.result= puzzle.toList();
		this.columns=new ArrayList<List<Piece>>();
		this.puzzle_size= result.size();
	}
/*
POST_PuzzleSort= 
	(result contiene la lista, eventualmente vuota, di n pezzi), 
	(columns contiene una lista di liste di pezzi vuote), 
	(puzzle_size contiene gli n elementi di result da ordinare con n >=0)



PRE_sort =
	(result contiene la lista di pezzi, eventualmente vuota, eventualmente da ordinare),
	(columns contiene una lista di liste di pezzi vuote), 
	(puzzle_size contiene gli n elementi di result da ordinare con n >=0)
*/
	public void sort(){
		if(puzzle_size > 1)//se <=1 -> ordinato
		{
			this.initializeColumns(rowSort(fetchFirstElement(buildFirstRow(new ArrayList<Piece>()))));
			this.sortColumns();
			this.mergeColumns();
		}
	}
/*
POST_sort=
	(result contiene la lista di n pezzi ordinati con n>=0),
	(columns contiene una lista di liste di pezzi vuota), 
	(puzzle_size contiene gli n elementi di result da ordinare con n >=0)



PRE_buildFirstRow= 
	firstRow è una lista di pezzi vuota,
	columns contiene una lista di liste di pezzi vuota,
	result è una lista di n pezzi con n>=2
*/
	private ArrayList<Piece> buildFirstRow(ArrayList<Piece> firstRow){
		int i=0;
		while(i < result.size())
		{
			if(result.get(i).checkCardinalPoint("north",Piece.EMPTY_ID))
			{
				columns.add(new ArrayList<Piece>());//aggiungo una nuova colonna alla lista delle colonne
				firstRow.add(result.get(i));
				result.remove(i);//rimuovo dalla lista iniziale il nodo appena inserito in modo da non doverlo riconsiderare alla prossima iterazione
			}
			else
				++i;
		}
		return firstRow;
	}
/*
POST_buildFirstRow =
	firstRow è definita come una lista di el con #elementi_firstRow >1 , el è definito come istanza di Piece con le seguenti proprietà: 
		(el ∈ firstRow se e solo se el.north == Piece.EMPTY_ID) , 
		(∃ un unico el ∈ firstRow t.c. el.west == Piece.EMPTY_ID),
NB:la seconda proprietà deriva direttamente da PRE_PuzzleSort
	columns contiene una lista con #elementi_firstRow liste di pezzi vuote,
	result è una lista di n - #elementi_firstRow con n>=0

 
PRE_fetchFirstElement == POST_buildFirstRow
*/
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
/* 
POST_fetchFirstElement =
	firstRow è una lista di el con le proprietà elencate in POST_buildFirstRow,
	il primo el di firstRow è ordinato


PRE_firstRowSort == POST_fetchFirstElement
*/
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
/*
POST_firstRowSort=
	(1<=index<#elementi_firstRow),
	(firstRow contiene tutti e soli el t.c. firstRow[index-1].east == firstRow[index]) -> (firstRow è ordinato)


PRE_initializeColumns == POST_firstRowSort
*/
	private void initializeColumns(final ArrayList<Piece> firstRow){
		int index=0;
		for(final Piece p : firstRow){
			columns.get(index).add(p);
			++index;
		}
	}
/*
POST_initializeColumns= 
	(1<=index<#elementi_firstRow, columns[index] esiste) -> (per ogni index vale che columns[index] è ordinato in quanto contiene un solo elemento)
	gli elementi di columns hanno le stesse proprietà di firstRow in PRE_initializeColumns


PRE_sortColumns= 
	result contiene n elementi con n >=0, (1<=index<#elementi_columns, columns[index] esiste) -> (per ogni index vale che columns[index] è ordinato in quanto contiene una lista con un solo elemento)
*/
	private void sortColumns(){
		for(List<Piece> p : columns){//reference perchè voglio fare side-effect
			for(int p_index=0;p_index < p.size(); ++p_index){
				int r_index=0;
				while(r_index < result.size() && !(p.get(p_index).checkCardinalPoint("south",Piece.EMPTY_ID)))
				{
					if((p.get(p_index)).checkCardinalPoint("south",result.get(r_index).getId()))
					{
						p.add(result.get(r_index));
						result.remove(r_index);//così rendo la lista iniziale vuota
					}
					else
						++r_index;
				}
			}
		}
	}
/*
POST_sortColumns=
	columns contiene #elementi_columns >1, 
	columns[index] contiene n/#elementi_columns elementi,
	(1<=v_index < n/#elementi_columns),
	per ogni index e per ogni v_index vale:
		(columns.[index].[v_index-1].south == columns.[index].[v_index]) --> ogni suo elemento è ordinato, 
	result è vuoto

PRE_mergeColumns == POST_sortColumns
*/
	private void mergeColumns(){
		int column_index=0;
		for(int row_index=0; row_index < columns.get(column_index).size(); ++row_index)//scorro una colonna
		{
			for(;column_index < columns.size();++column_index)//scorro una riga
				result.add(columns.get(column_index).get(row_index));
			column_index=0;
		}
		columns.clear();
	}
/*
POST_mergeColumns=
	result contiene n elementi ed ogni suo elemento è ordinato,
	columns è una lista di liste di pezzi vuota
*/
	public List<Piece> toList(){
		return new ArrayList<Piece>(result);//ritorno una copia, non voglio che modifiche esterne provochino side-effect sui campi dati
	}
}