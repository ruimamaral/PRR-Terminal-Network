package prr.app.terminals;

import java.util.Comparator;

import prr.core.Network;
import prr.core.terminal.Terminal;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show all terminals.
 */
class DoShowAllTerminals extends Command<Network> {

	DoShowAllTerminals(Network receiver) {
		super(Label.SHOW_ALL_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		final StringMaker stringMaker = new StringMaker();

		_receiver.visitAll(stringMaker,
				_receiver.getAllTerminals(),
				terminal -> true,
				Comparator.comparing(Terminal::getKey));

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
