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
    * `popupTop` - 
    * `popupLeft` -
 * Error Icon
    * `errorIcon` - 
    * `errorIconHeightSize` -
    * `errorIconWidthSize` -
 * Error pop-up
    * `errorBackground` -
    * `errorFontSize` - 
    * `errorFontColor` - 
    * `errorMessage` - 
 * Validation
    * `validationType` -
    * `validationMaxLength` -
    * `validationMinLength` -

TODO
=====

* Validate only number.
* Validate url address.
* Validate e-mail address.
* To edit design of error message's pop-up.
* To edit icon of error message.
* To check length of Input.
