package data;

public class Term {
	private int frequency;
	private String description;
	
	public Term(String description, int frequency){
		this.description = description;
		this.frequency = frequency;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
