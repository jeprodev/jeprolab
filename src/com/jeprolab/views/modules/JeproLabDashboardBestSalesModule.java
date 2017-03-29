package com.jeprolab.views.modules;

import com.jeprolab.JeproLab;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

/**
 *
 * Created by jeprodev on 25/03/2016.
 */
public class JeproLabDashboardBestSalesModule extends JeproLabDashboardModule {
    private Timeline animation;
    private LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> hourDataSeries;
    private XYChart.Series<Number, Number> minuteDataSeries;
    private NumberAxis xAxis, yAxis;
    private double hours = 0;
    private double minutes = 0;
    private double timeInHours = 0;
    private double prevY = 10;
    private double y = 10;

    public JeproLabDashboardBestSalesModule(){
        super();
        animation = new Timeline();
        animation.getKeyFrames().add(new KeyFrame(Duration.millis(100/6), (ActionEvent evt) -> {
            for(int i = 0; i < 6; i++){
                nextTime();
                plotTime();
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        setTitleAndIcon(JeproLab.getBundle().getString("JEPROLAB_PERFORMANCES_LABEL"), "resources/images/edit.png");
    }

    @Override
    public void initializeContent(){
        xAxis = new NumberAxis(0, 24, 3);
        yAxis = new NumberAxis(0, 100, 10);
        chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefSize(this.formContentWrapper.getPrefWidth() - 30, this.formContentWrapper.getPrefHeight() - 10);

        chart.setCreateSymbols(false);

        xAxis.setForceZeroInRange(false);
        xAxis.setLabel(JeproLab.getBundle().getString("JEPROLAB_TIME_LABEL"));

        //data
        hourDataSeries = new XYChart.Series<>();

        minuteDataSeries = new XYChart.Series<>();
        hourDataSeries.getData()
            .add(new XYChart.Data<>(timeInHours, prevY));
        minuteDataSeries.getData()
            .add(new XYChart.Data<>(timeInHours, prevY));
        for (double m = 0; m < (60); m++) {
            nextTime();
            plotTime();
        }
        chart.getData().add(minuteDataSeries);
        chart.getData().add(hourDataSeries);
        this.formContentWrapper.getChildren().add(chart);
    }

    private void nextTime(){
        if (minutes == 59) {
            hours++;
            minutes = 0;
        } else {
            minutes++;
        }
        timeInHours = hours + ((1d / 60d) * minutes);
    }

    private void plotTime(){
        if ((timeInHours % 1) == 0) {
            // change of hour
            double oldY = y;
            y = prevY - 10 + (Math.random() * 20);
            prevY = oldY;
            while (y < 10 || y > 90) {
                y = y - 10 + (Math.random() * 20);
            }
            hourDataSeries.getData()
                .add(new XYChart.Data<Number, Number>(timeInHours, prevY));
            // after 25hours delete old data
            if (timeInHours > 25) {
                hourDataSeries.getData().remove(0);
            }
            // every hour after 24 move range 1 hour
            if (timeInHours > 24) {
                xAxis.setLowerBound(xAxis.getLowerBound() + 1);
                xAxis.setUpperBound(xAxis.getUpperBound() + 1);
            }
        }

        double min = (timeInHours % 1);
        double randomPickVariance = Math.random();
        if (randomPickVariance < 0.3) {
            double minY = prevY + ((y - prevY) * min) - 4 + (Math.random() * 8);
            minuteDataSeries.getData().add(new XYChart.Data<Number, Number>(timeInHours, minY));
        } else if (randomPickVariance < 0.7) {
            double minY = prevY + ((y - prevY) * min) - 6 + (Math.random() * 12);
            minuteDataSeries.getData().add(new XYChart.Data<Number, Number>(timeInHours, minY));
        } else if (randomPickVariance < 0.95) {
            double minY = prevY + ((y - prevY) * min) - 10 + (Math.random() * 20);
            minuteDataSeries.getData().add(new XYChart.Data<Number, Number>(timeInHours, minY));
        } else {
            double minY = prevY + ((y - prevY) * min) - 15 + (Math.random() * 30);
            minuteDataSeries.getData().add(new XYChart.Data<Number, Number>(timeInHours, minY));
        }
        // after 25hours delete old data
        if (timeInHours > 25) {
            minuteDataSeries.getData().remove(0);
        }

        play();
    }

    public void play() {
        animation.play();
    }


    public void stop() {
        animation.pause();
    }
}
