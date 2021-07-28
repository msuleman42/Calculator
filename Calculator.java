
// Munir Suleman
// A simple calculator that takes input as a string.

public class Calculator {
	public float memory = 0;
	public float[] history = new float[10]; 
	
	public static void main(String[] args) {
		Calculator calc = new Calculator();
		
		// Enter expression as string with spaces between each operator/operand
		System.out.println(calc.evaluate("2 + 2 + 6 + 4 + 12 + 2 + 10 - 3"));
		// invalid expressions return Float.MIN_VALUE
	}
	
	public Calculator() {
		
	}
	
	public float evaluate (String expression) {
		// main method that separates between tasks
		float answer = 0;
		char firstChar = expression.charAt(0);
		// if the first character is an operation then rebuild the string as the memory value + the rest of the expression
		if(firstChar == '+' || firstChar == '-' || firstChar == '/' || firstChar == '*') {
			expression = String.valueOf(memory) + " " + expression;
		}
		// if first character is ( then go to brackerExpression method else continue
		if(firstChar == '(') {
			answer = bracketExpression(expression);
		}
		else {
			// check the expression is in the form or "number operator number....", and are valid numbers
			expression = checkExpression(expression);
			// split the string expression into a string array
			String[] exp = expression.split(" ");
			// if the string array is equal to 3 then it would only be one operation. if more than goes to large Expression
			if(exp.length == 3) {
				// if the operation is / and the operand after is 0 then returns the float.min_value
				// else feed expression into oneExpression method
				if(exp[1].equals("/") && exp[2].equals("0")) {
					answer = Float.MIN_VALUE;
				}
				else {
					answer = oneExpression(exp);
				}
			}
			else {
				answer = largeExpression(exp);
			}
		}
		// if the answer is invalid (i.e. float.min_value) then the number does not go into the history
		if(answer == Float.MIN_VALUE) {
			return answer;
		}
		else {
			setHistory(answer);
			return answer;
		}
	}
	
	public float oneExpression(String[] exp) {
		// method allows the string array expression that only has a length of 3 to be calculated
		float answer1=0;
		if(exp[1].equals("+")) {
			answer1 = Float.parseFloat(exp[0]) + Float.parseFloat(exp[2]);
		}
		else if(exp[1].equals("-")) {
			answer1 = Float.parseFloat(exp[0]) - Float.parseFloat(exp[2]);
		}
		else if(exp[1].equals("/")) {
			answer1 = Float.parseFloat(exp[0]) / Float.parseFloat(exp[2]);
		}
		else if(exp[1].equals("*")) {
			answer1 = Float.parseFloat(exp[0]) * Float.parseFloat(exp[2]);
		}
		else
			answer1 = Float.MIN_VALUE;
		return answer1;
	}
	
	public float bracketExpression(String exp) {
		// splits the expression 3: first expression, the operator, second expression
		// then feeds the individual expressions into the oneExpression to solve before forming the final
		// expression and solving that
		float ans = 0;
		int check = exp.indexOf(" ");
		if(check == -1) {
			return Float.MIN_VALUE;
		}
		else {
			int firstOpen = exp.indexOf("(");
			int secondOpen = exp.indexOf("(", firstOpen + 1);
			int firstClose = exp.indexOf(")");
			int secondClose = exp.indexOf(")",firstClose + 1);
			String answer1 = exp.substring(firstOpen+1,firstClose);
			String answer2 = exp.substring(firstClose+2,secondOpen-1);
			String answer3 = exp.substring(secondOpen+1,secondClose);
			String[] noBracExpression = {answer1,answer2,answer3};
			
			String[] exp1 = noBracExpression[0].split(" ");
			String[] exp2 = noBracExpression[2].split(" ");
			float ans1 = oneExpression(exp1);
			float ans2 = oneExpression(exp2);
			
			String[] finalExp = {Float.toString(ans1),noBracExpression[1],Float.toString(ans2)};
			ans = oneExpression(finalExp);
			return ans;
		}
	}

	public float getCurrentValue() {
		// gets the latest answer
		return history[0];
	}
	
	public float getMemoryValue() {
		// returns memory
		return memory;
	}
	
	public void setMemoryValue (float memval) {
		// sets the memory value to the input
		memory = memval;
	}
	
	public void clearMemory() {
		//sets memory to 0
		memory = 0;
	}
	
	public float getHistoryValue(int index) {
		// shows the answer in the history at the particular slot
		float hisIndex = history[index];
		return hisIndex;
	}
	
	public void setHistory(float value) {
		// makes the newest answer in the 0 slot of the array and shifts the other by one slot
		for(int i = history.length - 1; i > 0; i--) {
			history[i] = history[i-1];
		}
		history[0] = value;
	}
	
	public void disHistory() {
		//displays the last 10 answers
		for(int i = 0; i < history.length; i++) {
			System.out.print(history[i] + " ");
		}
	}
	
	public String checkExpression(String expression) {
		//checks if expression is valid
		//splits into string array
		String[] exp = expression.split(" ");
		//if string array length is less than 3 then its invalid
		if(exp.length < 3) {
			return "1 / 0";
		}
		else {
			//form a boolean array that checks to make sure the even numbered elements in the array are float numbers
			//and if the odd numbered elements in the array are operators
			boolean[] correctArray = new boolean[exp.length];
			for(int i = 0; i < correctArray.length; i++) {
				if(i % 2 == 0) {
					correctArray[i] = exp[i].matches("[-+]?[0-9]*\\.?[0-9]+");
				}
				else {
					correctArray[i] = exp[i].matches("[+-/*]");
				}
			}
			//feed the boolean array to the boolChecker method
			boolean correct = boolChecker(correctArray);
			//if the return from that method is true then its a valid expression. otherwise its invalid
			if(correct == true)
				return expression;
			else
				return "1 / 0";
		}
	}
	
	public boolean boolChecker (boolean[] values) {
		//checks if array of booleans is all true. If any are false then it returns false
	    for (boolean value : values) {
	        if(!value)
	            return false;
	    }
	    return true;
	}
	
	public float largeExpression(String[] exp) {
		//feeds string array of expression to the indexString method. Allows the use of indexOf to find the
		//placement of the operator
		String indexCheckerString = indexString(exp);
		//find the index of the / to do all division within the expression first
		int k = indexCheckerString.indexOf("/");
		//while there is / is the expression run this
		while(k != -1) {
			//Create an expression array that holds the operand, operator, operand and feed this to oneExpression method
			String[] tempExp = {exp[k-1], exp[k], exp[k+1]};
			float tempAns = oneExpression(tempExp);
			//Replaces the operand in the k-1 slot to be the answer to the above expression
			exp[k-1] = Float.toString(tempAns);
			//feeds full expression to removeElementArray so it removes the already completed operation
			exp = removeElementArray(exp,k);
			//Reforms the string to find the indexOf of the operator
			indexCheckerString = indexString(exp);
			k = indexCheckerString.indexOf("/");
		}
		//while there is * is the expression run this
		k = indexCheckerString.indexOf("*");
		while(k != -1) {
			String[] tempExp = {exp[k-1], exp[k], exp[k+1]};
			float tempAns = oneExpression(tempExp);
			exp[k-1] = Float.toString(tempAns);
			exp = removeElementArray(exp,k);
			indexCheckerString = indexString(exp);
			k = indexCheckerString.indexOf("*");
		}
		//while there is - is the expression run this
		k = indexCheckerString.indexOf("-");
		while(k != -1) {
			String[] tempExp = {exp[k-1], exp[k], exp[k+1]};
			float tempAns = oneExpression(tempExp);
			exp[k-1] = Float.toString(tempAns);
			exp = removeElementArray(exp,k);
			indexCheckerString = indexString(exp);
			k = indexCheckerString.indexOf("-");
		}
		//while there is + is the expression run this
		k = indexCheckerString.indexOf("+");
		while(k != -1) {
			String[] tempExp = {exp[k-1], exp[k], exp[k+1]};
			float tempAns = oneExpression(tempExp);
			exp[k-1] = Float.toString(tempAns);
			exp = removeElementArray(exp,k);
			indexCheckerString = indexString(exp);
			k = indexCheckerString.indexOf("+");
		}
		//final answer in float form
		float finAns = Float.parseFloat(exp[0]);
		return finAns;
	}
	
	public String indexString(String[] exp) {
		//method that makes any number in the string expression an A so it only takes up one index, to allow for an
		//indexOf method to be used when finding the operator
		//makes a string array of the same length as the expression
		String[] indexCheckerArray = new String[exp.length];
		//make each elements in the new string array the same as the original expression
		for(int k = 0; k < exp.length; k++) {
			indexCheckerArray[k] = exp[k];
		}
		//make every other elements in the string array an A. Only leaves A's and operators
		for(int i = 0; i < indexCheckerArray.length; i+=2) {
			indexCheckerArray[i] = "A";
		}
		//Creates a string buffer to go from string array to string
		StringBuffer indexChecker = new StringBuffer();
		for(int j = 0; j < indexCheckerArray.length; j++) {
			indexChecker.append(indexCheckerArray[j]);
		}
		String indexCheckerString = indexChecker.toString();
		return indexCheckerString;
	}
	
	public String[] removeElementArray(String[] array, int index) {
		//method to remove two elements in the array that are no longer needed
		//newstring array that has a shorter length by 2
		String[] tempArray = new String[array.length-2];
		//run through the original array and if i matches the index number or index + 1 then dont copy that into the new array
		for (int i = 0, k = 0; i < array.length; i++) {
			if(i == index || i == index + 1) {
				continue;
			}
			tempArray[k++] = array[i];
		}
		return tempArray;
	}
	
}
