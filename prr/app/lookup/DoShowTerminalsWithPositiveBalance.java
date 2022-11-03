package prr.app.lookup;

import java.util.Comparator;

import prr.core.Network;
import prr.core.terminal.Terminal;
import prr.util.StringMaker;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show terminals with positive balance.
 */
class DoShowTerminalsWithPositiveBalance extends Command<Network> {

	DoShowTerminalsWithPositiveBalance(Network receiver) {
		super(Label.SHOW_TERMINALS_WITH_POSITIVE_BALANCE, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		final StringMaker stringMaker = new StringMaker();

		_receiver.visitAll(stringMaker,
				_receiver.getAllTerminals(),
				t -> t.calculateBalance() > 0,
				Comparator.comparing(Terminal::getKey));

		if (stringMaker.length() != 0) {
			_display.popup(stringMaker);
		}
	}
}
