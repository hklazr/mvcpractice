package com.practice.elliot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Contact {

    //Model class created

    //Constructs the important details needed
    @Size(min=3, max=25, message="Name must be Between 3 and 25 characters")
    private String name;

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @Min(value=7, message="Phone number must contain at least 7 digits")
    private String phone;

    @NotEmpty
    private String dob;
    
    //Do Getters and Setters for each details needed
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name.trim();
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email.trim();
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone.trim();
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob.trim();
    }

    @Override
    public String toString() {
        return "Contact [name=" + name + ", email=" + email + ", phone=" + phone + ", dob=" + dob + "]";
    }

    public boolean invalidDateOfBirth(BindingResult bindingResult) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
        // System.out.println("testing dob");
        LocalDate date = null;
        ObjectError err = null;
        
        // Check Format dd-mm-yyyy
        try {
            date = LocalDate.parse(this.getDob(), formatter);
            
        } catch (DateTimeParseException e) {
            // System.out.println("failed");
            err = new ObjectError("globalError", "date must be in dd-mm-yyyy");
            bindingResult.addError(err);
            return true;
        }

        return false;
    }
}

