import org.example.Contact;
import org.example.ContactManagementSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContactManagementSystemTest {
    private ContactManagementSystem cms;

    @Before
    public void setUp() {
        cms = new ContactManagementSystem("test_contacts.txt");
    }

    @After
    public void tearDown() {
        cms = null;
    }

    @Test
    public void testAddContact() {
        Contact contact = new Contact("John Smith", "1234567890", "john@gmail.com");
        cms.addContact(contact);
        assertEquals(1, cms.getContacts().size());
    }

    @Test
    public void testSearchContacts() {
        Contact contact1 = new Contact("John Smith", "1234567890", "john@gmail.com");
        Contact contact2 = new Contact("Alex Smith", "9876543210", "alex@gmail.com");
        cms.addContact(contact1);
        cms.addContact(contact2);

        assertEquals(2, cms.searchContacts("Smith").size());
        assertEquals(1, cms.searchContacts("John").size());
        assertEquals(1, cms.searchContacts("alex@gmail.com").size());
        assertEquals(0, cms.searchContacts("NotExisting").size());
    }

    @Test
    public void testUpdateContact() {
        Contact contact = new Contact("John Smith", "1234567890", "john@gmail.com");
        cms.addContact(contact);

        cms.updateContact("John Smith", "9876543210", "updated@gmail.com");

        Contact updatedContact = cms.getContacts().get(0);
        assertEquals("9876543210", updatedContact.getPhoneNumber());
        assertEquals("updated@gmail.com", updatedContact.getEmail());
    }

    @Test
    public void testRemoveContact() {
        Contact contact = new Contact("John Smith", "1234567890", "john@gmail.com");
        cms.addContact(contact);

        cms.removeContact("John Smith");

        assertEquals(0, cms.getContacts().size());
    }
}
