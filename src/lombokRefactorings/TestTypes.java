package lombokRefactorings;


public enum TestTypes {
	BEFORE("before"),
	DELOMBOKED("delomboked"),
	REFACTORED("refactored"),
	DELOMBOKED_THEN_REFACTORED("delombokedThenRefactored"),
	REFACTORED_THEN_DELOMBOKED("refactoredThenDelomboked"),
	;
	
	private final String name;

	private TestTypes(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
