package com.practice.elliot;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;


@Controller
public class FormController {
    
    //controller gets called for form, it will retrieve from contact.java
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("contact", new Contact());
        System.out.println("hello!");
        return "form";
    
    }
    //after user keys in details in /form and save, form.html will check for errors
    @PostMapping("/contact")
    public String checkContactInfo(@Valid Contact contact, BindingResult bindingResult, Model model) {
        contact.invalidDateOfBirth(bindingResult);
        if (bindingResult.hasErrors()) {
            return "form";
        } else {
            return "result";
        }

    }  

    //controller gets result, stores data in file, gets result.html and post
    //new contact will be displayed in list and saved in data folder
    @PostMapping ("/result")
    public String submit(Contact contact) {
        Contacts.saveToFile(contact, "/data");
        return "result";
    }
    //controller gets called for list, gets all files from data file, requests for name and returns list.html
    //all saved contacts will be displayed
    @GetMapping("/list")
    public String list(Model model) {
        HashMap<String, Contact> names = Contacts.getAllFiles("/data");
        model.addAttribute("names", names);
        return "list";
    }

    // @DeleteMapping("/list/{name}")
    // public String delete(@PathVariable String name) {
    //     return "list"; 
}

