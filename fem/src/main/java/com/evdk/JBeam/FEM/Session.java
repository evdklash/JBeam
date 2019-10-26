/* 
 * Copyright 2017 Etienne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evdk.JBeam.FEM;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evdk.JBeam.Resource.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Etienne
 */
public class Session extends Application {

    private static final Logger LOGGER = LogManager.getLogger(Session.class.getName());

    public static FEModel model;
    public static double[] xPlotPoints, yPlotPoints;
    public static ArrayList<double[]> moments;
    static boolean showPlots = true;
    boolean pause = false;
    boolean maximize = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        FEModelFactory factory = new FEModelFactory();

        model = factory.getFEModel();
        Solver solver = new Solver(model);
        solver.solve();

        moments = new ArrayList<>(model.getElementList().size() * 2);

        model.getElementList().stream().map(element -> (TwoNodeBeamElement) element).forEach(element -> {
            LOGGER.info(new BeamElementEndForceMessage(element));
            moments.add(element.getBeamMoments());
        });

        // Visulisation routines to follow
        int count = 0;
        int numPlotPoints = model.getNodeList().size();
        xPlotPoints = new double[numPlotPoints];
        yPlotPoints = new double[numPlotPoints];
        for (FeNode node : model.getNodeList()) {
            xPlotPoints[count] = node.getX();
            count++;
        }
        yPlotPoints = solver.getVerticalDisplacement();

        Application.launch(args);

        // TODO: Implement rotated beam elements in 2D
    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("BeamFEM");
        // Image mainWindowIcon = new Image("Resource/I-Beam.png");
        // stage.getIcons().add(mainWindowIcon);
        if (maximize == true) {
            stage.setMaximized(true);
        } else {
            stage.setWidth(1600);
            stage.setHeight(1060);
        }
        double interval = (xPlotPoints[xPlotPoints.length - 1] - xPlotPoints[0]) / (xPlotPoints.length - 1);

        NumberAxis xAxis = new NumberAxis(0.0, xPlotPoints[xPlotPoints.length - 1], interval);
        NumberAxis yAxis = new NumberAxis();
        LineChart<?, ?> base = new LineChart(xAxis, yAxis);
        base.setCreateSymbols(false);
        base.setLegendVisible(false);
        base.getXAxis().setAutoRanging(false);
        base.getXAxis().setAnimated(false);
        base.getYAxis().setAnimated(false);

        NumberAxis xAxis1 = new NumberAxis();
        NumberAxis xAxis2 = new NumberAxis();

        xAxis1.setAutoRanging(false);
        xAxis1.lowerBoundProperty().bind(((NumberAxis) base.getXAxis()).lowerBoundProperty());
        xAxis1.upperBoundProperty().bind(((NumberAxis) base.getXAxis()).upperBoundProperty());
        xAxis1.tickUnitProperty().bind(((NumberAxis) base.getXAxis()).tickUnitProperty());

        xAxis2.setAutoRanging(false);
        xAxis2.lowerBoundProperty().bind(((NumberAxis) base.getXAxis()).lowerBoundProperty());
        xAxis2.upperBoundProperty().bind(((NumberAxis) base.getXAxis()).upperBoundProperty());
        xAxis2.tickUnitProperty().bind(((NumberAxis) base.getXAxis()).tickUnitProperty());

        NumberAxis displacementYAxis = new NumberAxis();
        NumberAxis momentYAxis = new NumberAxis();

        LineChart deltaY = new LineChart(xAxis1, displacementYAxis);
        AreaChart<Double, Double> bendingMoment = new AreaChart(xAxis2, momentYAxis);

        momentYAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(momentYAxis) {
            @Override
            public String toString(Number value) {
                // note we are printing minus value
                return String.format("%.2f", -value.doubleValue());
            }
        });

        momentYAxis.setLabel("Bending Moment [kNm]");
        xAxis1.setLabel("Length Along Beam [m]");
        displacementYAxis.setLabel("Vertical Displacement [mm]");
        xAxis2.setLabel("Length Along Beam [m]");

        deltaY.setData(getChartData1());
        deltaY.setTitle("Beam Deflection");
        deltaY.setLegendVisible(false);

        bendingMoment.setData(getChartData2());
        bendingMoment.setTitle("Bending Moment Diagram");
        bendingMoment.setLegendVisible(false);

        setVerticalAxisProperties(momentYAxis);
        setVerticalAxisProperties(displacementYAxis);

        VBox root = new VBox();
        root.getChildren().add(deltaY);
        VBox.setVgrow(deltaY, Priority.ALWAYS);
        VBox.setVgrow(bendingMoment, Priority.ALWAYS);
        root.getChildren().add(bendingMoment);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        
        scene.getStylesheets().add(this.getClass().getResource("mytheme.css").toExternalForm());
        if (showPlots == true) {
            stage.show();
            if (pause == true) {
                PauseTransition delay = new PauseTransition(Duration.seconds(30.0));
                delay.setOnFinished(event -> stage.close());
                delay.play();
            }
        } else {
            Platform.exit();
        }
    }

    private void setVerticalAxisProperties(NumberAxis axis) {
        int w = 60;
        axis.setMaxWidth(w);
        axis.setMinWidth(w);
        axis.setPrefWidth(w);
    }

    private ObservableList<XYChart.Series<Double, Double>> getChartData1() {
        ObservableList<XYChart.Series<Double, Double>> answer = FXCollections.observableArrayList();
        XYChart.Series<Double, Double> aSeries = new XYChart.Series<>();

        aSeries.setName("Delta");

        for (int i = 0; i < xPlotPoints.length; i++) {
            aSeries.getData().add(new XYChart.Data<>(xPlotPoints[i], yPlotPoints[i] * 1000));
        }
        aSeries.getData().forEach(item -> {
            double x = item.getXValue();
            double y = item.getYValue();
            System.out.format("At %.2fm: Delta = %+5.1fmm\n", x, y);
        });
        answer.add(aSeries);
        return answer;
    }

    private ObservableList<XYChart.Series<Double, Double>> getChartData2() {
        ObservableList<Series<Double, Double>> answer = FXCollections.observableArrayList();
        XYChart.Series<Double, Double> aSeries = new XYChart.Series<>();

        aSeries.setName("BMD");

        for (int i = 0; i < xPlotPoints.length; i++) {
            if (i == 0) {
                aSeries.getData().add(new XYChart.Data<>(xPlotPoints[i], -1.0 * moments.get(i)[0] / 1000));
            } else {
                aSeries.getData().add(new XYChart.Data<>(xPlotPoints[i], -1.0 * moments.get(i - 1)[1] / 1000));
            }
        }
        aSeries.getData().forEach(item -> {
            double x = item.getXValue();
            double y = item.getYValue();
            System.out.format("At %.2fm: Delta = %6.3fkNm\n", x, y);
        });
        answer.add(aSeries);
        return answer;
    }

}
