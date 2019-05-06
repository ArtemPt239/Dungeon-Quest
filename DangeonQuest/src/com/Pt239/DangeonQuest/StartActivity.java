package com.Pt239.DangeonQuest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.Random;

public class StartActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startlayout);

        Random rnd = new Random();
        switch (rnd.nextInt(4)){
            case 0:{((ImageView) findViewById(R.id.imageViewAdvert)).setImageResource(R.drawable.ad);
                findViewById(R.id.imageViewAdvert).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/drychan"));
                        startActivity(browserIntent);
                    }
                });
            }break;
            case 1:{((ImageView) findViewById(R.id.imageViewAdvert)).setImageResource(R.drawable.pixeltactics);}break;
            case 2:{((ImageView) findViewById(R.id.imageViewAdvert)).setImageResource(R.drawable.gf);}break;
            case 3:{((ImageView) findViewById(R.id.imageViewAdvert)).setImageResource(R.drawable.joke1);}break;
        }



        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.herolist, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((ImageView) findViewById(R.id.imageView1)).setImageResource(Player.getHeroBitmapID(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner = (Spinner)findViewById(R.id.spinner2);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((ImageView) findViewById(R.id.imageView2)).setImageResource(Player.getHeroBitmapID(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner = (Spinner)findViewById(R.id.spinner3);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((ImageView) findViewById(R.id.imageView3)).setImageResource(Player.getHeroBitmapID(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner = (Spinner)findViewById(R.id.spinner4);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((ImageView) findViewById(R.id.imageView4)).setImageResource(Player.getHeroBitmapID(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter =
                ArrayAdapter.createFromResource(this, R.array.mapmodes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner)findViewById(R.id.spinnerMapMode);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)findViewById(R.id.textViewGameMode))
                        .setText("Режим: " + getResources().getStringArray(R.array.gamemodes)[((Spinner)findViewById(R.id.spinnerGameMode)).getSelectedItemPosition()] +
                                "; "+ getResources().getStringArray(R.array.mapmodes)[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter =
                ArrayAdapter.createFromResource(this, R.array.gamemodes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner)findViewById(R.id.spinnerGameMode);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)findViewById(R.id.textViewGameMode))
                        .setText("Режим: " + getResources().getStringArray(R.array.gamemodes)[position] + "; "+
                                getResources().getStringArray(R.array.mapmodes)[((Spinner)findViewById(R.id.spinnerMapMode)).getSelectedItemPosition()]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.StartGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numOfPlayers=0;
                int[] playerHeroType = new int[4];
                String[] playerNames = new String[4];
                for(int i =0;i<4;i++){
                    playerHeroType[i]=-1;
                    playerNames[i]="noName";
                }
                if(((Switch)findViewById(R.id.switch1)).isChecked()){
                    playerHeroType[numOfPlayers]=((Spinner)findViewById(R.id.spinner1)).getSelectedItemPosition();
                    playerNames[numOfPlayers]=((EditText)findViewById(R.id.editText1)).getText().toString();
                    numOfPlayers++;
                }
                if(((Switch)findViewById(R.id.switch2)).isChecked()){
                    playerHeroType[numOfPlayers]=((Spinner)findViewById(R.id.spinner2)).getSelectedItemPosition();
                    playerNames[numOfPlayers]=((EditText)findViewById(R.id.editText2)).getText().toString();
                    numOfPlayers++;
                }
                if(((Switch)findViewById(R.id.switch3)).isChecked()){
                    playerHeroType[numOfPlayers]=((Spinner)findViewById(R.id.spinner3)).getSelectedItemPosition();
                    playerNames[numOfPlayers]=((EditText)findViewById(R.id.editText3)).getText().toString();
                    numOfPlayers++;
                }
                if(((Switch)findViewById(R.id.switch4)).isChecked()){
                    playerHeroType[numOfPlayers]=((Spinner)findViewById(R.id.spinner4)).getSelectedItemPosition();
                    playerNames[numOfPlayers]=((EditText)findViewById(R.id.editText4)).getText().toString();
                    numOfPlayers++;
                }

                if(numOfPlayers>0) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra("numOfPlayers",numOfPlayers);
                    intent.putExtra("playerHeroType[]",playerHeroType);
                    intent.putExtra("playerNames[]",playerNames);
                    intent.putExtra("maxNumOfTurnPoints",Integer.valueOf(((EditText)findViewById(R.id.editTextNumOfTurmPoints)).getText().toString()));
                    intent.putExtra("maxNumOfHP", ((SeekBar) findViewById(R.id.seekBarMaxNumOfHP)).getProgress()+1);
                    intent.putExtra("gameMode",((Spinner)findViewById(R.id.spinnerGameMode)).getSelectedItemPosition());
                    intent.putExtra("mapMode",((Spinner)findViewById(R.id.spinnerMapMode)).getSelectedItemPosition());
                    intent.putExtra("isPortalsEnabled",((Switch)findViewById(R.id.portalsSwitch)).isChecked());
                    intent.putExtra("isHealingFountainEnabled",((Switch)findViewById(R.id.switchHealingFountain)).isChecked());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Выберите хотя бы одного игрока",Toast.LENGTH_SHORT).show();
                }

            }
        });

        ((SeekBar) findViewById(R.id.seekBarMaxNumOfHP)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.textViewMaxNumOfHP)).setText("Кол-во очков здоровья: "+ String.valueOf(progress+1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Добро пожаловать в Dungeon Quest")
                .setIcon(R.drawable.sunduk0)
                .setCancelable(false)
                .setNegativeButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.setMessage(getResources().getString(R.string.welcome_message));
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Описание способностей классов");
        menu.add(0, 1, 1, "НИИЧАВО");
        menu.add(0, 2, 2, "НИИЧАВО");
        menu.add(0, 3, 3, "Сообщить об ошибке");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Способности классов:")
                        .setIcon(R.drawable.key0)
                        .setCancelable(false)
                        .setMessage(R.string.classesDescription)
                        .setNegativeButton("Всё понятно!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Куда сообщаем?")
                        .setIcon(R.drawable.key0)
                        .setCancelable(false)
                        .setPositiveButton("Админ!(Скатился)", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/id204600866"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNeutralButton("Разработчик", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/id20216275"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                builder.setMessage(getResources().getString(R.string.welcome_message));
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
        }
        return true;
    }
}
