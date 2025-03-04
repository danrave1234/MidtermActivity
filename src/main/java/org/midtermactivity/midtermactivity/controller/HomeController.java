package org.midtermactivity.midtermactivity.controller;

import java.util.List;

import org.midtermactivity.midtermactivity.model.Contact;
import org.midtermactivity.midtermactivity.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("authenticated", true);
        } else {
            model.addAttribute("authenticated", false);
        }
        return "home";
    }
    
    @GetMapping("/contacts")
    public String contacts(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        
        List<Contact> contacts = contactService.getAllContacts();
        model.addAttribute("contacts", contacts);
        model.addAttribute("newContact", new Contact());
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        
        return "contacts";
    }
    
    @GetMapping("/contacts/people/{resourceName}/edit")
    public String editContactForm(@PathVariable String resourceName, Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return "redirect:/";
        }
        
        Contact contact = contactService.getContact(resourceName);
        model.addAttribute("contact", contact);
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        
        return "edit-contact.html";
    }
    
    @PostMapping("/contacts/create")
    public String createContact(@ModelAttribute Contact contact) {
        contactService.createContact(contact);
        return "redirect:/contacts";
    }

    @PostMapping("/contacts/{resourceName}/update")
    public String updateContact(@PathVariable String resourceName, @ModelAttribute Contact contact) {
        // Let the service handle all normalization
        contactService.updateContact(resourceName, contact);
        return "redirect:/contacts";
    }
    
    @GetMapping("/contacts/{resourceName}/delete")
    public String deleteContact(@PathVariable String resourceName) {
        contactService.deleteContact(resourceName);
        return "redirect:/contacts";
    }
} 