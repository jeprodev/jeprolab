package com.jeprolab.controllers;

import com.jeprolab.models.JeproLabGroupModel;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabGroupController extends JeproLabController{
    public static class JeproLabGroupRecord {
        public JeproLabGroupRecord(JeproLabGroupModel group){}
        public JeproLabGroupRecord(JeproLabGroupModel group, List<Integer> selectedGroups){}

    }

    public static class JeproLabCheckBoxCellFactory extends TableCell<JeproLabGroupRecord, Boolean>{

    }

    public static class JeproLabStatusCellFactory extends TableCell<JeproLabGroupRecord, Boolean>{

    }

    public static class JeproLabActionCellFactory extends TableCell<JeproLabGroupRecord, HBox>{

    }
}
