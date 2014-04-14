ValidationEditText
==================
`ValidationEditText` is inheritanced by editText. It can be useful to validate input data and to notify error message. 

You already know way to notify error messages(using showError() function) on editText. I think however, design of default error message is not good to view. But, ValidationEditText provide powerful way to design error message. 

**NOTE:**
If you think to add more regular expression, Please visit my blog and write guestbook!

(example screen-shot)


Features
=====
* Extends [`EditText`](http://developer.android.com/reference/android/widget/EditText.html)
* Implements [`OnFocusChangeListener`](http://developer.android.com/reference/android/view/View.OnFocusChangeListener.html)
* Supports [`JAVA Regular expression`](http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html)

Setup
=====

The library is built for and tested on Android version 2.2(SDK 8) and above. A way to use ValidationEditText is to add the library as a dependency to your project. 

Usage
=====

1. You download this source code and link to your project by library.
2. In layout file(XML), add "destiny738.tistory.com.validation.ValidationEditText" widget.
  ```xml
  
    <destiny738.tistory.com.validation.ValidationEditText
        xmlns:destiny="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" 
        android:padding="10dp"
        android:background="@drawable/edit_text_background"
        destiny:validationType="email" 
        destiny:errorIcon="@drawable/caution_icon"
        destiny:popupTop="-10dp" />
  
  ```
3. Configure attributes.
 * Position of pop-up  
    * `popupTop` - The top position of pop-up message. Unit: "dp". Default: "-4dp".
    * `popupLeft` - The left position of pop-up message. Unit: "dp". Default: "0dp".
 * Error Icon
    * `errorIcon` - The image of error icon. Unit: "reference". Default: "R.drawable.caution_icon".
    * `errorIconHeightSize` - The height of icon. Unit: "dp". Default: "20dp".
    * `errorIconWidthSize` - The width of icon. Unit: "dp". Default: "20dp".
 * Error pop-up
    * `errorBackground` - The background of pop-up window. Unit: "reference". Default: "R.drawable.popup_background".
    * `errorFontSize` - The size of font in pop-up window. Unit: "integer". Default: "12".
    * `errorFontColor` - The color of font in pop-up window. Unit: "color". Default: "Color.BLACK".
    * `errorMessage` - The message in pop-up window for error. Unit: "string". Default: "Invalidated input".
 * Validation
    * `validationType` - Type for validation. Currently, number, url address and e-mail address can validate. Do not have default.
    * `validationMaxLength` - Maximum length of input. If you set smaller than minimum length, then it is not act and it notify error message to you using log. Do not have default.
    * `validationMinLength` - Minimum length of input. If you set larger than maximum length, then it is not act and it notify error message to you using log. Do not have default.

TODO
=====

* Validate only number.
* Validate url address.
* Validate e-mail address.
* To edit design of error message's pop-up.
* To edit icon of error message.
* To check length of Input.
