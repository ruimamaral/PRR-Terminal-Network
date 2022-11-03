package prr.app.lookup;

import java.util.Comparator;

import prr.core.Network;
import prr.core.terminal.Terminal;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show unused terminals (without communications).
 */
class DoShowUnusedTerminals extends Command<Network> {

	DoShowUnusedTerminals(Network receiver) {
		super(Label.SHOW_UNUSED_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		final StringMaker stringMaker = new StringMaker();

		_receiver.visitAll(stringMaker,
				_receiver.getAllTerminals(),
				Terminal::isInactive,
				Comparator.comparing(Terminal::getKey));

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
