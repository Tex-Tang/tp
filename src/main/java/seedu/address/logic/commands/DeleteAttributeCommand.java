package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SLACK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMEZONE;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import seedu.address.model.Model;
import seedu.address.model.person.*;
import seedu.address.model.person.Person;
import seedu.address.model.person.contact.Contact;
import seedu.address.model.person.contact.ContactType;
import seedu.address.model.tag.Tag;
import seedu.address.ui.MainPanelName;

public class DeleteAttributeCommand extends Command{
    public static final String COMMAND_WORD = "delete";
    public static final String MESSAGE_DELETE_SUCCESS = "Contact Attribute successfully deleted";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a person's specified contact. "
    + "Parameters: "
    + PREFIX_EMAIL + "EMAIL "
    + PREFIX_PHONE + "PHONE "
    + PREFIX_SLACK + "SLACK"
    + PREFIX_TELEGRAM + "TELEGRAM"
    + "Example: " + COMMAND_WORD + " "
    + PREFIX_EMAIL + "e0123456@u.nus.edu "
    + PREFIX_PHONE + "87654321"
    + PREFIX_SLACK + "johndoe "
    + PREFIX_TELEGRAM + "@johndoe";

    private Prefix prefixToDelete;

    /**
     * Constructor for DeleteAttributeCommand
     */
    public DeleteAttributeCommand (Prefix prefixToDelete) {
        this.prefixToDelete = prefixToDelete;
    }

    /**
     * Creates a new Person after deleting specified attribute.
     * Sets it to the previous person
     * @return CommandResult depicting success of delete command
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        // Get the Person whose attribute will be deleted
        Person toDelete = model.getSelectedPerson().get();
        assert toDelete != null;

        // Update the Person with the Deletion
        Person personAfterDeletion = createPersonAfterDeletion(toDelete);

        // Check for Duplicates
        model.setPerson(toDelete, personAfterDeletion);
        return new CommandResult(MESSAGE_DELETE_SUCCESS);
    }

    /**
     * Returns if DeleteAttributeCommand can execute at given Panel name.
     * @return true if Detail Page, false otherwise. 
     */
    @Override
    public boolean canExecuteAt(MainPanelName name) {
        return name.equals(MainPanelName.Detail);
    }

    /**
     * Creates new Person Object after deleting specified attribute (prefixToDelete)
     * @param toDelete
     * @return
     */
    private Person createPersonAfterDeletion(Person toDelete) {
        Name name = toDelete.getName();
        Role role = prefixToDelete.equals(PREFIX_ROLE) ? null : toDelete.getRole().orElse(null);
        Address address = prefixToDelete.equals(PREFIX_ADDRESS) ? null :
                                toDelete.getAddress().orElse(null);
        Set<Tag> tags = prefixToDelete.equals(PREFIX_TAG) ? null : toDelete.getTags();
        Timezone timezone = prefixToDelete.equals(PREFIX_TIMEZONE) ? null :
                                toDelete.getTimezone().orElse(null);
        Map<ContactType, Contact> contacts = toDelete.getContacts();
        Contact email = prefixToDelete.equals(PREFIX_EMAIL) ? null :
                                        contacts.get(ContactType.EMAIL);
        Contact phone = prefixToDelete.equals(PREFIX_PHONE) ? null :
                                        contacts.get(ContactType.PHONE);
        Contact slack = prefixToDelete.equals(PREFIX_SLACK) ? null :
                                        contacts.get(ContactType.SLACK);
        Contact telegram = prefixToDelete.equals(PREFIX_TELEGRAM) ? null :
                                        contacts.get(ContactType.TELEGRAM);

        Map<ContactType, Contact> updatedContacts = new HashMap<>();
       
        updatedContacts.put(ContactType.EMAIL, email);
        updatedContacts.put(ContactType.TELEGRAM, telegram);
        updatedContacts.put(ContactType.PHONE, phone);
        updatedContacts.put(ContactType.SLACK, slack);

        return new Person(name, address, tags, updatedContacts, role, timezone);
    }
}
