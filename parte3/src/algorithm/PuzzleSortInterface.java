package algorithm;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.List;

import puzzle.Puzzle;
import puzzle.Puzzle.Piece;

public interface PuzzleSortInterface extends Remote{
	public List<Piece> sort(final Puzzle puzzle) throws RemoteException;
}