package org.example;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ContactManagementSystem {
    private List<Contact> contacts;
    private String fileName;

    public ContactManagementSystem(String fileName) {
        this.fileName = fileName;
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        saveContactsToFile();
    }

    public List<Contact> searchContacts(String keyword) {
        List<Contact> searchResults = new ArrayList<>();

        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(keyword.toLowerCase())
                    || contact.getPhoneNumber().contains(keyword)
                    || contact.getEmail().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(contact);
            }
        }

        return searchResults;
    }

    public void updateContact(String name, String newPhoneNumber, String newEmail) {
        Contact contactToUpdate = null;
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                contactToUpdate = contact;
                break;
            }
        }

        if (contactToUpdate != null) {
            contactToUpdate.phoneNumber = newPhoneNumber;
            contactToUpdate.email = newEmail;
            saveContactsToFile();
            System.out.println("Contact updated successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    public void removeContact(String name) {
        Contact contactToRemove = null;
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                contactToRemove = contact;
                break;
            }
        }

        if (contactToRemove != null) {
            contacts.remove(contactToRemove);
            saveContactsToFile();
            System.out.println("Contact removed successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void saveContactsToFile() {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (Contact contact : contacts) {
                writer.println(contact.getName() + "," + contact.getPhoneNumber() + "," + contact.getEmail());
            }
            System.out.println("Contacts saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    public void loadContactsFromFile() {
        contacts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String phoneNumber = parts[1];
                    String email = parts[2];
                    Contact contact = new Contact(name, phoneNumber, email);
                    contacts.add(contact);
                }
            }
            System.out.println("Contacts loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }
}

class Main {
    private static final String FILE_NAME = "contacts.txt";
    private static ContactManagementSystem cms;

    public static void main(String[] args) {
        cms = new ContactManagementSystem(FILE_NAME);
        cms.loadContactsFromFile();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Contact Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton addButton = createButton("Add Contact");
        JButton searchButton = createButton("Search Contacts");
        JButton updateButton = createButton("Update Contact");
        JButton removeButton = createButton("Remove Contact");
        JButton viewAllButton = createButton("View All Contacts");
        JButton quitButton = createButton("Quit");

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(frame, "Enter contact name to remove:");
                cms.removeContact(name);
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(frame, "Enter name:");
                String phoneNumber = JOptionPane.showInputDialog(frame, "Enter phone number:");
                String email = JOptionPane.showInputDialog(frame, "Enter email:");

                Contact contact = new Contact(name, phoneNumber, email);
                cms.addContact(contact);

                JOptionPane.showMessageDialog(frame, "Contact added successfully.");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = JOptionPane.showInputDialog(frame, "Enter search keyword:");

                List<Contact> searchResults = cms.searchContacts(keyword);

                if (searchResults.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No contacts found.");
                } else {
                    StringBuilder resultText = new StringBuilder();
                    for (Contact result : searchResults) {
                        resultText.append("Name: ").append(result.getName()).append("\n");
                        resultText.append("Phone Number: ").append(result.getPhoneNumber()).append("\n");
                        resultText.append("Email: ").append(result.getEmail()).append("\n\n");
                    }

                    JOptionPane.showMessageDialog(frame, resultText.toString());
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(frame, "Enter contact name:");
                String newPhoneNumber = JOptionPane.showInputDialog(frame, "Enter new phone number:");
                String newEmail = JOptionPane.showInputDialog(frame, "Enter new email:");

                cms.updateContact(name, newPhoneNumber, newEmail);
            }
        });

        viewAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Contact> allContacts = cms.getContacts();

                if (allContacts.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No contacts found.");
                } else {
                    StringBuilder resultText = new StringBuilder();
                    for (Contact contact : allContacts) {
                        resultText.append("Name: ").append(contact.getName()).append("\n");
                        resultText.append("Phone Number: ").append(contact.getPhoneNumber()).append("\n");
                        resultText.append("Email: ").append(contact.getEmail()).append("\n\n");
                    }

                    JOptionPane.showMessageDialog(frame, resultText.toString());
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(Box.createHorizontalGlue());
        panel.add(addButton);
        panel.add(searchButton);
        panel.add(updateButton);
        panel.add(removeButton);
        panel.add(viewAllButton);
        panel.add(quitButton);
        panel.add(Box.createHorizontalGlue());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(300, 220));
        frame.pack();
        frame.setVisible(true);
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 30));
        button.setMinimumSize(new Dimension(200, 30));
        button.setMaximumSize(new Dimension(200, 30));
        return button;
    }
}