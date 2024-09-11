package com.gibatekpro.ecommerceandroid.checkout.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomEditText {

    private static final String TAG = "CustomEditText";
    
    private int minLength;

    private EditText editText;

    private String text;

    private boolean isEmail;

    private boolean lengthIsValid;

    private boolean formIsValid;

    private CharSequence sequence;

    public CustomEditText(int minLength, EditText editText, boolean isEmail) {
        this.minLength = minLength;
        this.editText = editText;
        this.isEmail = isEmail;

        this.text = "";
        this.lengthIsValid = false;
        this.formIsValid = false;

        setListeners();

    }

    private void setListeners() {


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sequence = s;

                validateForm();

            }

            @Override
            public void afterTextChanged(Editable s) {

                sequence = s;

                validateForm();

            }
        });

    }

    private void validateForm() {

        String trimmedText;

        if (sequence == null) {
            // Trim the text of leading/trailing whitespaces
            trimmedText = editText.getText().toString().trim();
        }else trimmedText = sequence.toString().trim(); // Trim the text of leading/trailing whitespaces

        
        // Get the number of characters
        int numCharacters = trimmedText.length();

        if (isEmail) {

            if (isValidEmail(trimmedText)) {

                resetError();

                lengthIsValid = true;
            }else {
                lengthIsValid = false;

                displayEmailError();
            }

        }else {
            if (numCharacters >= minLength) {

                resetError();

                lengthIsValid = true;

            }else {

                lengthIsValid = false;

                displayLengthError();

            }
        }

        //Set the form validation if true or false
        formIsValid = setValidation();

        setText(trimmedText);


    }

    private boolean setValidation(){

        //The form is valid if it has been touched
        // and if the length is okay
        return lengthIsValid;

    }

    private void setText(String text){
        this.text = text;
    }

    public String getText() {
        validateForm();
        return text;
    }

    public boolean isValid(){
        return formIsValid;
    }

    public void displayLengthError(){
        editText.setError("Error: Too short");
    }

    public void displayEmailError(){
        editText.setError("Error: Not an email");
    }

    public void resetError(){
        editText.setError(null);
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
