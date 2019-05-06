package com.Pt239.DangeonQuest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    int turnPoints = 1, maxNumOfTurnPoints = 1, maxNumOfHP = 1, numOfPlayers, nowPlayerNum = 0;
    Player[] players;

    Boolean isPortalsEnabled = false;
    TextView infoTextView, turnPointsView, HPTextView;
    MapView mapView;
    Player activePlayer;
    Map map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Инициализация интерфейса
        FrameLayout l1 = (FrameLayout) findViewById(R.id.FrameLayout);

        infoTextView = new TextView(this);
        infoTextView.setTextSize(22);
        infoTextView.setGravity(Gravity.BOTTOM);
        infoTextView.setTextColor(Color.BLUE);

        turnPointsView = new TextView(this);
        turnPointsView.setTextSize(22);
        turnPointsView.setGravity(Gravity.TOP);
        turnPointsView.setTextColor(Color.BLUE);

        HPTextView = new TextView(this);
        HPTextView.setTextSize(22);
        HPTextView.setGravity(Gravity.RIGHT);
        HPTextView.setTextColor(Color.BLUE);

        findViewById(R.id.ScaleDownButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setScale(mapView.getScale() * 0.9f);
            }
        });
        findViewById(R.id.ScaleUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setScale(mapView.getScale() / 0.9f);
            }
        });
        findViewById(R.id.UpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTurn(0);
            }
        });
        findViewById(R.id.DownButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTurn(1);
            }
        });
        findViewById(R.id.LeftButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTurn(2);
            }
        });
        findViewById(R.id.RightButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTurn(3);
            }
        });
        findViewById(R.id.ItemPickButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickItems();
            }
        });
        findViewById(R.id.ItemListOpenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openItemsList();
            }
        });
        findViewById(R.id.ShootButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activePlayer != null) {
                    if (activePlayer.heroType == Player.HERO_TYPE_PYROMANCER) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Выберите действие:")
                                .setIcon(R.drawable.arrow0)
                                .setCancelable(false)
                                .setPositiveButton("Выстрел", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        shoot();
                                    }
                                })
                                .setNeutralButton("Магия", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        castMagic();
                                    }
                                })
                                .setNegativeButton("Отмена",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        shoot();
                    }
                }
            }
        });
        findViewById(R.id.EndTurnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTurn();
            }
        });
        findViewById(R.id.ButtonAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCheck();
            }
        });


        //Инициализация режима игры
        if (getIntent().getIntExtra("gameMode", 0) == 1) {
            findViewById(R.id.ButtonChangeViewingMap).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Выберите игрока")
                            .setIcon(R.drawable.sunduk0)
                            .setCancelable(false)
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    String[] playerNamesList = new String[numOfPlayers];
                    for (int i = 0; i < numOfPlayers; i++) {
                        playerNamesList[i] = players[i].name;
                    }
                    builder.setItems(playerNamesList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == nowPlayerNum) {
                                mapView.setPlayerNum(which);
                                findViewById(R.id.UpButton).setEnabled(true);
                                findViewById(R.id.DownButton).setEnabled(true);
                                findViewById(R.id.RightButton).setEnabled(true);
                                findViewById(R.id.LeftButton).setEnabled(true);
                                findViewById(R.id.ItemPickButton).setEnabled(true);
                                findViewById(R.id.ItemListOpenButton).setEnabled(true);
                                findViewById(R.id.ShootButton).setEnabled(true);
                                findViewById(R.id.ButtonAct).setEnabled(true);
                            } else {
                                mapView.setPlayerNum(which);
                                findViewById(R.id.UpButton).setEnabled(false);
                                findViewById(R.id.DownButton).setEnabled(false);
                                findViewById(R.id.RightButton).setEnabled(false);
                                findViewById(R.id.LeftButton).setEnabled(false);
                                findViewById(R.id.ItemPickButton).setEnabled(false);
                                findViewById(R.id.ItemListOpenButton).setEnabled(false);
                                findViewById(R.id.ShootButton).setEnabled(false);
                                findViewById(R.id.ButtonAct).setEnabled(false);
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        } else {
            findViewById(R.id.ButtonChangeViewingMap).setEnabled(false);
        }


        numOfPlayers = getIntent().getIntExtra("numOfPlayers", 1);
        if (numOfPlayers == 1) {
            maxNumOfTurnPoints = 99999999;
        } else {
            maxNumOfTurnPoints = getIntent().getIntExtra("maxNumOfTurnPoints", 1);
        }
        maxNumOfHP = getIntent().getIntExtra("maxNumOfHP", 1);
        turnPoints = maxNumOfTurnPoints;
        players = new Player[numOfPlayers];

        //Генерация карты
        Random rnd = new Random();
        int width = 3, height = 3, probableNumOfPortals = 0;
        switch (getIntent().getIntExtra("mapMode", 0)) {
            case 0: {
                width = 3;
                height = 3;
                probableNumOfPortals = 2;
                break;
            }
            case 1: {
                width = 5;
                height = 5;
                probableNumOfPortals = 2 + rnd.nextInt(2);
                break;
            }
            case 2: {
                width = rnd.nextInt(4) + 3;
                height = rnd.nextInt(4) + 3;
                probableNumOfPortals = 2 + rnd.nextInt(2);
                break;
            }
            case 3: {
                width = 9;
                height = 9;
                probableNumOfPortals = 4;
                break;

            }
        }
        map = generateMap(width, height);
        isPortalsEnabled = getIntent().getBooleanExtra("isPortalsEnabled", false);
        if (isPortalsEnabled) {
            map.generatePortals(probableNumOfPortals);
        }

        //Инициализация данных об игроках
        String[] playerNames = getIntent().getStringArrayExtra("playerNames[]");
        int[] playerHeroType = getIntent().getIntArrayExtra("playerHeroType[]");
        for (int i = 0; i < numOfPlayers; i++) {
            players[i] = new Player(width, height, playerHeroType[i]);
            players[i].name = playerNames[i];
            players[i].setMaxNumOfHP(maxNumOfHP);
            players[i].HP=players[i].maxNumOfHP;
            players[i].playerMap = new Map(width, height);
            players[i].playerMap.necropolisX = map.necropolisX;
            players[i].playerMap.necropolisY = map.necropolisY;
            players[i].playerMap.portals = map.portals;
        }

        activePlayer = players[0];
        mapView = new MapView(getApplicationContext(), players);
        mapView.setPlayerNum(0);
        //генерация мест спауна
        int ii = 0;
        while (ii < numOfPlayers) {
            players[ii].heroX = rnd.nextInt(width);
            players[ii].heroY = rnd.nextInt(height);

            if (map.cells[players[ii].heroX][players[ii].heroY].hasMonster) {
                ii--;
            } else {
                for (int j = 0; j < ii; j++) {
                    if ((players[ii].heroX == players[j].heroX && players[ii].heroY == players[j].heroY)) {
                        ii--;
                        break;
                    }
                }
            }
            ii++;
        }
        for (int i = 0; i < numOfPlayers; i++) {
            players[i].playerMap.cells[players[i].heroX][players[i].heroY] =
                    new Cell(map.cells[players[i].heroX][players[i].heroY]);
        }


        l1.addView(mapView);
        l1.addView(infoTextView);
        l1.addView(turnPointsView);
        l1.addView(HPTextView);
        addMessageForAllPlayers("Welcome and be ready for die!");
        refreshScreen();
    }

    void startTurn() {
        if(activePlayer.heroType==Player.HERO_TYPE_PYROMANCER){
            ((Button)findViewById(R.id.ShootButton)).setText("Shoot//Magic");
        }else{
            ((Button)findViewById(R.id.ShootButton)).setText("Shoot");
        }
        Player combatingPlayer1 = new Player(1, 1, Player.HERO_TYPE_MAGE);
        Boolean flag = false;
        for (int j = 0; j < numOfPlayers; j++) {
            if ((j != nowPlayerNum) && (activePlayer.heroX == players[j].heroX) && (activePlayer.heroY == players[j].heroY)) {
                flag = true;
                combatingPlayer1 = players[j];
                break;
            }
        }
        if (flag) {
            fightWithNearPlayer(combatingPlayer1);
        }

    }

    void doTurn(int direction) {

        if (activePlayer != null) {
            final int startHeroX = activePlayer.heroX, startHeroY = activePlayer.heroY;
            if (map != null) {
                if (turnPoints > 0) {
                    Wall targetWall = new Wall();
                    Cell targetCell = new Cell();
                    switch (direction) {
                        case 0: {//up
                            targetWall = map.wallsHorizontal[activePlayer.heroY][activePlayer.heroX];
                            activePlayer.playerMap.wallsHorizontal[activePlayer.heroY][activePlayer.heroX] = targetWall;
                            if (targetWall.state == Wall.STATE_CORRIDOR) {
                                activePlayer.heroY--;
                                targetCell = map.cells[activePlayer.heroX][activePlayer.heroY];
                            }

                            break;
                        }
                        case 1: {//down
                            targetWall = map.wallsHorizontal[activePlayer.heroY + 1][activePlayer.heroX];
                            activePlayer.playerMap.wallsHorizontal[activePlayer.heroY + 1][activePlayer.heroX] = targetWall;
                            if (targetWall.state == Wall.STATE_CORRIDOR) {
                                activePlayer.heroY++;
                                targetCell = map.cells[activePlayer.heroX][activePlayer.heroY];
                            }
                            break;
                        }
                        case 2: {//left
                            targetWall = map.wallsVertical[activePlayer.heroX][activePlayer.heroY];
                            activePlayer.playerMap.wallsVertical[activePlayer.heroX][activePlayer.heroY] = targetWall;
                            if (targetWall.state == Wall.STATE_CORRIDOR) {
                                activePlayer.heroX--;
                                targetCell = map.cells[activePlayer.heroX][activePlayer.heroY];
                            }
                            break;
                        }
                        case 3: {//right
                            targetWall = map.wallsVertical[activePlayer.heroX + 1][activePlayer.heroY];
                            activePlayer.playerMap.wallsVertical[activePlayer.heroX + 1][activePlayer.heroY] = targetWall;
                            if (targetWall.state == Wall.STATE_CORRIDOR) {
                                activePlayer.heroX++;
                                targetCell = map.cells[activePlayer.heroX][activePlayer.heroY];
                            }
                            break;
                        }
                    }


                    final Cell tgtCell = targetCell;
                    switch (targetWall.state) {
                        case 1: {//corridor      обработка попадания в клетку

                            activePlayer.playerMap.cells[activePlayer.heroX][activePlayer.heroY] =
                                    new Cell(targetCell);

                            if (targetCell.hasMonster) {//fight with monster
                                if (((float) new Random().nextInt(150)) / 100 > activePlayer.getFightCoefficient()) {
                                    //loose first
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Бой:")
                                            .setIcon(R.drawable.sword0)
                                            .setCancelable(false)
                                            .setPositiveButton("Биться", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (((float) new Random().nextInt(100)) / 100 > activePlayer.getFightCoefficient()) {
                                                        //death
                                                        activePlayer.addMessage("Монстр ударяет вас");
                                                        dealDamageToPlayer(activePlayer,2);
                                                    } else {
                                                        //win
                                                        tgtCell.hasMonster = false;
                                                        tgtCell.droppedItems.push(Item.MAGIC_ESSENCE);
                                                        activePlayer.addMessage("Монстр повержен!");
                                                        refreshScreen();
                                                    }
                                                }
                                            })
                                            .setNegativeButton("Бежать",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            activePlayer.heroX = startHeroX;
                                                            activePlayer.heroY = startHeroY;
                                                            if (((float) new Random().nextInt(150)) / 100 > 1) {
                                                                activePlayer.addMessage("Монстр ранил вас во время бегства");
                                                                dealDamageToPlayer(activePlayer, 1);
                                                            }
                                                            refreshScreen();
                                                            dialog.cancel();
                                                        }
                                                    });
                                    String msg = "";
                                    int rnd = new Random().nextInt(3);
                                    for (int i = 0; i < 1 + rnd; i++) {
                                        if (activePlayer.numOfItems > 0) {
                                            int rnd2 = new Random().nextInt(activePlayer.numOfItems);
                                            msg += Item.ITEM_NAMES[activePlayer.items[rnd2]] + "\n";
                                            dropItem(rnd2, activePlayer);
                                        }
                                    }
                                    builder.setMessage("Из темноты выскакивает нечто! От неожиданности вы роняете:" + msg +
                                            String.format("Вероятность победы: %f", activePlayer.getFightCoefficient()));
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    //win
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Бой:")
                                            .setIcon(R.drawable.sword0)
                                            .setCancelable(false)
                                            .setMessage("Из темноты выпрыгивает жуткая жуть, но напарывается на ваш меч. Вы победили монстра!")
                                            .setNegativeButton("Ура!",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    targetCell.hasMonster = false;
                                    targetCell.droppedItems.push(Item.MAGIC_ESSENCE);
                                    activePlayer.addMessage("Монстр повержен!");
                                    refreshScreen();
                                }
                            } else {

                                //fight with player
                                Player combatingPlayer1 = new Player(1, 1, Player.HERO_TYPE_MAGE);
                                Boolean flag = false;
                                for (int j = 0; j < numOfPlayers; j++) {
                                    if ((j != nowPlayerNum) && (activePlayer.heroX == players[j].heroX) && (activePlayer.heroY == players[j].heroY)) {
                                        flag = true;
                                        combatingPlayer1 = players[j];
                                        break;
                                    }
                                }
                                final Player combatingPlayer = combatingPlayer1;
                                if (flag) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Бой:")
                                            .setIcon(R.drawable.sword0)
                                            .setCancelable(false)
                                            .setPositiveButton("Биться", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (new Random().nextInt(100) * combatingPlayer.getFightCoefficient() >
                                                            new Random().nextInt(100) * activePlayer.getFightCoefficient()) {
                                                        //death
                                                        activePlayer.addMessage("Вы вступили в бой с игроком " + combatingPlayer.name);
                                                        combatingPlayer.addMessage("На вас напал игрок " + activePlayer.name + ", но в неравном бою вы одолели его!");
                                                        dealDamageToPlayer(activePlayer,1);
                                                        refreshScreen();
                                                    } else {
                                                        //win
                                                        activePlayer.addMessage("Вы вступили в бой с игроком " + combatingPlayer.name + " и победили!");
                                                        combatingPlayer.addMessage("На вас напал игрок " + activePlayer.name);
                                                        dealDamageToPlayer(combatingPlayer,1);
                                                        refreshScreen();
                                                    }
                                                }
                                            })
                                            .setNeutralButton("Сохранять нейтралитет", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    refreshScreen();
                                                    dialog.cancel();
                                                }
                                            })
                                            .setNegativeButton("Бежать(Вы потеряете вещи)",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            String msg = "";
                                                            int rnd = new Random().nextInt(3);
                                                            for (int i = 0; i < 1 + rnd; i++) {
                                                                if (activePlayer.numOfItems > 0) {
                                                                    int rnd2 = new Random().nextInt(activePlayer.numOfItems);
                                                                    msg += Item.ITEM_NAMES[activePlayer.items[rnd2]] + "\n";
                                                                    dropItem(rnd2, activePlayer);
                                                                }
                                                            }
                                                            combatingPlayer.addMessage("На вас наткнулся игрок " + activePlayer.name +
                                                                    ". Он сбежал, но уронил: " + msg);
                                                            activePlayer.addMessage("Убегая вы уронили: " + msg);
                                                            activePlayer.heroX = startHeroX;
                                                            activePlayer.heroY = startHeroY;
                                                            refreshScreen();
                                                            dialog.cancel();
                                                        }
                                                    });
                                    builder.setMessage("Вы натыкаетесь на игрока:" + combatingPlayer.name +
                                            String.format("Вы сильнее в %f раз", activePlayer.getFightCoefficient() / combatingPlayer.getFightCoefficient()));
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    //Ловушки!!!
                                    if (new Random().nextInt(20) == 5) {
                                        switch (new Random().nextInt(4)) {
                                            case 0: {
                                                int x, y;
                                                do {
                                                    x = new Random().nextInt(map.width);
                                                    y = new Random().nextInt(map.height);
                                                } while ((x == activePlayer.heroX && y == activePlayer.heroY));
                                                activePlayer.heroX = x;
                                                activePlayer.heroY = y;
                                                refreshScreen();
                                                if (map.cells[x][y].hasMonster) {
                                                    createMessageDialog("Oops...", "Перед вами монстр!");
                                                    fightWithNearMonster();
                                                }
                                                createMessageDialog("It's a trap!", "Вы слышите щелчок! Затем вас утаскивает в темноту... \nВы телепортировались!");
                                                refreshScreen();
                                                break;
                                            }
                                            case 1: {
                                                if (activePlayer.hasItemOfType(Item.SWORD) != -1) {
                                                    int x, y;
                                                    do {
                                                        x = new Random().nextInt(map.width);
                                                        y = new Random().nextInt(map.height);
                                                    } while (x == activePlayer.heroX && y == activePlayer.heroY);
                                                    activePlayer.removeItem(activePlayer.hasItemOfType(Item.SWORD));
                                                    map.cells[x][y].droppedItems.push(Item.SWORD);
                                                    refreshScreen();
                                                    createMessageDialog("It's a trap!", "Вы слышите щелчок! Ваш меч вырывается из ваших рук и улетает в неизвестном направлении!");
                                                } else {
                                                    createMessageDialog("It's a trap!", "Вы слышите щелчок,.. но ничего ни происходит!");
                                                }
                                                break;
                                            }
                                            case 2: {
                                                createMessageDialog("It's a trap!", "Вы слышите щелчок! Перед вами материализовывается монстр!");
                                                map.cells[activePlayer.heroX][activePlayer.heroY].hasMonster = true;
                                                refreshScreen();
                                                fightWithNearMonster();
                                                break;
                                            }
                                            case 3: {
                                                createMessageDialog("It's a trap!", "Вы слышите щелчок! Вы слышите свист! Что-то ударяет вас!");
                                                dealDamageToPlayer(activePlayer,1);
                                                refreshScreen();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {//indestructible
                            activePlayer.addMessage("Вы упёрлись в стену");
                            break;
                        }
                        case 3: {//destructible
                            activePlayer.addMessage("Эта стена выглядит так, словно она рухнет от малейшего чиха");
                            break;
                        }
                        case 4: {//exit
                            Boolean hasTreasure = false;
                            for (int i = 0; i < activePlayer.numOfItems; i++) {
                                if (activePlayer.items[i] == Item.TREASURE) {
                                    hasTreasure = true;
                                }
                            }
                            if (hasTreasure) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("*****Q-U-E-S-T*****")
                                        .setIcon(R.drawable.sunduk0)
                                        .setCancelable(false)
                                        .setMessage("Ваша миссия выполнена!\nВы победили!")
                                        .setNegativeButton("Пойду поброжу ещё.",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                activePlayer.addMessage("Нельзя так просто выйти из DungeonQuest");
                            }

                            break;
                        }
                    }
                    turnPoints--;

                    refreshScreen();
                } else {
                    activePlayer.addMessage("Вы истратили все очки хода!");
                    refreshScreen();
                }
            }
        }

    }

    void endTurn() {
        Player nextPlayer;
        if (nowPlayerNum + 1 < numOfPlayers) {
            nextPlayer = players[nowPlayerNum + 1];
        } else {
            nextPlayer = players[0];
        }
        infoTextView.setText("");
        mapView.setPlayerNum(-1);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Передайте телефон другому игроку")
                .setIcon(R.drawable.sunduk0)
                .setCancelable(false)
                .setNegativeButton("Телефон передан!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                findViewById(R.id.UpButton).setEnabled(true);
                                findViewById(R.id.DownButton).setEnabled(true);
                                findViewById(R.id.RightButton).setEnabled(true);
                                findViewById(R.id.LeftButton).setEnabled(true);
                                findViewById(R.id.ItemPickButton).setEnabled(true);
                                findViewById(R.id.ItemListOpenButton).setEnabled(true);
                                findViewById(R.id.ShootButton).setEnabled(true);
                                findViewById(R.id.ButtonAct).setEnabled(true);
                                turnPoints = maxNumOfTurnPoints;
                                if (nowPlayerNum + 1 < numOfPlayers) {
                                    nowPlayerNum++;
                                } else {
                                    nowPlayerNum = 0;
                                }
                                activePlayer = players[nowPlayerNum];
                                mapView.setPlayerNum(nowPlayerNum);
                                mapView.dx = 0;
                                mapView.dy = 0;
                                refreshScreen();
                                dialog.cancel();
                                startTurn();
                            }
                        });
        String msg = "Сейчас ходит: " + nextPlayer.name;
        builder.setMessage(msg);
        AlertDialog alert = builder.create();
        alert.show();

    }

    void actionCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Тут есть игроки:")
                .setIcon(R.drawable.sunduk0)
                .setCancelable(false)
                .setNegativeButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                refreshScreen();
                                dialog.cancel();
                            }
                        });

        final ArrayList<Integer> nearestPlayerNumbers = new ArrayList<Integer>();
        for (int j = 0; j < numOfPlayers; j++) {
            if ((j != nowPlayerNum) && (activePlayer.heroX == players[j].heroX) && (activePlayer.heroY == players[j].heroY)) {
                nearestPlayerNumbers.add(j);
            }
        }
        if (map.cells[activePlayer.heroX][activePlayer.heroY].hasMonster == true) {
            fightWithNearMonster();
        } else {
            if (nearestPlayerNumbers.size() > 0) {
                String[] playerNames = new String[nearestPlayerNumbers.size()];
                for (int i = 0; i < nearestPlayerNumbers.size(); i++) {
                    playerNames[i] = players[nearestPlayerNumbers.get(i)].name;
                }
                builder.setItems(playerNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fightWithNearPlayer(players[nearestPlayerNumbers.get(which)]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                if (isPortalsEnabled) {
                    if (map.portals.isCellHasPortal(activePlayer.heroX, activePlayer.heroY) != -1) {
                        travelTroughPortal();
                    }
                }
            }
        }
    }

    public void refreshScreen() {
        mapView.setPlayerNum(nowPlayerNum);
        activePlayer.playerMap.cells[activePlayer.heroX][activePlayer.heroY] = new Cell(map.cells[activePlayer.heroX][activePlayer.heroY]);
        infoTextView.setText(activePlayer.messages[4] + "\n" + activePlayer.messages[3] + "\n" + activePlayer.messages[2] + "\n" + activePlayer.messages[1] + "\n" + activePlayer.messages[0]);
        turnPointsView.setText("Кол-во очков хода: " + String.valueOf(turnPoints));
        HPTextView.setText("Кол-во очков здоровья: " + String.valueOf(activePlayer.HP));
        mapView.invalidate();
    }

    void createMessageDialog(String title, String message) {
        activePlayer.addMessage(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setIcon(R.drawable.sunduk0)
                .setCancelable(false)
                .setNegativeButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                refreshScreen();
                                dialog.cancel();
                            }
                        });
        builder.setMessage(message);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addMessageForAllPlayers(String message) {
        for (int i = 0; i < numOfPlayers; i++) {
            players[i].addMessage(message);
        }
    }

    public void pickItems() {
        if (activePlayer != null) {
            final Cell targetCell = map.cells[activePlayer.heroX][activePlayer.heroY];

            if (!targetCell.droppedItems.empty()) {
                if (activePlayer.numOfItems < Player.MAX_NUMBER_OF_ITEMS) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Тут есть клёвые вещички:")
                            .setIcon(R.drawable.sunduk0)
                            .setCancelable(false)
                            .setPositiveButton("Берём всё!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (activePlayer.numOfItems + targetCell.droppedItems.size() < Player.MAX_NUMBER_OF_ITEMS) {
                                        while (!targetCell.droppedItems.empty()) {
                                            activePlayer.addItem(targetCell.droppedItems.pop());
                                        }
                                    } else {
                                        createMessageDialog("Oops...", "Недостаточно места!");
                                    }
                                    refreshScreen();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Пускай остаются здесь",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    String[] itemsList = new String[targetCell.droppedItems.size()];
                    for (int i = 0; i < targetCell.droppedItems.size(); i++) {
                        itemsList[i] = Item.ITEM_NAMES[targetCell.droppedItems.elementAt(i)] + "\n";
                    }
                    builder.setItems(itemsList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activePlayer.addItem(targetCell.droppedItems.get(which));
                            targetCell.droppedItems.remove(which);
                            refreshScreen();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    createMessageDialog("Oops...", "Недостаточно места!");
                    refreshScreen();
                }
            }
        }

    }

    public void openItemsList() {
        if (activePlayer != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("У вас имеется:")
                    .setIcon(R.drawable.sunduk0)
                    .setCancelable(false)
                    .setNegativeButton("Назад",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            String[] itemsNames = new String[activePlayer.numOfItems];
            for (int i = 0; i < activePlayer.numOfItems; i++) {
                itemsNames[i] = Item.ITEM_NAMES[activePlayer.items[i]];
            }
            builder.setItems(itemsNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final int itemId = which;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder
                            .setCancelable(false)
                            .setMessage(Item.ITEMS_DESCRIPTIONS[activePlayer.items[which]])
                            .setPositiveButton("Использовать", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    useItem(itemId);
                                    refreshScreen();
                                }
                            })
                            .setNeutralButton("Выложить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dropItem(itemId, activePlayer);
                                    refreshScreen();
                                }
                            })
                            .setNegativeButton("Назад",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    public void useItem(int itemId) {
        switch (activePlayer.items[itemId]) {
            case 5: {//arrow
                shoot();
                break;
            }
            case 7: {//magicEssence
                if (activePlayer.heroType == Player.HERO_TYPE_MAGE) {
                    activePlayer.playerMap.wallsHorizontal[activePlayer.heroY][activePlayer.heroX] =
                            map.wallsHorizontal[activePlayer.heroY][activePlayer.heroX];
                    activePlayer.playerMap.wallsHorizontal[activePlayer.heroY + 1][activePlayer.heroX] =
                            map.wallsHorizontal[activePlayer.heroY + 1][activePlayer.heroX];
                    activePlayer.playerMap.wallsVertical[activePlayer.heroX][activePlayer.heroY] =
                            map.wallsVertical[activePlayer.heroX][activePlayer.heroY];
                    activePlayer.playerMap.wallsVertical[activePlayer.heroX + 1][activePlayer.heroY] =
                            map.wallsVertical[activePlayer.heroX + 1][activePlayer.heroY];
                    activePlayer.addMessage("Яркий свет озарил комнату");
                }
                break;
            }
            case 8: {//peace of map
                activePlayer.removeItem(itemId);
                int a = 1, b = 2;
                if (map.width * map.height > 20) {
                    a = 2;
                }
                if (map.width * map.height > 30) {
                    a = 3;
                }
                if (map.width * map.height > 45) {
                    b = 3;
                }
                if (map.width * map.height > 80) {
                    b = 4;
                }
                int x0 = new Random().nextInt(map.width - a);
                int y0 = new Random().nextInt(map.height - b);
                for (int dx = 0; dx < a; dx++) {
                    for (int dy = 0; dy < b; dy++) {
                        activePlayer.playerMap.cells[x0 + dx][y0 + dy] = new Cell(map.cells[x0 + dx][y0 + dy]);
                        activePlayer.playerMap.wallsVertical[x0 + dx][y0 + dy] = map.wallsVertical[x0 + dx][y0 + dy];
                        activePlayer.playerMap.wallsVertical[x0 + dx + 1][y0 + dy] = map.wallsVertical[x0 + dx + 1][y0 + dy];
                        activePlayer.playerMap.wallsHorizontal[y0 + dy][x0 + dx] = map.wallsHorizontal[y0 + dy][x0 + dx];
                        activePlayer.playerMap.wallsHorizontal[y0 + dy + 1][x0 + dx] = map.wallsHorizontal[y0 + dy + 1][x0 + dx];
                    }
                }
                activePlayer.addMessage("Свиток сгорел в ярком огне! Вы получили знания о небольшом участке лабиринта");
                break;
            }
        }
    }

    public void dropItem(int itemId, Player player) {
        map.cells[player.heroX][player.heroY].droppedItems.push(player.items[itemId]);
        player.removeItem(itemId);
    }

    void travelTroughPortal() {
        if (map.portals.isCellHasPortal(activePlayer.heroX, activePlayer.heroY) != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Пройти через портал?")
                    .setIcon(R.drawable.portal0)
                    .setCancelable(false)
                    .setPositiveButton("Вперёд!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int newX = map.portals.x[map.portals.getNextPortalNumber(map.portals.isCellHasPortal(activePlayer.heroX, activePlayer.heroY))],
                                    newY = map.portals.y[map.portals.getNextPortalNumber(map.portals.isCellHasPortal(activePlayer.heroX, activePlayer.heroY))];
                            activePlayer.heroX = newX;
                            activePlayer.heroY = newY;
                            refreshScreen();
                            if (map.cells[activePlayer.heroX][activePlayer.heroY].hasMonster) {
                                createMessageDialog("Oops...", "Перед вами монстр!");
                                fightWithNearMonster();
                            }
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Пожалуй не буду...",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void shoot() {
        if (activePlayer != null) {
            if ((turnPoints > 0) || (activePlayer.heroType == Player.HERO_TYPE_ROGUE)) {


                if ((activePlayer.hasItemOfType(Item.ARROW) != -1) && (activePlayer.hasItemOfType(Item.CROSSBOW) != -1)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Выбирете направление выстрела:")
                            .setIcon(R.drawable.arrow0)
                            .setCancelable(false)
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    String[] names = {"Вверх", "Вниз", "Влево", "Вправо"};
                    builder.setItems(names, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!(activePlayer.heroType == Player.HERO_TYPE_ROGUE) || (turnPoints < maxNumOfTurnPoints)) {
                                turnPoints--;
                            }
                            activePlayer.removeItem(activePlayer.hasItemOfType(Item.ARROW));
                            Wall targetWall = new Wall();
                            Cell targetCell = new Cell();
                            int arrowX = activePlayer.heroX, arrowY = activePlayer.heroY;
                            int newX = arrowX, newY = arrowY;
                            do {
                                switch (which) {
                                    case 0: {//up
                                        targetWall = map.wallsHorizontal[arrowY][arrowX];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX][arrowY - 1];
                                        }
                                        newY = arrowY - 1;
                                        break;
                                    }
                                    case 1: {//down
                                        targetWall = map.wallsHorizontal[arrowY + 1][arrowX];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX][arrowY + 1];
                                        }
                                        newY = arrowY + 1;
                                        break;
                                    }
                                    case 2: {//left
                                        targetWall = map.wallsVertical[arrowX][arrowY];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX - 1][arrowY];
                                        }
                                        newX = arrowX - 1;
                                        break;
                                    }
                                    case 3: {//right
                                        targetWall = map.wallsVertical[arrowX + 1][arrowY];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX + 1][arrowY];
                                        }
                                        newX = arrowX + 1;
                                        break;
                                    }
                                }
                                if (targetWall.state == Wall.STATE_CORRIDOR) {
                                    if (targetCell.hasMonster) {
                                        targetCell.hasMonster = false;
                                        targetCell.droppedItems.push(Item.MAGIC_ESSENCE);
                                        activePlayer.addMessage("Вы слышите далёкие стоны");
                                        break;
                                    }

                                    Boolean flag = false;
                                    for (int j = 0; j < numOfPlayers; j++) {
                                        if ((j != nowPlayerNum) && (newX == players[j].heroX) && (newY == players[j].heroY)) {
                                            players[j].addMessage("Вы слышите свист, а затем что-то ударяет вас в грудь");
                                            dealDamageToPlayer(players[j],2);
                                            activePlayer.addMessage("Вы слышите далёкие стоны");
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        break;
                                    }

                                    arrowX = newX;
                                    arrowY = newY;
                                } else {
                                    map.cells[arrowX][arrowY].droppedItems.push(Item.ARROW);
                                    activePlayer.addMessage("*Звук удара*");
                                }
                            } while (targetWall.state == Wall.STATE_CORRIDOR);

                            refreshScreen();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    activePlayer.addMessage("Трудно стрелять без стрел или арбалета!");
                    refreshScreen();
                }
            } else {
                activePlayer.addMessage("Вы истратили все очки хода!");
                refreshScreen();
            }
        }

    }//----------------------------------------------------

    void castMagic() {
        if (turnPoints > 0) {
            switch(activePlayer.heroType){
                case 4: {//Pyromancer
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Выбирете направление выстрела:")
                            .setIcon(R.drawable.arrow0)
                            .setCancelable(false)
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    String[] names = {"Вверх", "Вниз", "Влево", "Вправо"};
                    builder.setItems(names, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                turnPoints--;

                            Wall targetWall = new Wall();
                            Cell targetCell = new Cell();
                            int arrowX = activePlayer.heroX, arrowY = activePlayer.heroY;
                            int newX = arrowX, newY = arrowY;
                                switch (which) {
                                    case 0: {//up
                                        targetWall = map.wallsHorizontal[arrowY][arrowX];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX][arrowY - 1];
                                        }
                                        newY = arrowY - 1;
                                        break;
                                    }
                                    case 1: {//down
                                        targetWall = map.wallsHorizontal[arrowY + 1][arrowX];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX][arrowY + 1];
                                        }
                                        newY = arrowY + 1;
                                        break;
                                    }
                                    case 2: {//left
                                        targetWall = map.wallsVertical[arrowX][arrowY];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX - 1][arrowY];
                                        }
                                        newX = arrowX - 1;
                                        break;
                                    }
                                    case 3: {//right
                                        targetWall = map.wallsVertical[arrowX + 1][arrowY];
                                        if (targetWall.state == Wall.STATE_CORRIDOR) {
                                            targetCell = map.cells[arrowX + 1][arrowY];
                                        }
                                        newX = arrowX + 1;
                                        break;
                                    }
                                }
                                if (targetWall.state == Wall.STATE_CORRIDOR) {
                                    if (targetCell.hasMonster) {
                                        targetCell.hasMonster = false;
                                        targetCell.droppedItems.push(Item.MAGIC_ESSENCE);
                                        activePlayer.addMessage("Вы слышите далёкие стоны");
                                    }


                                    for (int j = 0; j < numOfPlayers; j++) {
                                        if ((j != nowPlayerNum) && (newX == players[j].heroX) && (newY == players[j].heroY)) {
                                            players[j].addMessage("Сгусток пламени вырывается из тьмы и поражает вас");
                                            dealDamageToPlayer(players[j],1);
                                            activePlayer.addMessage("Вы слышите далёкие стоны");
                                            break;
                                        }
                                    }

                                } else {
                                    activePlayer.addMessage("*Звук глухого удара*");
                                }

                            refreshScreen();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

        } else {
            activePlayer.addMessage("Вы истратили все очки хода!");
            refreshScreen();
        }

    }

    void fightWithNearPlayer(Player combatingPlayer1) {
        final Player combatingPlayer = combatingPlayer1;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Бой:")
                .setIcon(R.drawable.sword0)
                .setCancelable(false)
                .setPositiveButton("Будем драться!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (new Random().nextInt(100) * combatingPlayer.getFightCoefficient() >
                                new Random().nextInt(100) * activePlayer.getFightCoefficient()) {
                            //death
                            activePlayer.addMessage("Вы вступили в бой с игроком " + combatingPlayer.name);
                            combatingPlayer.addMessage("На вас напал игрок " + activePlayer.name + ", но в неравном бою вы одолели его!");
                            dealDamageToPlayer(activePlayer,1);
                            refreshScreen();
                        } else {
                            //win
                            activePlayer.addMessage("Вы вступили в бой с игроком " + combatingPlayer.name + " и победили!");
                            combatingPlayer.addMessage("На вас напал игрок " + activePlayer.name);
                            dealDamageToPlayer(combatingPlayer,1);
                            refreshScreen();
                        }
                    }
                })
                .setNegativeButton("Оставить в покое!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                refreshScreen();
                                dialog.cancel();
                            }
                        });
        builder.setMessage("Рядом с вами стоит игрок " + combatingPlayer.name + "!" +
                String.format("Вы сильнее в %f раз", activePlayer.getFightCoefficient() / combatingPlayer.getFightCoefficient()));
        AlertDialog alert = builder.create();
        alert.show();
    }

    void fightWithNearMonster() {
        map.cells[activePlayer.heroX][activePlayer.heroY].hasMonster = true;
        if (((float) new Random().nextInt(100)) / 100 > activePlayer.getFightCoefficient()) {
            //death
            activePlayer.addMessage("Монстр ударяет вас");
            dealDamageToPlayer(activePlayer,2);
        } else {
            //win
            map.cells[activePlayer.heroX][activePlayer.heroY].hasMonster = false;
            map.cells[activePlayer.heroX][activePlayer.heroY].droppedItems.push(Item.MAGIC_ESSENCE);
            activePlayer.addMessage("Монстр повержен!");
            refreshScreen();
        }
    }

    public void dealDamageToPlayer(Player player, int damage){
        player.HP-=damage;
        player.addMessage(String.format("Вы получили %d урона!",damage));
        if(player.HP<=0){
            killPlayer(player);
        }
    }

    public void killPlayer(Player player) {
        while (player.numOfItems > 0) {
            dropItem(0, player);
        }
        player.addMessage(getResources().getString(R.string.player_death_message));
        player.heroX = map.necropolisX;
        player.heroY = map.necropolisY;
        player.playerMap.cells[map.necropolisX][map.necropolisY] = map.cells[map.necropolisX][map.necropolisY];
        player.HP=player.maxNumOfHP;
        refreshScreen();
    }

    public Map generateMap(int width, int height) {
        Random random = new Random();
        Toast.makeText(getApplicationContext(), "Start generation", Toast.LENGTH_SHORT).show();
        Map outputMap = new Map(width, height);


        //спавним клетки
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                outputMap.cells[i][j].isDiscovered = true;
            }
        }
        //спавним стены
        for (int i = 0; i < height + 1; i++) {
            for (int j = 0; j < width; j++) {
                outputMap.wallsHorizontal[i][j].state = Wall.STATE_CORRIDOR;
            }
        }
        for (int i = 0; i < width + 1; i++) {
            for (int j = 0; j < height; j++) {
                outputMap.wallsVertical[i][j].state = Wall.STATE_CORRIDOR;
            }
        }
        //периметр
        for (int i = 0; i < width; i++) {
            outputMap.wallsHorizontal[0][i].state = Wall.STATE_INDESTRUCTIBLE;
            outputMap.wallsHorizontal[height][i].state = Wall.STATE_INDESTRUCTIBLE;
        }
        for (int i = 0; i < height; i++) {
            outputMap.wallsVertical[0][i].state = Wall.STATE_INDESTRUCTIBLE;
            outputMap.wallsVertical[width][i].state = Wall.STATE_INDESTRUCTIBLE;
        }
        //выход и кладбище
        int rnd1 = new Random().nextInt(2 * (width + height));
        if (rnd1 < 2 * width) {
            if (rnd1 < width) {
                outputMap.wallsHorizontal[0][rnd1].state = Wall.STATE_EXIT;
                //    outputMap.necropolisY = height - 1;
                //    outputMap.necropolisX = random.nextInt(width);
            } else {
                outputMap.wallsHorizontal[height][rnd1 - width].state = Wall.STATE_EXIT;
                //    outputMap.necropolisY = 0;
                //    outputMap.necropolisX = random.nextInt(width);
            }
        } else {
            if (rnd1 - 2 * width < height) {
                outputMap.wallsVertical[0][rnd1 - 2 * width].state = Wall.STATE_EXIT;
                //    outputMap.necropolisX = width - 1;
                //    outputMap.necropolisY = random.nextInt(height);
            } else {
                outputMap.wallsVertical[width][rnd1 - 2 * width - height].state = Wall.STATE_EXIT;
                //    outputMap.necropolisX = 0;
                //    outputMap.necropolisY = random.nextInt(height);
            }
        }
        outputMap.necropolisX = (width + random.nextInt(width)) / 3;
        outputMap.necropolisY = (height + random.nextInt(height)) / 3;

        //промежуточные стенки
        int maxNumOfWalls = ((width * (height - 1) + (width - 1) * height));
        for (int ii = 0; ii < ((float) maxNumOfWalls) / 3; ii++) {                   //   3-коэф.
            int rnd2 = new Random().nextInt(maxNumOfWalls);
            if (rnd2 < width * (height - 1)) {
                //horizontal
                int level = rnd2 / width + 1, x = rnd2 - (level - 1) * width;
                outputMap.wallsHorizontal[level][x].state = Wall.STATE_INDESTRUCTIBLE;
                if (!(cellCheck(outputMap, x, level - 1) && cellCheck(outputMap, x, level))) {
                    outputMap.wallsHorizontal[level][x].state = Wall.STATE_CORRIDOR;
                    ii--;
                }

            } else {
                //vertical
                int level = (rnd2 - width * (height - 1)) / height + 1, y = (rnd2 - width * (height - 1)) - (level - 1) * height;
                outputMap.wallsVertical[level][y].state = Wall.STATE_INDESTRUCTIBLE;
                if (!(cellCheck(outputMap, level - 1, y) && cellCheck(outputMap, level, y))) {
                    outputMap.wallsVertical[level][y].state = Wall.STATE_CORRIDOR;
                    ii--;
                }
            }
        }

        //items
        outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.TREASURE);
        outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.FAKE_TREASURE);
        for (int i = 0; i < width * height / 6; i++) { // fail in generation possibility is not equal for x and y
            switch (random.nextInt(8)) {
                case 0: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.SWORD);
                    break;
                }
                case 1: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.CROSSBOW);
                    break;
                }
                case 2: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.KEY);
                    break;
                }
                case 3: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.ARROW);
                    break;
                }
                case 4: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.ARROW);
                    break;
                }
                case 5: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.ARROW);
                    break;
                }
                case 6: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.MAGIC_ESSENCE);
                    break;
                }
                case 7: {
                    outputMap.cells[random.nextInt(width)][random.nextInt(height)].droppedItems.push(Item.PEACE_OF_MAP);
                    break;
                }
            }
        }
        //intresting objects
        if(getIntent().getBooleanExtra("isHealingFountainEnabled", false)) {
            outputMap.cells[random.nextInt(width)][random.nextInt(height)].specialObject = Cell.OBJECT_TYPE_HEALING_FOUNTAIN;
        }
        Toast.makeText(getApplicationContext(), "Monster spawn start", Toast.LENGTH_SHORT).show();
        //monsters
        for (int i = 0; i < width * height / 6; i++) {
            int rnd = random.nextInt(width * height);
            int x = rnd - rnd / width * width, y = rnd / width;
            if (!outputMap.cells[x][y].hasMonster) {
                outputMap.cells[x][y].hasMonster = true;
            }
        }
        Toast.makeText(getApplicationContext(), "Generation end", Toast.LENGTH_SHORT).show();
        return outputMap;
    }

    private boolean cellCheck(Map map, int x, int y) {
        // проверка - можно ли от этой ячейки добраться до всех
        //работает только после постройки стен периметра!!!!!
        Boolean[][] isChecked = new Boolean[map.width][map.height];
        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                isChecked[i][j] = false;
            }
        }
        mapCheckHelper(isChecked, map, x, y);
        Boolean outBool = true;
        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                outBool = isChecked[i][j] && outBool;
            }
        }
        return outBool;
    }

    private void mapCheckHelper(Boolean[][] isChecked, Map map, int x, int y) {
        isChecked[x][y] = true;
        if (map.wallsHorizontal[y][x].state == Wall.STATE_CORRIDOR && !isChecked[x][y - 1]) {
            mapCheckHelper(isChecked, map, x, y - 1);
        }
        if (map.wallsHorizontal[y + 1][x].state == Wall.STATE_CORRIDOR && !isChecked[x][y + 1]) {
            mapCheckHelper(isChecked, map, x, y + 1);
        }
        if (map.wallsVertical[x][y].state == Wall.STATE_CORRIDOR && !isChecked[x - 1][y]) {
            mapCheckHelper(isChecked, map, x - 1, y);
        }
        if (map.wallsVertical[x + 1][y].state == Wall.STATE_CORRIDOR && !isChecked[x + 1][y]) {
            mapCheckHelper(isChecked, map, x + 1, y);
        }
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainActivity.this);
        quitDialog.setTitle("Выход: Вы уверены?");
        quitDialog.setIcon(R.drawable.teemo0);
        quitDialog.setPositiveButton("Таки да!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        quitDialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "in Develop");
        menu.add(0, 1, 1, "in Develop");
        menu.add(0, 2, 2, "in Develop");
        menu.add(0, 3, 3, "in Develop");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                refreshScreen();
                break;
            }
            case 1: {
                refreshScreen();
                break;
            }
            case 2: {
                refreshScreen();
                break;
            }
            case 3: {
                refreshScreen();
                break;
            }
        }
        return true;
    }
}
