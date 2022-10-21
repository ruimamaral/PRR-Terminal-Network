package prr.app.main;

import java.io.IOException;

import prr.core.NetworkManager;
import prr.core.exception.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;

/**
 * Command to save a file.
 */
class DoSaveFile extends Command<NetworkManager> {

	DoSaveFile(NetworkManager receiver) {
		super(Label.SAVE_FILE, receiver);
	}
	
	@Override
	protected final void execute() {
		try {
			try {
				_receiver.save();
			} catch (MissingFileAssociationException e) {
				this.saveAs();
			}
		} catch (IOException e) {
			e.printStackTrace(); // unexpected behaviour
		}
	}
	private void saveAs() throws IOException {
		try {
			_receiver.saveAs(Form.requestString(Message.newSaveAs()));
		} catch (MissingFileAssociationException e) {
			this.saveAs();
		}
	}
}
