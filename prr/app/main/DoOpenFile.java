package prr.app.main;

import prr.core.NetworkManager;
import prr.core.exception.UnavailableFileException;
import prr.app.exception.FileOpenFailedException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//Add more imports if needed

/**
 * Command to open a file.
 */
class DoOpenFile extends Command<NetworkManager> {

	DoOpenFile(NetworkManager receiver) {
		super(Label.OPEN_FILE, receiver);
		addStringField("fileName", Message.openFile());
	}
	
	@Override
	protected final void execute() throws CommandException {
		String fileName = stringField("fileName");
			try {
				_receiver.load(fileName);
			} catch (UnavailableFileException e) {
				throw new FileOpenFailedException(e);
			}
	}
}
