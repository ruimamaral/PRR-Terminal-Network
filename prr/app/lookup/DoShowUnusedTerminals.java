package prr.app.lookup;

import prr.core.Network;
import prr.util.StringMaker;
import prr.util.Visitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show unused terminals (without communications).
 */
class DoShowUnusedTerminals extends Command<Network> {

	DoShowUnusedTerminals(Network receiver) {
		super(Label.SHOW_UNUSED_TERMINALS, receiver);
	}

	final Visitor<Void> stringMaker = new StringMaker();

	@Override
	protected final void execute() throws CommandException {
		if (_receiver.getClientCount() != 0) {
			_receiver.visitAll(stringMaker,
					_receiver.getAllTerminals(),
					term -> (!term.hasActivity()));
		}
	}
}
