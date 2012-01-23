package S4;


import lombok.Data;

@Data
public class S4Data0 {
	public int i;
	/*1: CreateUnresolvedMethod() :1*/
	private int method() implements IS4{
		i = createdMethod()
		return i;
	}
	/*:1:*/
}
