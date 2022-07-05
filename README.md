# Eriantys Board Game - Software Engineering Project

<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/src/main/resources/assets/gui/images/Eriantys_Scatola.png?raw=true" width="260" align="right" />

Eriantys Board Game is the final test of **"Software Engineering"**, course of **"Computer Science Engineering"** held at Politecnico di Milano (2021/2022).

**Teacher**: Gianpaolo Cugola

**Grade**: 30 / 30

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
| 12 character cards           |   ⛔    |
| 4 players option             |   ⛔    |
| Simultaneous games           |   ✅    |
| Persistence                  |   ✅    |
| Resilience to disconnections |   ⛔    |

⛔ Not implemented &nbsp;&nbsp;&nbsp;&nbsp; ✅ Implemented

## Test cases
| Package    | Class, %     | Method, %     | Line, %       |
|:-----------|:-------------|:--------------|:--------------|
| Model      | 100% (29/29) | 87% (183/208) | 82% (597/721) |
| Controller | 100% (5/5)   | 97% (44/45)   | 86% (223/257) |

## Usage

### Requirements

Regardless of the operating system, you must have installed the following programs:
- Java 17
- Maven 3.3+ (e.g. 3.8.6)

#### Windows
On Windows it is needed to:
- Set system visual scaling to 100%.
- Only for CLI:
  - Set the CMD font to "Lucida Console" (the towers will show up as rectangles `▯`, if you want them to show up as tower character `♜` please install the font "DejaVuSansMono.ttf", find it in deliverables folder, and set it for the CMD).
  - Run the `chcp 65001` command in the CMD. This enables UTF-8 encoding.

### Instructions
1. Clone this repository:
    ```shell
   git clone https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon
   ```
2. Move to the repository folder.
3. Build the client package and move it from `target` to a new directory:

    ```shell
    mvn clean package -P client
    ```
4. Build the server package and move it from `target` to a new directory:

    ```shell
    mvn clean package -P server
    ```
5. Move to the new directory and execute the server and a client:
    ```shell
    java -jar server.jar
    java -jar client.jar
    ```
   Note that the client also accepts arguments at startup. In fact, it can also be started as:
   1. For CLI version:
   
       ```shell
       java -jar client.jar -cli
       ```
   2. For GUI version:
   
       ```shell
       java -jar client.jar -gui
       ```

## Screenshots
### Command Line Interface
<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/resources/screenshots/demo_cli.png?raw=true" />

### Graphical User Interface
<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/resources/screenshots/demo_gui_start.png?raw=true" />

<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/resources/screenshots/demo_gui_lobby.png?raw=true" />

<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/resources/screenshots/demo_gui_select_wizard.png?raw=true" />

<img src="https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/resources/screenshots/demo_gui.png?raw=true" />
