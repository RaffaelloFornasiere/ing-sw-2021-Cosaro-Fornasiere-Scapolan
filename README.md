# Prova finale di ingegneria del software - a.a. 2020-2021
The aim of the project is to develop a software version of the game "Master of Renaissance".

### Developers
This particular version was developed by Lisa Cosaro, Raffaello Fornasiere and Leonardo Scapolan.

### Implemented functionalities
- Complete rule set
- CLI
- GUI
- Socket
- Two advanced functionalities: "Partite multiple" and "Partita locale"

### Documentation
The project is accompanied by several UML diagrams:

- A [simplified UML diagram](https://github.com/RaffaelloFornasiere/ing-sw-2021-Cosaro-Fornasiere-Scapolan/blob/main/deliverables/UMLdiagram/SimplifiedUML.png) detailing the main classes of the application and their iteration
- A [initial UML diagram](https://github.com/RaffaelloFornasiere/ing-sw-2021-Cosaro-Fornasiere-Scapolan/blob/main/deliverables/UMLdiagram/InitialUML.png) showing the initial concept for the model where the development of the application started (as such it might slightly differ from the final implementation)
- A [sequence diagram](https://github.com/RaffaelloFornasiere/ing-sw-2021-Cosaro-Fornasiere-Scapolan/tree/main/deliverables/UMLdiagram/Sequence%20Diagrams/PlayerJoiningGame) showing the network interaction and the act of a user joining a lobby, together with an explanatory text
- A [sequence diagram](https://github.com/RaffaelloFornasiere/ing-sw-2021-Cosaro-Fornasiere-Scapolan/tree/main/deliverables/UMLdiagram/Sequence%20Diagrams/Buy%20a%20resource) showing the action of buying resources from the market, together with an explanatory text
- A [sequence diagram](https://github.com/RaffaelloFornasiere/ing-sw-2021-Cosaro-Fornasiere-Scapolan/tree/main/deliverables/UMLdiagram/Sequence%20Diagrams/PlayLeaderCard) showing the action of activating a leader card, together with an explanatory text
- A series of [complete UML diagrams](https://github.com/RaffaelloFornasiere/ing-sw-2021-Cosaro-Fornasiere-Scapolan/tree/main/deliverables/UMLdiagram/CompleteUML) for the whole application

### Requirements
The application supports only UNIX based operating systems (like Linux or macOS) due to the use of special characters to color the text in the CLI.
It requires Java SE 14.

### Instruction for testing
#### Single player
The application in single player can be run by executing the following command:
```bash
java -jar client.jar
```
After this you'll be asked to select CLI or GUI mode

#### Multiplayer
If you want to play multiplayer game you must run the server before through the following command
```bash
java -jar server.jar
```
and then 
```bash
java -jar client.jar
```
to run the client (Attention: only one client can run per set of resources. As such if you want to run two clients on the same machine, you must duplicate the whole \deliverables\jar\ directory). Again you'll be asked to select CLI or GUI.

If you're going to use the CLI it's recommended to execute this command before executing the jar
```bash
tput rmam
```
this ensures a proper visualization of the CLI.