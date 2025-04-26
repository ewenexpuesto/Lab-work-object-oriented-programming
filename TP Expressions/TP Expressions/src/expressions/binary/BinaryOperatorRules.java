package expressions.binary;

/**
 * Binary Operator Rule enum.
 * Defines various constants that can be used to obtain rules applicable to
 * various binary operators such as +, -, *, / or ^
 * The rules provided by this enum concerns
 * <ul>
 * 	<li>The priority of binary operations with {@link #priority()}:
 * 		<ul>
 * 			<li>Assignement: "=": priority 0</li>
 * 			<li>Additions and Subtractions: "+, -": priority 1</li>
 * 			<li>Multiplications and Divisions: "*, /": priority 2</li>
 * 			<li>Power: "^": priority 3</li>
 * 		</ul>
 * 	</li>
 * 	<li>The possibility for an operator to be used with a single operand
 * 	(such as "+2" or "-a") with </li>
 * 	<li>The string prepresentation of the operator with {@link #toString()}</li>
 * </ul>
 * @author davidroussel
 */
public enum BinaryOperatorRules
{
	/**
	 * Assignment expression rule
	 */
	ASSIGNMENT,
	/**
	 * Addition expression rule
	 */
	ADDITION,
	/**
	 * Subtraction expression rule
	 */
	SUBTRACTION,
	/**
	 * Multiplication expression rule
	 */
	MULTIPLICATION,
	/**
	 * Division expression rule
	 */
	DIVISION,
	/**
	 * Power expression rule
	 */
	POWER;

	/**
	 * String representation of this binary expression type
	 * @return a String representing this operator
	 * @throws AssertionError for unexpected enum value
	 */
	@Override
	public String toString() throws AssertionError
	{
		switch (this)
		{
			case ASSIGNMENT:
				return new String("=");
			case ADDITION:
				return new String("+");
			case SUBTRACTION:
				return new String("-");
			case MULTIPLICATION:
				return new String("*");
			case DIVISION:
				return new String("/");
			case POWER:
				return new String("^");
			default:
				throw new AssertionError("Unexpected value: " + this);
		}
	}

	/**
	 * Priority of this type of expression
	 * @return the priority of this type of expression
	 * @throws AssertionError for unexpected enum value
	 */
	public int priority() throws AssertionError
	{
		switch (this)
		{
			case ASSIGNMENT:
				return 0;
			case ADDITION:
			case SUBTRACTION:
				return 1;
			case MULTIPLICATION:
			case DIVISION:
				return 2;
			case POWER:
				return 3;
			default:
				throw new AssertionError("Unexpected value: " + this);
		}
	}


	/**
	 * Indicate if the current operator can have only one operand
	 * (such as "-2" or "+a")
	 * @return true if the operator can have only one operand, false otherwise
	 * @throws AssertionError for unexpected enum value
	 */
	public boolean hasUnary() throws AssertionError
	{
		switch (this)
		{
			case ASSIGNMENT:
			case MULTIPLICATION:
			case DIVISION:
			case POWER:
				return false;
			case ADDITION:
			case SUBTRACTION:
				return true;
			default:
				throw new AssertionError("Unexpected value: " + this);
		}
	}

	/*
	 * Enums have a default final hashCode method which can not be overridden
	 * but can be used
	 */
}
