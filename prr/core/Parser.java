package prr.core;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import java.util.Collection;
import java.util.ArrayList;

import prr.core.exception.UnrecognizedEntryException;
import prr.core.exception.DuplicateClientKeyException;
import prr.core.exception.DuplicateTerminalKeyException;
import prr.core.exception.UnknownClientKeyException;
// import more exception core classes if needed
import prr.core.exception.UnknownTerminalKeyException;

/* 
 * A concretização desta classe depende da funcionalidade suportada pelas entidades do core:
 *  - adicionar um cliente e terminal a uma rede de terminais;
 *  - indicar o estado de um terminal
 *  - adicionar um amigo a um dado terminal
 * A forma como estas funcionalidades estão concretizaas terão impacto depois na concretização dos
 * métodos parseClient, parseTerminal e parseFriends
 */

public class Parser {
  private Network _network;

  Parser(Network network) {
    this._network = network;
  }

  public void parseFile(String filename) throws IOException, UnrecognizedEntryException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      
      while ((line = reader.readLine()) != null)
        parseLine(line);
    }
  }
  
  private void parseLine(String line) throws UnrecognizedEntryException {
    String[] components = line.split("\\|");

    switch(components[0]) {
      case "CLIENT" -> parseClient(components, line);
      case "BASIC", "FANCY" -> parseTerminal(components, line);
      case "FRIENDS" -> parseFriends(components, line);
      default -> throw new UnrecognizedEntryException("Line with wong type: " + components[0]);
    }
  }

  private void checkComponentsLength(String[] components, int expectedSize, String line) throws UnrecognizedEntryException {
    if (components.length != expectedSize)
      throw new UnrecognizedEntryException("Invalid number of fields in line: " + line);
  }
  
  // parse a client with format CLIENT|id|nome|taxId
  private void parseClient(String[] components, String line) throws UnrecognizedEntryException {
    checkComponentsLength(components, 4, line);

    try {
      int taxId = Integer.parseInt(components[3]);
      this._network.registerClient(components[1], components[2], taxId);
    } catch (NumberFormatException nfe) {
      throw new UnrecognizedEntryException("Invalid number in line " + line, nfe);
    } catch (DuplicateClientKeyException e) {
      throw new UnrecognizedEntryException("Invalid specification in line: " + line, e);
    }
  }

  // parse a line with format terminal-type|idTerminal|idClient|state
  private void parseTerminal(String[] components, String line) throws UnrecognizedEntryException {
    checkComponentsLength(components, 4, line);

    try {
      this._network.registerTerminal(
          components[0], components[1], components[2], components[3]);
    } catch (IllegalArgumentException
			| UnknownClientKeyException 
			| DuplicateTerminalKeyException e) {
		throw new UnrecognizedEntryException("Invalid specification: " + line, e);
    }
  }

  //Parse a line with format FRIENDS|idTerminal|idTerminal1,...,idTerminalN
  private void parseFriends(String[] components, String line) throws UnrecognizedEntryException {
    checkComponentsLength(components, 3, line);
      
    try {
      String terminalKey = components[1];
      String[] friends = components[2].split(",");
      
      for (String friendKey : friends)
        this._network.addFriend(terminalKey, friendKey);
    } catch (UnknownTerminalKeyException e) {
      throw new UnrecognizedEntryException("Some message error in line:  " + line, e); // FIXME add terminal id
    }
  }
}