package prr.app.terminals;

import prr.core.Network;
import prr.util.StringMaker;
import prr.util.Visitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show all terminals.
 */
class DoShowAllTerminals extends Command<Network> {

	DoShowAllTerminals(Network receiver) {
		super(Label.SHOW_ALL_TERMINALS, receiver);
	}

	private final Visitor<Void> _stringMaker = new StringMaker();

	@Override
	protected final void execute() throws CommandException {
		if (_receiver.getClientCount() != 0) {
			_receiver.visitAll(this._stringMaker,
					_receiver.getAllTerminals(), terminal -> true);
			_display.popup(this._stringMaker);
		}
	}
}
