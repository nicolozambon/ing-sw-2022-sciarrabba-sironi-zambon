# Eriantys Board Game - Software Engineering Project

<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/src/main/resources/assets/gui/images/Eriantys_Scatola.png?raw=true" width="260" align="right" />

Eriantys Board Game is the final test of **"Software Engineering"**, course of **"Computer Science Engineering"** held at Politecnico di Milano (2021/2022).

**Teacher**: Gianpaolo Cugola

## The Team
* [Jonathan Sciarrabba](https://github.com/jonnysciar)
* [Alessandro Sironi](https://github.com/alessandrosironi)
* [Nicolò Zambon](https://github.com/nicolozambon)

## Project specification
The project consists of a Java version of the board game *Eriantys*, made by Cranio Creations. You can find the full game [here](https://www.craniocreations.it/prodotto/eriantys/).

The final version includes:
* Initial UML diagram.
* Final UML diagram, generated from the code by automated tools.
* Working game implementation, which has to be rules compliant.
* Source code of the implementation.
* Source code of unity tests.

## Implemented functionalities

### Main functionalities
| Functionality                    | Status |
|:---------------------------------|:------:|
| Basic rules                      |   ✅    |
| Complete rules                   |   ✅    |
| Socket                           |   ✅    |
| CLI _(Command Line Interface)_   |   ✅    |
| GUI _(Graphical User Interface)_ |   ✅    |

### Advanced functionalities
| Functionality                | Status |
|:-----------------------------|:------:|
| Twelve character cards       |   ⛔    |
| Four players option          |   ⛔    |
| Simultaneous games           |   ✅    |
| Persistence                  |   ✅    |
| Resilience to disconnections |   ⛔    |

⛔ Not implemented &nbsp;&nbsp;&nbsp;&nbsp; ✅ Implemented

## Test cases
| Package    | Class, %    | Method, %     | Line, %       |
|:-----------|:------------|:--------------|:--------------|
| Model      | 87% (29/33) | 85% (183/213) | 78% (600/760) |
| Controller | 71% (5/7)   | 93% (44/47)   | 83% (223/266) |

## Usage

### Requirements

Regardless of the operating system, you must have installed the following programs:
- Java 17
- Maven 3.3+ (e.g. 3.8.6)

#### Windows
On Windows it is needed to:
- Set system visual scaling to 100%.
- Install the "dejaVu Sans Font" and set it for the terminal.
- Run the `chcp 65001` command in the terminal. This enables UTF-8 encoding.

### Instructions
1. Clone this repository:
    ```shell
   git clone https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon
   ```
2. Move to the repository folder.
3. Build the client package and move it from `target` to a new directory:

    ```shell
    mvn clean package -P client
    # ------------------------------------------ add command here
    ```
4. Build the server package and move it from `target` to a new directory:

    ```shell
    mvn clean package -P server
    # ------------------------------------------ add command here
    ```
5. Move to the new directory and execute the server and a client:
    ```shell
    java -jar server.jar
    java -jar client.jar
    ```

## Screenshots
### Command Line Interface
<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/deliverables/screenshots/demo_cli.png?raw=true" />

### Graphical User Interface
<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/deliverables/screenshots/demo_gui_start.png?raw=true" />

<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/deliverables/screenshots/demo_gui_lobby.png?raw=true" />

<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/deliverables/screenshots/demo_gui.png?raw=true" />
