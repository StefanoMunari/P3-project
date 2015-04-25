package puzzle;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.Serializable;


public class Puzzle implements Serializable{
/****FIELDS****/
	private List<Piece> pieces;
	private int rows=0;
	private int columns=0;
	
	static public class Piece implements Serializable{
	/****FIELDS****/
		private String id;
		private String value;
		private Map<String, String> cardinal_points= new HashMap<String, String>();
		public static final int NUMBER_OF_FIELDS= 6;
		public static final String EMPTY_ID= "VUOTO";
	/****METHODS****/
		public Piece(final ArrayList<String> values) throws InvalidParameterSizeException
		{
			id=values.get(0);
			value=values.get(1);
			cardinal_points.put("north", values.get(2));
			cardinal_points.put("east", values.get(3));
			cardinal_points.put("south", values.get(4));
			cardinal_points.put("west", values.get(5));
		}
		public Piece(final Piece piece)
		{
				this.id=piece.getId();
				this.value=piece.getValue();
				this.cardinal_points.put("north", piece.getNorth());
				this.cardinal_points.put("east", piece.getEast());
				this.cardinal_points.put("south", piece.getSouth());
				this.cardinal_points.put("west", piece.getWest());
		}
		public String getValue(){
			return value;
		}
		public String getId(){
			return id;
		}
		public String getNorth(){
			return cardinal_points.get("north");
		}
		public String getEast(){
			return cardinal_points.get("east");
		}
		public String getSouth(){
			return cardinal_points.get("south");
		}
		public String getWest(){
			return cardinal_points.get("west");
		}
		public boolean checkCardinalPoint(final String key,final String compare_value){
			return cardinal_points.get(key).equals(compare_value);
		}
	}
/****METHODS****/	
	public Puzzle() {
		pieces=new ArrayList<Piece>();
	}
	
	public void add(final Piece p){
		pieces.add(p);
	}
	public Piece getPiece(final int index){
		return new Piece(pieces.get(index));//ritorna una copia, così non violo encapsulation
	}
	public int getRows(){
		return rows;
	}
	public int getColumns(){
		return columns;
	}
	public List<Piece> toList() {
		return new ArrayList<Piece>(pieces);//ritorno una copia
	}
	public void setPieces(final List<Piece> pieces){//come da nome permette di sostituire i pezzi del puzzle
		this.pieces = pieces;
		this.setDimensions();//risetto anche le dimensioni perchè potrebbero essere diverse
	}
	
	public String toString(){
		String values="";
		for(int index=0; index < pieces.size(); ++index)
			values+=pieces.get(index).getValue();
		return values;
	}
	
	public String toMatrix(){
		String matrix="";
		for(int row_index=0; row_index < rows;++row_index){
			for(int column_index=0; column_index < columns; ++column_index)
				matrix+=(pieces.get(row_index*columns+column_index)).getValue();
			if(row_index != rows-1)
				matrix+="\n";
		}
		return matrix;
	}
	public void setDimensions()
	{
		if(columns!=0 || rows!=0)
			columns= rows =0;
		for(final Piece p : pieces)
		{
			if((p.cardinal_points.get("north")).equals(Piece.EMPTY_ID))
				++columns;
			if((p.cardinal_points.get("west")).equals(Piece.EMPTY_ID))
				++rows;
		}
	}		
}