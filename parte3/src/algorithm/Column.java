package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import puzzle.Puzzle.Piece;

public class Column implements Runnable{
	private List<Piece> column;
	private AtomicInteger monitor;
	private List<Piece> sharedPieces;
	
	Column(List<Piece> sharedPieces, AtomicInteger monitor)
	{
		column= new ArrayList<Piece>();
		this.monitor= monitor;
		this.sharedPieces= sharedPieces;
	}
	
	public void add(final Piece p)
	{
		column.add(p);
	}
	
	public Piece get(final int index)
	{
		return new Piece(column.get(index));
	}
	
	public int size()
	{
		return column.size();
	}
	
	public void run()
	{
		for(int p_index=0;p_index < column.size(); ++p_index)
		{
			int r_index=0;
			while(r_index < sharedPieces.size() && !(column.get(p_index).checkCardinalPoint("south",Piece.EMPTY_ID)))
			{
				if((column.get(p_index)).checkCardinalPoint("south",sharedPieces.get(r_index).getId()))
				{
					column.add(sharedPieces.get(r_index));
				}
				r_index++;
			}
		}
		synchronized(monitor)
		{
			monitor.incrementAndGet();
			monitor.notifyAll();
		}
	}
}
