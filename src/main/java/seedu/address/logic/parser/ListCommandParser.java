package seedu.address.logic.parser;

import static seedu.address.commons.core.DataTypeFlags.FLAG_APPLICANT;
import static seedu.address.commons.core.DataTypeFlags.FLAG_INTERVIEW;
import static seedu.address.commons.core.DataTypeFlags.FLAG_POSITION;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.applicant.ListApplicantCommand;
import seedu.address.logic.commands.interview.ListInterviewCommand;
import seedu.address.logic.commands.position.ListPositionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments for data type flag and calls the respective
     * ListXCommand according to the flag specified.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ListCommand parse(String args) throws ParseException {
        char flag = ArgumentTokenizer.getFlag(args.trim());

        if (flag == FLAG_APPLICANT) {
            return new ListApplicantCommand();
        } else if (flag == FLAG_INTERVIEW) {
            return new ListInterviewCommand();
        } else if (flag == FLAG_POSITION) {
            return new ListPositionCommand();
        }

        return null;
    }
}