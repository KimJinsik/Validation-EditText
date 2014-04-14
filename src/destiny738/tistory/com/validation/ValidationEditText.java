/*
 * Copyright (C) 2014 Jinsik Kim
 * 
 */
package destiny738.tistory.com.validation;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
/**
 * ValidationEditText.java
 * 
 * This is custom EditText view. 
 * It can validate input value and If the input value is unvalidated, then caution to user.
 * Also, this editText view use a unique error icon and background of input box. 
 * Moreover, it have a custom pop-up which can be seen to call showError().
 * It is easy to change pop-up layout.
 * 
 * This use OnFocusChangeListener. And validation is occurred when it lose focus.
 */
public class ValidationEditText extends EditText implements OnFocusChangeListener{
	
	// remember scale of display for converting from px to dp. 
	private final float scale = getResources().getDisplayMetrics().density;
	TypedArray attrArray;
	
	// to declare for notifying error message
	private String errorMessage;
	private Drawable errorIcon;
	private PopupWindow popup;
	
	// scale of error icon
	private int iconWidth;
	private int iconHeight;	
	
	// error-popup background
	private int errorBackground;
	private int errorFontSize;
	private int errorFontColor;
	
	// pop-up position (user input)
	private int popupTop;
	private int popupLeft;
	
	// validation type
	private String validationType;
	
	// to declare variables for saving validation information 
	private String REGEX;			// regular expression
	private int MINLENGTH;			// minimum length. 
	private int MAXLENGTH;			// maximum length
	// I think information of lengths is not useful because of regular expression can covered length of string.
	
	// to declare flags
	private boolean regFlag;		// whether validate patterns or not
	private boolean lengthFlag;		// whether validate length or not
	private boolean showFlag;		// to check conflict each validation (length and regular expression)

	/** constructor */
	public ValidationEditText(Context context){
		super(context);
		// TODO Auto-generated constructor stub
		initValidationEditText();
	}
	public ValidationEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		attrArray = context.obtainStyledAttributes(attrs,R.styleable.destiny);
		initValidationEditText();
	}
	public ValidationEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		attrArray = context.obtainStyledAttributes(attrs,R.styleable.destiny, defStyle, 0);
		initValidationEditText();
	}
	
	/**
	 * initValidationeditText()
	 * 
	 * To get the attributes and to apply your applications. (To set the initial status on EditText.)
	 * Reset the flags. 
	 */ 
	private void initValidationEditText(){
		int attrMinLength = MINLENGTH = 0;
		int attrMaxLength = MAXLENGTH = Integer.MAX_VALUE;
		
		if ( attrArray != null ){
			iconHeight = attrArray.getDimensionPixelSize(R.styleable.destiny_errorIconHeightSize, 20);
			iconWidth = attrArray.getDimensionPixelSize(R.styleable.destiny_errorIconWidthSize, 20);
			iconHeight = pixelToDpi(iconHeight);
			iconWidth = pixelToDpi(iconWidth);
			errorIcon = getResources().getDrawable(attrArray.getResourceId(R.styleable.destiny_errorIcon, R.drawable.caution_icon));
			errorIcon.setBounds(0,0,iconWidth, iconHeight);
			
			errorMessage = attrArray.getString(R.styleable.destiny_errorMessage);
			if ( errorMessage == null ) errorMessage = "Invalidated input";
			errorBackground = attrArray.getResourceId(R.styleable.destiny_errorBackground, R.drawable.popup_background);
			errorFontSize = attrArray.getInt(R.styleable.destiny_errorFontSize, 12);
			errorFontColor = attrArray.getColor(R.styleable.destiny_errorFontColor, Color.BLACK);
			
			popupTop = attrArray.getDimensionPixelSize(R.styleable.destiny_popupTop, -4);
			popupLeft = attrArray.getDimensionPixelOffset(R.styleable.destiny_popupLeft, 0);
			
			validationType = attrArray.getString(R.styleable.destiny_validationType);
			
			attrMaxLength = attrArray.getInteger(R.styleable.destiny_validationMaxLength, Integer.MAX_VALUE);
			attrMinLength = attrArray.getInteger(R.styleable.destiny_validationMinLength, 0);
			
			attrArray.recycle();
		}else{
			iconHeight = 20;
			iconWidth = 20;
			errorIcon = getResources().getDrawable(R.drawable.caution_icon);
			errorIcon.setBounds(0,0,iconWidth,iconHeight);
			
			errorMessage = "Invalidated input";
			errorFontSize = 12;
			errorFontColor = Color.BLACK;
		}
		
		regFlag = false;
		lengthFlag = false;
		showFlag = false;
		
		if ( validationType != null ){
			if ( validationType.equals("email") ){
				setRegex(ValidationType.VALIDATION_EMAIL);
			}else if ( validationType.equals("number") ){
				setRegex(ValidationType.VALIDATION_NUMBER);
			}else if ( validationType.endsWith("url") ){
				setRegex(ValidationType.VALIDATION_URL);
			}else{
				Log.e("destiny738.tistory.com.validation","You can not select that validation type");
				Log.e("destiny738.tistory.com.validation","You can not select that validation type");
				Log.e("destiny738.tistory.com.validation","You can not select that validation type");
			}
		} 
		
		if ( attrMinLength > 0 ) setMinLength(attrMinLength);
		if ( attrMaxLength <= Integer.MAX_VALUE ) setMaxLength(attrMaxLength);
		
		setOnFocusChangeListener(this);
	}
	
	/**
	 * onFocusChange(View v, boolean hasFocus)
	 * 
	 * This listener is called when focus of view is changed.
	 * 
	 * Method do follow these step.
	 * 1. check the change of focus which is form focus to unfocus.
	 * 2. check flags. 
	 * 3. According to flags, it validate input data. 
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		// lost focus
		if ( !hasFocus ){	
			String input = this.getText().toString();		// save input data
			// regulation check
			myLog(regFlag+"");
			if ( regFlag && !showFlag){
				if ( !Pattern.matches(REGEX, input) && !input.equals("") ){
					showError();
					showFlag = true;
				}else{
					resetError();
				}
			}
			// length check
			if ( lengthFlag && !showFlag ){
				if ( (input.length() < MINLENGTH && input.length() > 0) || (input.length() > MAXLENGTH) ){
					showError();
				}else{
					resetError();
				}
			}
		}
	}
	
	/**
	 * showError() 
	 * 
	 * showError() is called when user input is not validated.
	 * It make pop-up window and view error icon.
	 */
	private void showError(){
		// pop-up null check and when pop-up is null then make new pop-up
		if ( popup == null ){	
			// to set the view(TextView) of pop-up attributes(background, text size and color...).
			TextView err = new TextView(getContext());
			err.setTextColor(errorFontColor);
			err.setBackgroundResource(errorBackground);
			err.setTextSize(errorFontSize);
			// to make new pop-up using err view-layout.
			popup = new PopupWindow(err, (int) (200 * scale + 0.5f), (int) (50*scale+0.5f));
			popup.setFocusable(false);
			popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		}
		
		// get the content view of new pop-up window.
		TextView tv = (TextView) popup.getContentView();
		setPopUpSize(popup, errorMessage, tv);
		
		tv.setText(errorMessage);
		// set the position and set the icon of error
		popup.showAsDropDown(this, getPositionX(), getPositionY());
		setCompoundDrawables(null, null, errorIcon, null);
	} 
	
	/**
	 * resetError()
	 * 
	 * resetError() is called when user input is validated.
	 * It remove pop-up window and error icon.
	 */
	private void resetError(){
		// remove error icon
		setError(null);		
		setCompoundDrawables(null, null, null, null);
		// remove pop-up
		if ( popup != null && popup.isShowing() ){
			popup.dismiss();
		}
	}
	
	/**
	 * setPopUpSize()
	 * @param pop  pop-up
	 * @param text message
	 * @param tv   textView in pop-up
	 * 
	 * setPopUpSize() is set to the pop-up message's size.
	 */
	private void setPopUpSize(PopupWindow pop, CharSequence text, TextView tv) {
		int width = tv.getPaddingLeft() + tv.getPaddingRight();
		int height = tv.getPaddingTop() + tv.getPaddingBottom();
		
		int maxWidth = getWidth() - width;
		
		Layout l = new StaticLayout(text, tv.getPaint(), maxWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
		float max = 0;
		for (int i = 0; i < l.getLineCount(); i++) {
			max = Math.max(max, l.getLineWidth(i));
		}

		pop.setWidth(width + (int) Math.ceil(max));
		pop.setHeight(height + l.getHeight());
	}
	
	/**
	 * getPositionX()
	 * @return left of pop-up message's layout
	 * 
	 * getPositionX() return pop-up x-position.
	 */
	private int getPositionX() {
		return getWidth() - popup.getWidth() - getPaddingRight() / 2 + popupLeft;
	}
	
	/**
	 * getPositionY()
	 * @return top of pop-up message's layout
	 * 
	 * getPositionY() return pop-up y-position.
	 */
	private int getPositionY() {
		return popupTop;
	}
	
	/**
	 * setRegex()
	 * @param validationType Input type. Static variable of ValidationType.java.
	 * 
	 * To set the patterns for regulation from ValidationRegularString.java.
	 * To change Regular validation flag(regFlag) true.
	 * If you add some case then write with both class ValidationRegularString and ValidationType.
	 */
	public void setRegex(int validationType) {
		switch(validationType){
			case ValidationType.VALIDATION_EMAIL:
				REGEX = ValidationRegularString.EMAIL;
				setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				break;
			case ValidationType.VALIDATION_URL:
				REGEX = ValidationRegularString.URL;
				setInputType(InputType.TYPE_TEXT_VARIATION_URI);
				break;
			case ValidationType.VALIDATION_NUMBER:
				REGEX = ValidationRegularString.NUMBER;
				setInputType(InputType.TYPE_CLASS_NUMBER);
				break;
		}
		regFlag = true;
	}

	/**
	 * setMinLength()
	 * @param length minimum length of string.
	 * 
	 * To set the minimum length of input string.
	 * To set the validation length(lengthFlag) true.
	 * If you to set the wrong number(larger than max-length) it is not act and just tell you using log.
	 */
	public void setMinLength(int length) {
		if ( length > MAXLENGTH ){
			myLog("Programmer!! You input wrong number. Reset the MinLength");
		}else{
			MINLENGTH = length;
			lengthFlag = true;
		}
	}
	
	/**
	 * setMaxLength()
	 * @param length maximum length of string.
	 * 
	 * To set the maximum length of input string.
	 * To set the validation length(lengthFlag) true.
	 * If you to set the wrong number(smaller than min-length) it is not act and just tell you using log.
	 */
	public void setMaxLength(int length){
		if ( length < MINLENGTH ){
			myLog("Programmer!! You input wrong number. Reset the MaxLength");
		}else{
			MAXLENGTH = length;
			lengthFlag = true;
		}
	}
	
	/**
	 * setMessage()
	 * @param message error message.
	 * 
	 * To set the message which is showed when user input is not validated.
	 */
	public void setMessage(String message) {
		errorMessage = message;
	}
	
	private int pixelToDpi(int number){
		return (int) (number * scale + 0.5f);
	}
	
	/* for debug */
	private static final String TAG = "DEBUG";
	void myLog(String input){
		Log.d(TAG,input);
	}
}
