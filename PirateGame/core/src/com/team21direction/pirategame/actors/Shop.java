package com.team21direction.pirategame.actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.team21direction.pirategame.screens.MainScreen;

public class Shop {
    private Stage stage;
    private FitViewport stageViewport;
    private Table shopTable;
    private Ship p;
    private Screen s;

    public Shop(Batch batch, Ship player, MainScreen screen) {
        this.p = player;
        this.s = screen;
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        stageViewport = new FitViewport(640, 480);
        stage = new Stage(stageViewport, batch);

        shopTable = new Table();
        shopTable.setFillParent(true);
        shopTable.top();
        Label title = new Label("Shop", skin);
        TextButton repairButton = new TextButton("Repair Ship: Cost 10", skin);
        repairButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(p.getGold() >= 10) {
                    p.setHealth(p.getMaxHealth());
                    p.addGold(-10);
                }
            }
        });
        shopTable.add(title);
        shopTable.row();
        shopTable.add(repairButton);
        stage.addActor(shopTable);

    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }
}
