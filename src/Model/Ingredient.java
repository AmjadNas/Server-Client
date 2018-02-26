package Model;




public class Ingredient implements Comparable<Ingredient>{
	
	private int ingredientNumber;
	private String ingredientName;
	private int ingredientQuantity;
	
	public Ingredient(int ingredientNumber, String ingredientName, int ingredientQuantity){
		this.ingredientNumber = ingredientNumber;
		this.ingredientName = ingredientName;
		this.ingredientQuantity = ingredientQuantity;
	}
	
	/**
	 * @return the partNumber
	 */
	public int getIngredientNumber() {
		return ingredientNumber;
	}
	/**
	 * @param ingredientNumber the partNumber to set
	 */
	public void setIngredientNumber(int ingredientNumber) {
		this.ingredientNumber = ingredientNumber;
	}
	/**
	 * @return the partName
	 */
	public String getIngredientName() {
		return ingredientName;
	}
	/**
	 * @param ingredientName the partName to set
	 */
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	/**
	 * @return the partQuantity
	 */
	public int getIngredientQuantity() {
		return ingredientQuantity;
	}
	/**
	 * @param ingredientQuantity the partQuantity to set
	 */
	public void setIngredientQuantity(int ingredientQuantity) {
		this.ingredientQuantity = ingredientQuantity;
	}

	@Override
	public int compareTo(Ingredient o) {
		return this.ingredientName.compareTo(o.ingredientName);
	}	
}
