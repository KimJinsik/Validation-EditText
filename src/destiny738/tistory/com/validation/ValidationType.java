/*
 * Copyright (C) 2014 Jinsik Kim
 * 
 */
package destiny738.tistory.com.validation;
/**
 * ValidationType.java
 * 
 * This class has constant integer variables for selecting regular expression.
 * I say these constant integer to selector.
 * I make selector for e-mail, URL and number. Each number does not have meaning, just dummy.
 * If you want to have more patterns, you just register new selections.
 * And If you add a new selection, then you define a pattern in ValidationType.java 
 *  	and use this in ValidationEditText.java.
 */
class ValidationType {
	final static int VALIDATION_EMAIL = 3250;
	final static int VALIDATION_URL = 3251;
	final static int VALIDATION_NUMBER = 3252;
}
