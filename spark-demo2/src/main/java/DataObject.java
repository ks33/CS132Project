package main.java;

import java.util.ArrayList;
import java.util.List;

public class DataObject {

	private int tenure;

	private List<String> list ;

	public DataObject(int tenure) {

		this.tenure = tenure;

		this.list = new ArrayList<String>();
		this.list.add("Int1");

	}

	public void settenure(int tenure) {
		this.tenure = tenure;
	}

	public int gettenure() {
		return this.tenure;
	}

	@Override
	public String toString() {
		return new StringBuffer(" First Name : ").append(" tenure : ")
				.append(this.tenure).append(" "+this.list).toString();
	}
}