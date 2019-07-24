// Calculator.java

/**
 * This program implements a basic GUI calculator.
 *
 * @author Scott Salvaggio
 * @version last modified Apr 16, 2013
 **/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator extends JFrame implements ActionListener
{
	// Back-end variables
	private double operand1 = 0.0;
	private char arithmeticOperator = '\0';
	private double operand2 = 0.0;
	private double tempAnswer = 0.0;
	private boolean decimalAvailable = true;
	private boolean zeroAccepted = false;
	private boolean operandEntered = false;
	private boolean appendText = false;

	// inInitialState refers to whether the user has entered an arithmetic
	// operator: 
	// - Whenever this variable is set to true, an arithmetic operator has
	// not yet been entered, so the calculator is in its 'initial state'. 
	// - Once the user enters an arithmetic operator, this variable is set 
	// to false, meaning that the calculator is no longer in its 
	// 'initial state' because it has the beginnings of an equation 
	// (i.e. operand1 and arithmeticOperator have been assigned values 
	// by the user).
	private boolean inInitialState = true; 
	
	// inErrorState refers to whether the user has tried to perform a 
	// calculation that results in one of the following invalid answers:
	// - 'infinity': caused by dividing by 0.
	// - 'NaN': caused by taking the square root of a negative number.
	// If either of the above operations occur, inErrorState is set to 
	// true and remains in this state until the user clicks the clear
	// button (which sets inErrorState back to false).
	private boolean inErrorState = false;
	
	// Front-end variables
	
	// Sets the width and height of the content pane.
	// The width:height ratio is 2:3.
	public static final int WIDTH = 200;
	public static final int HEIGHT = (int) (WIDTH * 1.5);

	// display - the JTextField that displays the numbers entered 
	// by the user (contained in the topPanel)
	private JTextField display;
	
	// top 12 calculator buttons (contained in the middlePanel)
	// these buttons are all squares
	private JButton clear;
	private JButton sqrt;
	private JButton divide;
	private JButton multiply;
	private JButton seven;
	private JButton eight;
	private JButton nine;
	private JButton subtract;
	private JButton four;
	private JButton five;
	private JButton six;
	private JButton add;

	// bottom 6 calculator buttons (contained in the bottomPanel)

	// - bottomPanel consists of leftPanel and rightPanel

	// -- leftPanel consists of leftTop and leftBottom

	// --- buttons contained in leftTop
	// --- these 2 buttons are squares
	private JButton one;
	private JButton two;

	// --- button contained in leftBottom
	private JButton zero; // this button is a 2:1 rectangle

	// -- rightPanel consists of right1 and right2

	// --- buttons contained in right1
	// --- these 2 buttons are squares
	private JButton three;
	private JButton decimal;

	// --- button contained in right2
	private JButton equalSign; // this button is a 1:2 rectangle

	/**
	 * Constructor for the class. Sets up the calculator GUI.
	 */
	public Calculator()
	{
		// JFrame settings
		setTitle("Calculator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		// Set JFrame location to middle of user's screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int midWidth = (int) ( dim.getWidth() - WIDTH ) / 2;
		int midHeight = (int) ( dim.getHeight() - HEIGHT ) / 2;
	        setLocation( midWidth, midHeight );

		// outerPanel (panel that fills the entire JFrame) 
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BorderLayout());
		outerPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		// topPanel (contains 'display', the JTextField that displays 
		// the numbers entered by the user)
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(0, 1));
		topPanel.setPreferredSize
			(new Dimension(WIDTH, (int) HEIGHT/6));
		
		// display
		display = new JTextField("0");
		display.setEditable(false);
		display.setHorizontalAlignment(JTextField.RIGHT);

		topPanel.add(display);

		// middlePanel (contains the top 12 JButtons)
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(0, 4));
		middlePanel.setPreferredSize
			(new Dimension(WIDTH, (int) HEIGHT/2));

		// top 12 buttons
		clear = new JButton("C");
		clear.addActionListener(this);
		sqrt = new JButton("\u221A");
		sqrt.addActionListener(this);
		divide = new JButton("/");
		divide.addActionListener(this);
		multiply = new JButton("*");
		multiply.addActionListener(this);
		seven = new JButton("7");
		seven.addActionListener(this);
		eight = new JButton("8");
		eight.addActionListener(this);
		nine = new JButton("9");
		nine.addActionListener(this);
		subtract = new JButton("-");
		subtract.addActionListener(this);
		four = new JButton("4");
		four.addActionListener(this);
		five = new JButton("5");
		five.addActionListener(this);
		six = new JButton("6");
		six.addActionListener(this);
		add = new JButton("+");
		add.addActionListener(this);

		middlePanel.add(clear);
		middlePanel.add(sqrt);
		middlePanel.add(divide);
		middlePanel.add(multiply);
		middlePanel.add(seven);
		middlePanel.add(eight);
		middlePanel.add(nine);
		middlePanel.add(subtract);
		middlePanel.add(four);
		middlePanel.add(five);
		middlePanel.add(six);
		middlePanel.add(add);

		// bottomPanel (contains the bottom 6 JButtons)
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(0, 2));
		bottomPanel.setPreferredSize
			(new Dimension(WIDTH, (int) HEIGHT/3));
		
		// - leftPanel (contains the leftTop and leftBottom panels)
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(2, 0));

		// -- leftTop (contains the 'one' and 'two' buttons)
		JPanel leftTop = new JPanel();
		leftTop.setLayout(new GridLayout(0, 2));

		one = new JButton("1");
		one.addActionListener(this);
		two = new JButton("2");
		two.addActionListener(this);

		leftTop.add(one);
		leftTop.add(two);

		// -- leftBottom (contains the 'zero' button)
		JPanel leftBottom = new JPanel();
		leftBottom.setLayout(new GridLayout(0, 1));

		zero = new JButton("0");
		zero.addActionListener(this);

		leftBottom.add(zero);
	
		// adds the leftTop and leftBottom panels to leftPanel
		leftPanel.add(leftTop);
		leftPanel.add(leftBottom);

		// - rightPanel (contains the right1 and right2 panels)
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(0, 2));
	
		// -- right1 (contains the 'three' and 'decimal' buttons)
		JPanel right1 = new JPanel();
		right1.setLayout(new GridLayout(2, 0));

		three = new JButton("3");
		three.addActionListener(this);
		decimal = new JButton(".");
		decimal.addActionListener(this);

		right1.add(three);
		right1.add(decimal);

		// -- right2 (contains the 'equalSign' button)
		JPanel right2 = new JPanel();
		right2.setLayout(new GridLayout(1, 0));

		equalSign = new JButton("=");
		equalSign.addActionListener(this);

		right2.add(equalSign);
		
		// adds the right1 and right2 panels to rightPanel
		rightPanel.add(right1);
		rightPanel.add(right2);

		// adds leftPanel and rightPanel to bottomPanel
		bottomPanel.add(leftPanel);
		bottomPanel.add(rightPanel);		

		// adds topPanel, middlePanel, and bottomPanel to outerPanel
		outerPanel.add(topPanel, BorderLayout.NORTH);
		outerPanel.add(middlePanel, BorderLayout.CENTER);
		outerPanel.add(bottomPanel, BorderLayout.SOUTH);

		// adds outerPanel to content pane
		getContentPane().add(outerPanel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	/**
	 * Defines what to do when the calculator buttons are clicked: 
	 * The processButton() method is called.
	 *
	 * @param  ae  The event generated by the button click
	 */
	public void actionPerformed(ActionEvent ae)
	{
		processButton(ae.getSource());
	}

	/**
	 * Defines how calculator buttons are processed.
	 *
	 * @param  buttonClicked  The button that was clicked
	 */
	private void processButton(Object o)
	{
		JButton buttonClicked = (JButton) o;
	
		// The clear button always functions, so it is independent of
		// the inErrorState boolean variable.	
		if (buttonClicked == clear)
			processClearButton();

		// The other buttons only function if inErrorState is false
		else if (!inErrorState)
		{
			// Note that the sqrt button is not considered 
			// to be an arithmetic operator. So it is defined in 
			// the processOtherButton() method.
			if (!isArithmeticOperatorOrEqualSign(buttonClicked))
				processOtherButton(buttonClicked);
			else if (buttonClicked == equalSign)
				processEqualSign();
			else // an arithmetic operator (i.e. add, subtract,
			     //	multiply, divide) was clicked
			{
				// Checks if an operand was entered prior to 
				// this arithmetic operator
				if (operandEntered)
					processArithmeticOperator
							(buttonClicked);
				else // This arithmetic operator was preceded 
				     // by an arithmetic operator. So this
				     // current operator will replace the 
				     // former one.
					arithmeticOperator = 
					    buttonClicked.getText().charAt(0);
			}
		}
	}

	/**
	 * Determines whether the JButton passed as a parameter is either an
	 * arithmetic operator button (add, subtract, multiply, or divide) or
	 * an equalSign button. Returns true if the JButton is any of the 
	 * above.
	 *
	 * @param  button  The JButton to be checked.
	 * @return	   Whether the JButton is an arithmetic operator or
	 * 		   equalSign.
	 */
	private boolean isArithmeticOperatorOrEqualSign(JButton button)
	{
		return button == add || button == subtract 
			|| button == multiply || button == divide
			|| button == equalSign;
	}

	/**
	 * Defines how the clear button is processed.
	 */
	private void processClearButton()
	{
		initializeEquationVariables();
		initializeInputBooleans();
		inInitialState = true;
		inErrorState = false;
		display.setText(zero.getText());
	}
	
	/**
	 * Defines how all buttons other than the clear button, the equalSign,
	 * and the four arithmetic operators (add, subtract, multiply, and 
	 * divide) are processed. 
	 *
	 * @param  button  The button that was clicked
	 */
	private void processOtherButton(JButton button)
	{
		if (button == sqrt)
		{
			double number = Double.parseDouble(display.getText());
			if (number == 0)
				display.setText(zero.getText());
			else if (number < 0)
				processInvalidAnswer();
			else // number > 0
			{
				display.setText
					(doubleToString( Math.sqrt(number) ));
			}
			decimalAvailable = true;
			zeroAccepted = true;
			operandEntered = true;
			appendText = false;
		}
		else if (button == decimal)
		{
			if (decimalAvailable)
			{
				// Prevents more than one decimal from being
				// appended to a number
				if (appendText)
					display.setText(display.getText() 
							+ decimal.getText());
				else // apendText is false
					display.setText(zero.getText()
							+ decimal.getText());
				decimalAvailable = false;
				zeroAccepted = true;
				operandEntered = true;
				appendText = true;
			}
		}
		else if (button == zero)
		{
			// Determines when zeros are appended to the display
			// text and when they replace the display text.
			// Prevents leading zeros from being added.
			if (appendText)
			{
				display.setText(display.getText() 
						+ zero.getText());
			}
			else if (zeroAccepted)
			{
				display.setText(zero.getText());
				zeroAccepted = false;
				decimalAvailable = true;
				operandEntered = true;
			}
		}
		else // a button from 1 to 9 was clicked
		{
			// Determines when these buttons are appended to the
			// display text and when they replace the display 
			// text
			if (appendText)
			{
				display.setText(display.getText() 
						+ button.getText());
			}
			else // appendText is false
			{
				display.setText(button.getText());
				zeroAccepted = true;
				operandEntered = true;
				appendText = true;
			}
		}
	}

	/**
	 * Defines how the arithmetic operator buttons (add, subtract, 
	 * multiply, and divide) are processed.  
	 *
	 * @param  button  The arithmetic operator that was clicked
	 */
	private void processArithmeticOperator(JButton button)
	{
		// The arithmetic operator buttons function differently,
		// depending on the boolean value of inInitialState.
		if (inInitialState)
		{
			// The user has not yet entered operand2, but has
			// provided input for operand1 and arithmeticOperator.
			operand1 = Double.parseDouble(display.getText());
			arithmeticOperator = button.getText().charAt(0);
			display.setText(doubleToString(operand1));
			inInitialState = false;
			zeroAccepted = true;
			decimalAvailable = true;
			operandEntered = false;
			appendText = false;
		}
		else // inInitialState is false
		{
			// Now we have a value for operand2, so we can 
			// perform the calculation.
			operand2 = Double.parseDouble(display.getText());
			tempAnswer = calculate();
			
			// Determines whether the calculated result is valid,
			// meaning it is not 'infinity' or 'NaN'.
			if (isValidAnswer(tempAnswer))
			{
				display.setText(doubleToString(tempAnswer));
				operand1 = tempAnswer;
				arithmeticOperator = button.getText().charAt(0);
				operand2 = 0.0;
				zeroAccepted = true;
				decimalAvailable = true;
				operandEntered = false;
				appendText = false;
			}
			else // tempAnswer is not valid (it is either
			     // infinity or NaN)
				processInvalidAnswer();
		}
	}

	/**
	 * Defines how the equalSign button is processed.
	 */
	private void processEqualSign()
	{
		// The equalSign only functions when the calculator is NOT 
		// in its initial state, which means that the user has entered 
		// sufficient input to perform a calculation.
		if (!inInitialState)
		{
			// Now we have a value for operand2, so we can 
			// perform the calculation.
			operand2 = Double.parseDouble(display.getText());
			tempAnswer = calculate();
			
			// Determines whether the calculated result is valid,
			// meaning it is not 'infinity' or 'NaN'.
			if (isValidAnswer(tempAnswer))
			{
				display.setText(doubleToString(tempAnswer));
				initializeEquationVariables();
				zeroAccepted = true;
				decimalAvailable = true;
				operandEntered = true;
				appendText = false;
				inInitialState = true;
			}
			else // tempAnswer is not valid (it is either
			     // infinity or NaN)
				processInvalidAnswer();
		}
	}
	
	/**
	 * Sets the boolean variables related to input back to their 
	 * initial values.
	 */
	private void initializeInputBooleans()
	{
		zeroAccepted = false;
		decimalAvailable = true;
		operandEntered = false;
		appendText = false;
	}

	/**
	 * Sets the equation variables back to their initial values.
	 */
	private void initializeEquationVariables()
	{
		operand1 = 0.0;
		arithmeticOperator = '\0';
		operand2 = 0.0;
		tempAnswer = 0.0;
	}
			
	/**
	 * Performs the calculation dictated by operand1, arithmetic operator,
	 * and operand2.
	 *
	 * @returns  The resulting value of the calculation
	 */
	private double calculate()
	{
		double answer = 0.0;
		switch (arithmeticOperator)
		{
			case '+': 
				answer = operand1 + operand2;
				break;
			case '-':
				answer = operand1 - operand2;
				break;
			case '*':
				answer = operand1 * operand2;
				break;
			case '/': 
				answer = operand1 / operand2;
				break;
		}
		return answer;
	}

	/**
	 * Determines whether the number passed as a parameter is infinitely
	 * large in magnitude (infinity) or is Not-a-Number (NaN).
	 * If the number is neither infinity or NaN, it is considered a 
	 * valid answer. 
	 *
	 * @param  number  The number to be checked.
	 * @return         Whether the number is a valid answer (i.e. it is 
	 * 		   not infinity or NaN).
	 */
	private boolean isValidAnswer(double number)
	{
		return !Double.isInfinite(number) && !Double.isNaN(number);
	}

	/**
	 * Defines what to do when (based on the user's input) the calculator
	 * produces an invalid answer (i.e. a value of infinity or NaN).
	 */
	private void processInvalidAnswer()
	{
		display.setText("Invalid operation");
		inErrorState = true;
	}
	
	/**
	 * Converts the parameter to a String as follows:
	 * - If parameter is a whole number, the parameter is cast to an
	 *   int before being converted to a String. This assures that whole
	 *   numbers like 3.0 are represented as "3" instead of "3.0".
	 * - If parameter is NOT a whole number, the parameter is converted
	 *   as is to a String (e.g. 3.5 is simply converted to "3.5"). 
	 *
	 * @param  number  The number to be converted to a String.
	 * @return         The String representation of this number.
	 */
	private String doubleToString(double number)
	{
		int numberAsInt = (int) number;
		double numberAsDouble = (double) numberAsInt;
		if (number - numberAsDouble == 0)
			return "" + numberAsInt;
		else // not a whole number
			return "" + number;
	}

	public static void main(String [] args)
	{
		Calculator calc = new Calculator();
	}
}
