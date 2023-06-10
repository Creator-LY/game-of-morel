# game-of-morel
Collect the most delicious mushrooms and roast them gently in a pan. With butter and cider you can refine your meal. For preparing mushrooms you will earn flavour points. The player who was able to receive the most flavour points by game’s end, will be the winner.

Enjoy your meal.

There is a total of 57 mushroom cards (49 day cards and 8 night cards). This accounts for 64
mushrooms (48+16) as night cards count as two mushrooms.

#### Mushroom cards
Mushroom | Amount (day/night) | Flavour points | Stick value |
| --- | --- | --- | --- |
| Honey fungus | 10/1 | 1 | 1 |
| Tree ear | 8/1 | 1 | 2 |
| Lawyer's wig | 6/1 | 2 | 1 |
| Shiitake | 5/1 | 2 | 2 |
| Hen of Woods | 5/1 | 3 | 1 |
| Birch bolete | 4/1 | 3 | 2 |
| Porcini | 4/1 | 3 | 3 |
| Chanterelle | 4/1 | 4 | 2 |
| Morel | 3/0 | 6 | 4 |

#### Other cards
Card | Amount |
| --- | --- |
| Butter | 3 |
| Cider | 3 |
| pan | 13 |
| Basket | 5 |
| Stick | Infinite |


## DEMO Images

<img src="/demo/start.png" width=340px /><img src="/demo/phase.png" width=340px />
<br>
<img src="/demo/uml.png" width=340px />


## Guidelines to setup

### Prerequisites

- java version with javafx installed.
- Download the javafx SDK that corresponds to your operating system and CPU architecture.
- Unzip the file and move the lib folder to the root folder of the project.
- You may want to add path of javafx bin to `path` in the `System Variable` if you encounter issue like `no suitable pipeline found`.


### Compile

```
javac --module-path ./lib --add-modules javafx.controls -d ./bin/ src/driver/*.java src/cards/*.java src/board/*.java
```

### Runnning Terminal version

```
java --class-path ./bin/ driver.Game
```

### Runnning Graphic version

```
java --module-path ./lib --add-modules javafx.controls --class-path ./bin/ driver.GraphicalGame
```

### Running Test

```
javac -cp ".;junit-platform-console-standalone.jar" --source-path ./src/ ./test/cards/*.java ./test/board/*.java -d ./bin/test/
java -jar junit-platform-console-standalone.jar  --class-path ./bin/test/ --scan-class-path --fail-if-no-tests
```
