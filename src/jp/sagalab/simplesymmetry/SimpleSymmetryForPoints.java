/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;



import static jp.sagalab.simplesymmetry.Output.getFileName2;

/**
 * 描画や点列の入力を扱うクラス
 *
 * @author sasaki
 */
public class SimpleSymmetryForPoints extends Application {

    @Override
    /**
     * 処理開始
     */
    public void start(Stage primaryStage) {
        Button btn1 = new Button();
        Button btn2 = new Button();
        Button btn3 = new Button();
        Button btn4 = new Button();
        Button btn5 = new Button();
        final Canvas canvas = new Canvas(CANVAS_SIZE_X, CANVAS_SIZE_Y);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, CANVAS_SIZE_X, CANVAS_SIZE_Y);
        gc.setFill(Color.BLACK);

        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (m_drawStatusFlag == 0) {
                    /*
                     とりあえずファジィを持たない点情報として点を格納
                     */
                    m_points.add(Point.create(event.getX(), event.getY(), 0));
                    gc.fillOval(event.getX() - 3, event.getY() - 3, 6, 6);
                }
                if (m_drawStatusFlag == 1) {
                    double centralX = m_points.get(m_points.size() - 1).getX();
                    double centralY = m_points.get(m_points.size() - 1).getY();
                    /*
                     ２点間の距離(=ファジネスの大きさ)を導出
                     */
                    double radius = Point.getDistance(m_points.get(m_points.size() - 1), Point.create(event.getX(), event.getY(), 0));
                    /*
                     ファジネスを付加した点情報に書き換える
                     */
                    m_points.set(m_points.size() - 1,
                            Point.create(centralX, centralY, radius));

                    drawFuzzyPoint(Point.create(centralX, centralY, radius), gc);

                    for (int i = 0; i < m_points.size(); i++) {
                        gc.setStroke(Color.BLACK);
                        gc.strokeOval(m_points.get(i).getX() - m_points.get(i).getF(), m_points.get(i).getY() - m_points.get(i).getF(),
                                2 * m_points.get(i).getF(), 2 * m_points.get(i).getF());
                    }

                }

                if (m_drawStatusFlag == 0) {
                    m_drawStatusFlag = 1;
                } else {
                    m_drawStatusFlag = 0;
                }

            }

        });

        /**
         * Calculateボタン押下時の動作
         *
         */
        btn1.setText("Calculate");
        btn1.setOnAction(new EventHandler<ActionEvent>() {

            double angle;
            double distance;
            List<SymmetricAxis> axis = new ArrayList<>();

            @Override
            public void handle(ActionEvent event) {
                //ファジネスの大きさを徐々に大きくしながら実行を繰り返す場合用
//                while (NUM_OF_FUZZINESS < 500) {
                if (m_points.size() > 0) {

                    double start = System.currentTimeMillis();

                    if (m_drawStatusFlag == 0) {
                        m_numOfPoints = m_points.size();
                        axis = FuzzySymmetry.generateSymmetricAxis(m_points);
                        Output.writeToCSV(m_points);
                    } else {
                        m_points.remove(m_points.size() - 1);
                        m_numOfPoints = m_points.size();
                        axis = FuzzySymmetry.generateSymmetricAxis(m_points);
                        Output.writeToCSV(m_points);
                    }

                    ImageView imageView = new ImageView();
                    String address = "file:./files/planesPicture/" + Output.getFileName() + ".png";
                    Image image = new Image(address, true);
                    imageView.setImage(image);

                    StackPane root2 = new StackPane();

                    root2.getChildren().add(imageView);

                    Scene scene2 = new Scene(root2, 640, 480);

                    Stage primaryStage2 = new Stage();
                    primaryStage2.initOwner(primaryStage);
                    primaryStage2.setTitle("houghplane");
                    primaryStage2.setScene(scene2);
                    primaryStage2.show();


//                    gc.setStroke(Color.RED);
//                    for (int i = 0; i < m_points.size()-1; i++) {
//                        gc.strokeLine(m_points.get(i).getX(),m_points.get(i).getY(),m_points.get(i+1).getX(),m_points.get(i+1).getY());
//                    }
//                    gc.strokeLine(m_points.get(m_points.size()-1).getX(),m_points.get(m_points.size()-1).getY(),m_points.get(0).getX(),m_points.get(0).getY());


                    gc.setStroke(Color.BLACK);
                    for (int i = 0; i < m_points.size()-1; i++) {
                        Pair p = Pair.create(m_points.get(i),m_points.get(i+1));
                        gc.fillOval(p.calculateCenterPoint().getX()-3,p.calculateCenterPoint().getY()-3,6,6);
                    }
                    Pair p = Pair.create(m_points.get(m_points.size()-1),m_points.get(0));
                    gc.fillOval(p.calculateCenterPoint().getX()-3,p.calculateCenterPoint().getY()-3,6,6);

                    for (int i = 0; i < axis.size(); i++) {
                        drawAxis(axis.get(i), gc);
                    }

//                    for(int i = axis.size()-5; i < axis.size(); i++) {
//                        System.out.println(axis.get(i).getGrade());
//                    }


                    for (int i = 0; i < m_points.size(); i++) {
                        gc.setFill(Color.BLACK);
                        gc.fillOval(m_points.get(i).getX() - 3, m_points.get(i).getY() - 3, 6, 6);
                        gc.setStroke(Color.BLACK);
                        gc.strokeOval(m_points.get(i).getX() - m_points.get(i).getF(), m_points.get(i).getY() - m_points.get(i).getF(),
                                2 * m_points.get(i).getF(), 2 * m_points.get(i).getF());
                    }

                    //canvas画像の保存
                    saveAsPng(new File("files/canvasPicture/"+getFileName2()+".png"),canvas);

                    double stop = System.currentTimeMillis();
                      //ファジネスの大きさを徐々に大きくしながら実行を繰り返す場合用
//                        saveAsPng(new File("C:\\Users\\sasaki\\Dropbox\\journalThesis\\AustraliaEdmonds\\" + "Australia" + (int) NUM_OF_FUZZINESS + ".png"), canvas);
                    System.out.println("実行にかかった時間は " + (stop - start) / 1000 + " 秒です。");

                    FuzzySymmetry.initializeHoughPlane();

                } else {
                    System.err.println("入力点数が少なすぎます");
                }

                //以下実験用------------------------------------------
                //ファジネスの大きさを徐々に大きくしながら実行を繰り返す場合用
//                    if ((NUM_OF_FUZZINESS - 1) % 100 == 0) {
//                        Output.writeTimeDataToTxt(m_allTimes, "allTimes_" + (int) NUM_OF_FUZZINESS);
//                        Output.writeTimeDataToTxt(m_votingTimes, "votingTimes_" + (int) NUM_OF_FUZZINESS);
//                        Output.writeTimeDataToTxt(m_matchingTimes, "matchingTimes_" + (int) NUM_OF_FUZZINESS);
//                    }
//
//                    NUM_OF_FUZZINESS = NUM_OF_FUZZINESS + 2;
//
//                    initialize();
//                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//
//                    m_points = Input.LoadPoint();
//
//                    //ファジネスを一定の大きさに変換
//                    for (int k = 0; k < m_points.size(); k++) {
//                        m_points.set(k, Point.create(m_points.get(k).getX(), m_points.get(k).getY(), NUM_OF_FUZZINESS, 0));
//                    }
//
//                    for (int i = 0; i < m_points.size(); i++) {
//                        gc.fillOval(m_points.get(i).getX() - 1,
//                                m_points.get(i).getY() - 1, 2, 2);
//
//                        drawFuzzyPoint(m_points.get(i), gc);
//                    }
//
//                    for (int i = 0; i < m_points.size(); i++) {
//                        gc.setStroke(Color.BLACK);
//                        gc.strokeOval(m_points.get(i).getX() - m_points.get(i).getF(), m_points.get(i).getY() - m_points.get(i).getF(),
//                                2 * m_points.get(i).getF(), 2 * m_points.get(i).getF());
//                    }
//                waitForThreeSeconds();
//                -------------------------------------------
//                }
            }
        });

        /**
         * Clearボタン押下時の動作
         *
         */
        btn2.setText("ClearAll");
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                initialize();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, CANVAS_SIZE_X, CANVAS_SIZE_Y);
                gc.setFill(Color.BLACK);
            }
        });

        /**
         * LoadPointsボタン押下時の動作
         */
        btn3.setText("LoadPoints");
        btn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                initialize();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, CANVAS_SIZE_X, CANVAS_SIZE_Y);
                gc.setFill(Color.BLACK);

                m_points = Input.LoadPoint();

                for (int i = 0; i < m_points.size(); i++) {
                    gc.fillOval(m_points.get(i).getX() - 3,
                            m_points.get(i).getY() - 3, 6, 6);

                    drawFuzzyPoint(m_points.get(i), gc);
                }

                for (int i = 0; i < m_points.size(); i++) {
                    gc.setStroke(Color.BLACK);
                    gc.strokeOval(m_points.get(i).getX() - m_points.get(i).getF(), m_points.get(i).getY() - m_points.get(i).getF(),
                            2 * m_points.get(i).getF(), 2 * m_points.get(i).getF());
                }

                m_drawStatusFlag = 0;
                m_fileLoadFlag = true;

            }

        });

        /**
         * ClearAxisボタン押下時の動作
         */
        btn4.setText("ClearAxis");
        btn4.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, CANVAS_SIZE_X, CANVAS_SIZE_Y);
                gc.setFill(Color.BLACK);

                for (int i = 0; i < m_points.size(); i++) {
                    gc.fillOval(m_points.get(i).getX() - 3,
                            m_points.get(i).getY() - 3, 6, 6);

                    drawFuzzyPoint(m_points.get(i), gc);
                }

                for (int i = 0; i < m_points.size(); i++) {
                    gc.setStroke(Color.BLACK);
                    gc.strokeOval(m_points.get(i).getX() - m_points.get(i).getF(), m_points.get(i).getY() - m_points.get(i).getF(),
                            2 * m_points.get(i).getF(), 2 * m_points.get(i).getF());
                }

                m_drawStatusFlag = 0;

            }

        });

        /**
         * Backボタン押下時の動作
         */
        btn5.setText("Back");
        btn5.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (m_points.size() > 0) {

                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, CANVAS_SIZE_X, CANVAS_SIZE_Y);
                    gc.setFill(Color.BLACK);

                    m_points.remove(m_points.size() - 1);

                    for (int i = 0; i < m_points.size(); i++) {
                        gc.fillOval(m_points.get(i).getX() - 3,
                                m_points.get(i).getY() - 3, 6, 6);

                        drawFuzzyPoint(m_points.get(i), gc);

                    }

                    for (int i = 0; i < m_points.size(); i++) {
                        gc.setStroke(Color.BLACK);
                        gc.strokeOval(m_points.get(i).getX() - m_points.get(i).getF(), m_points.get(i).getY() - m_points.get(i).getF(),
                                2 * m_points.get(i).getF(), 2 * m_points.get(i).getF());
                    }

                    m_drawStatusFlag = 0;

                } else {
                    System.err.println("点の数が少なすぎます。");
                }
            }

        });

        Pane root = new Pane(canvas, btn1, btn2, btn3, btn4, btn5);

        int box_x = 80;
        int box_y = 20;

        // Calculateボタン
        btn1.relocate(CANVAS_SIZE_X / 2 - (box_x / 2), CANVAS_SIZE_Y);
        btn1.setPrefSize(box_x, box_y);

        // ClearAllボタン
        btn2.relocate(CANVAS_SIZE_X - box_x, CANVAS_SIZE_Y);
        btn2.setPrefSize(box_x, box_y);

        // Loadpointsボタン
        btn3.relocate(box_x, CANVAS_SIZE_Y);
        btn3.setPrefSize(box_x, box_y);

        // ClearAxisボタン
        btn4.relocate(CANVAS_SIZE_X - (box_x * 2), CANVAS_SIZE_Y);
        btn4.setPrefSize(box_x, box_y);

        // Backボタン
        btn5.relocate(0, CANVAS_SIZE_Y);
        btn5.setPrefSize(box_x, box_y);

        Scene scene = new Scene(root, CANVAS_SIZE_X, CANVAS_SIZE_Y + 23);

        primaryStage.setTitle("FuzzySymmetry");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * 変数の初期化を行うメソッド
     */
    public void initialize() {
        m_drawStatusFlag = 0;
        m_fileLoadFlag = false;
        m_points = new ArrayList<>();
        FuzzySymmetry.initializeHoughPlane();
    }

    /**
     * ファジィ点の描画を行う
     * @param _point ファジィ点
     * @param _gc 
     */
    private void drawFuzzyPoint(Point _point, GraphicsContext _gc) {
        _gc.setStroke(Color.BLACK);
        _gc.setFill(Color.BLACK);
        _gc.fillOval(_point.getX() - 1, _point.getY() - 1, 2, 2);

        _gc.strokeOval(_point.getX() - _point.getF(), _point.getY() - _point.getF(),
                2 * _point.getF(), 2 * _point.getF());
    }

    /**
     * キャンバスに描画されているもののキャプチャーをしpng画像として保存
     * @param _file 保存先ファイル名
     * @param _canvas キャプチャ対象となるキャンバス
     */
    public void saveAsPng(File _file, Canvas _canvas) {
        WritableImage image = _canvas.snapshot(new SnapshotParameters(), null);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", _file);
        } catch (IOException e) {
        }
    }



    /**
     * 直線の描画を行う
     *
     * @param _axis 描画したい軸
     * @param _gc GraphicsContext
     */
    private void drawAxis(SymmetricAxis _axis, GraphicsContext _gc) {

        double x1, x2;
        double angle;

        angle = _axis.getAngle();

        x1 = (_axis.getDistance() - Math.sin(angle) * CANVAS_SIZE_Y) / Math.cos(angle);
        x2 = (_axis.getDistance() - Math.sin(angle) * 0) / Math.cos(angle);

        _gc.setStroke(_axis.getColor());
        if(_axis.getGrade() == 1.0) {
            _gc.setStroke(Color.RED);
        }

        if (Math.abs(x2) > CANVAS_SIZE_X) {
            double y1 = (_axis.getDistance() - Math.cos(angle) * CANVAS_SIZE_X) / Math.sin(angle);
            double y2 = (_axis.getDistance() - Math.cos(angle) * 0) / Math.sin(angle);

            _gc.strokeLine(CANVAS_SIZE_X, y1, 0, y2);
        } else {
            _gc.strokeLine(x1, CANVAS_SIZE_Y, x2, 0);
        }

        _gc.setStroke(Color.BLACK);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FuzzySymmetry.initializeHoughPlane();

        PlatformUtils.deleteDirectory("files/planes(pair)");
        PlatformUtils.deleteDirectory("files/planesPicture(pair)");

        PlatformUtils.createDirectory("files");
        PlatformUtils.createDirectory("files/points");
        PlatformUtils.createDirectory("files/attributes");
        PlatformUtils.createDirectory("files/planes");
        PlatformUtils.createDirectory("files/planesPicture");
        PlatformUtils.createDirectory("files/pointsforcurves");
        PlatformUtils.createDirectory("files/canvasPicture");
        PlatformUtils.createDirectory("files/planes(pair)");
        PlatformUtils.createDirectory("files/planesPicture(pair)");

        launch(args);
    }

    /**
     * 描画モードを扱うフラッグ変数
     */
    private int m_drawStatusFlag;
    /**
     * ファジィ点を格納するリスト
     */
    List<Point> m_points = new ArrayList<>();
    /**
     * 点の数を持ちます
     */
    public static int m_numOfPoints;
    /**
     * キャンバスサイズ(X軸方向)
     */
    public static final int CANVAS_SIZE_X = 800;
    /**
     * キャンバスサイズ(Y軸方向)
     */
    public static final int CANVAS_SIZE_Y = 800;

    /**
     * 点列データのローディングが行われたかどうかを示すフラグ変数
     */
    public static boolean m_fileLoadFlag;

    //ファジネスの大きさを固定にする場合の大きさ
    private final static double NUM_OF_FUZZINESS = 1;


}
