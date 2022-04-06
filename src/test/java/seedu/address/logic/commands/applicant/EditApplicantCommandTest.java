package seedu.address.logic.commands.applicant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showApplicantAtIndex;
import static seedu.address.testutil.TypicalApplicants.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPLICANT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPLICANT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.applicant.EditApplicantCommand.EditApplicantDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.applicant.Applicant;
import seedu.address.testutil.ApplicantBuilder;
import seedu.address.testutil.EditApplicantDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditApplicantCommand.
 */
public class EditApplicantCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /*
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Applicant editedApplicant = new ApplicantBuilder().build();
        EditApplicantDescriptor descriptor = new EditApplicantDescriptorBuilder(editedApplicant).build();
        EditApplicantCommand editCommand = new EditApplicantCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditApplicantCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedApplicant);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setApplicant(model.getFilteredPersonList().get(0), editedApplicant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }
    */

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredApplicantList().size());
        Applicant lastApplicant = model.getFilteredApplicantList().get(indexLastPerson.getZeroBased());

        ApplicantBuilder personInList = new ApplicantBuilder(lastApplicant);
        Applicant editedApplicant = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditApplicantCommand.EditApplicantDescriptor descriptor =
                new EditApplicantDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditApplicantCommand editCommand = new EditApplicantCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditApplicantCommand.MESSAGE_EDIT_APPLICANT_SUCCESS, editedApplicant);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setApplicant(lastApplicant, editedApplicant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditApplicantCommand editCommand = new EditApplicantCommand(INDEX_FIRST_APPLICANT,
                                            new EditApplicantDescriptor());
        Applicant editedApplicant = model.getFilteredApplicantList().get(INDEX_FIRST_APPLICANT.getZeroBased());

        String expectedMessage = String.format(EditApplicantCommand.MESSAGE_EDIT_APPLICANT_SUCCESS, editedApplicant);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showApplicantAtIndex(model, INDEX_FIRST_APPLICANT);

        Applicant applicantInFilteredList = model.getFilteredApplicantList().get(INDEX_FIRST_APPLICANT.getZeroBased());
        Applicant editedApplicant = new ApplicantBuilder(applicantInFilteredList).withName(VALID_NAME_BOB).build();
        EditApplicantCommand editCommand = new EditApplicantCommand(INDEX_FIRST_APPLICANT,
                new EditApplicantDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditApplicantCommand.MESSAGE_EDIT_APPLICANT_SUCCESS, editedApplicant);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setApplicant(model.getFilteredApplicantList().get(0), editedApplicant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateApplicantUnfilteredList_failure() {
        Applicant firstApplicant = model.getFilteredApplicantList().get(INDEX_FIRST_APPLICANT.getZeroBased());
        EditApplicantDescriptor descriptor = new EditApplicantDescriptorBuilder(firstApplicant).build();
        EditApplicantCommand editCommand = new EditApplicantCommand(INDEX_SECOND_APPLICANT, descriptor);

        assertCommandFailure(editCommand, model, EditApplicantCommand.MESSAGE_DUPLICATE_APPLICANT);
    }

    @Test
    public void execute_duplicateApplicantFilteredList_failure() {
        showApplicantAtIndex(model, INDEX_FIRST_APPLICANT);

        // edit applicant in filtered list into a duplicate in address book
        Applicant applicantInList = model.getAddressBook().getApplicantList()
                                            .get(INDEX_SECOND_APPLICANT.getZeroBased());
        EditApplicantCommand editCommand = new EditApplicantCommand(INDEX_FIRST_APPLICANT,
                new EditApplicantDescriptorBuilder(applicantInList).build());

        assertCommandFailure(editCommand, model, EditApplicantCommand.MESSAGE_DUPLICATE_APPLICANT);
    }

    @Test
    public void execute_invalidApplicantIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredApplicantList().size() + 1);
        EditApplicantCommand.EditApplicantDescriptor descriptor =
                new EditApplicantDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditApplicantCommand editCommand = new EditApplicantCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_APPLICANT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidApplicantIndexFilteredList_failure() {
        showApplicantAtIndex(model, INDEX_FIRST_APPLICANT);
        Index outOfBoundIndex = INDEX_SECOND_APPLICANT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getApplicantList().size());

        EditApplicantCommand editApplicantCommand = new EditApplicantCommand(outOfBoundIndex,
                new EditApplicantDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editApplicantCommand, model, Messages.MESSAGE_INVALID_APPLICANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditApplicantCommand standardCommand = new EditApplicantCommand(INDEX_FIRST_APPLICANT, DESC_AMY);

        // same values -> returns true
        EditApplicantDescriptor copyDescriptor = new EditApplicantDescriptor(DESC_AMY);
        EditApplicantCommand commandWithSameValues = new EditApplicantCommand(INDEX_FIRST_APPLICANT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditApplicantCommand(INDEX_SECOND_APPLICANT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditApplicantCommand(INDEX_FIRST_APPLICANT, DESC_BOB)));
    }

}
