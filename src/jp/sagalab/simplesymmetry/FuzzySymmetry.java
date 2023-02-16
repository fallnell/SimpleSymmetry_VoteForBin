/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 * 略対称軸の算出を行う
 *
 * @author sasaki
 */
public class FuzzySymmetry {

    /**
     * 略対称軸を算出する
     *
     * @param _points 点列
     * @return 条件をクリアした対称軸
     */
    public static List<SymmetricAxis> generateSymmetricAxis(List<? extends Point> _points) {

        //ハフ空間の初期化
        initializeHoughPlaneCalculate();

        long start = System.currentTimeMillis();

        //2点の組み合わせを取りグレードを算出
        for (int i = 0; i < _points.size(); i++) {
            m_pivotI = i;
            for (int j = i; j < _points.size(); j++) {
                m_pivotJ = j;
                Pair pair = Pair.create(_points.get(i), _points.get(j));
                //ハフ平面への投票操作
                calculateGrade(pair);
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("ペア毎の投票操作の処理時間は" + (end - start) / 1000.0 + "s");

        //算出したグレードを元に投票
        voteHoughSpace();

        //ハフ空間のデータをCSVファイルに出力
        try {
            Output.writeToCSV1(m_houghPlane);
        } catch (InterruptedException ex) {
            Logger.getLogger(FuzzySymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }

        //一定以上のグレードを満たす対称軸すべてを返すメソッドへ
        return searchPassingPoints(m_houghPlane);
    }

    /**
     * ハフ平面への投票結果により導出されたグレードがPASSING_AVERAGEより大きいものを返す
     *
     * @param array2d ハフ平面
     * @return 得票数が一定より多かった直線
     */
    private static List<SymmetricAxis> searchPassingPoints(double[][] array2d) {

        List<SymmetricAxis> passedPoints = new ArrayList<>();

        //キャンバスの大きさから距離の取りうる最大値(R)を算出
        double R = 2 * Math.sqrt(Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_X, 2) + Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_Y, 2));

        //許容範囲定数(PASSING_AVERAGE)を超えるグレードを持つ直線を出力する直線としてリストへ追加
        for (int i = 0; i < NUM_OF_DIVISION_ANGLES; i++) {
            for (int j = 0; j < NUM_OF_DIVISION_PIXELS; j++) {
                if (PASSING_AVERAGE < array2d[i][j]) {
                    //(s, t)の情報を実際の角度・距離へと変換したものを追加
                    passedPoints.add(SymmetricAxis.create(i * (2 * Math.PI / (double) NUM_OF_DIVISION_ANGLES),
                            (j - (NUM_OF_DIVISION_PIXELS / 2)) * (R / (double) NUM_OF_DIVISION_PIXELS), 0, 0, 0, null,
                            SymmetricAxis.setColor(array2d[i][j]), array2d[i][j]));
                }
            }
        }

        //高いグレードを得た対称軸を最前面に表示するために，出力する直線をグレードが昇順となるようにソート
        List<SymmetricAxis> passedPointsSort = SymmetricAxis.sort(passedPoints);
        if (passedPointsSort.size() != 0) {
            double a = passedPointsSort.get(passedPointsSort.size() - 1).getAngle();
            double b = passedPointsSort.get(passedPointsSort.size() - 1).getDistance();
            passedPointsSort.set(passedPointsSort.size() - 1, SymmetricAxis.create(a, b, 0, 0l, 0, null, Color.BLUE, passedPointsSort.get(passedPointsSort.size() - 1).getGrade()));
        }
        return passedPointsSort;
    }

    /**
     * ハフ平面に投票を行うための票数(グレード)を算出
     * 各対称軸に対するペア毎の支持率のハフ投票
     *
     * @param _pair 投票を行う2点のペア
     */
    public static void calculateGrade(Pair _pair) {
        /*ペア毎の投票結果を保存する配列*/
        double[][] VotesOfPair = new double[NUM_OF_DIVISION_ANGLES][NUM_OF_DIVISION_PIXELS];
        /* 角度の刻み幅 */
        double intervalAngle = (Math.PI * 2) / (double) NUM_OF_DIVISION_ANGLES;

        double gradeTheta, gradeRho;

        for (int i = 0; i < NUM_OF_DIVISION_ANGLES; i++) {

            //角度側のグレードとしてθとθ＋πのグレードを導出し高い方をグレードとして採用
            //修論中の(2.3)式の操作を表している
            //ファジィ角度の導出の順序が論文における式と微妙に違うため注意
            gradeTheta = Math.max(_pair.calculateGrade(getBeginTheta(i), getEndTheta(i),true),
                    _pair.calculateGrade(getBeginTheta(i), getEndTheta(i), false));

            /* θに対応するρの値を算出 */
            double D1 = _pair.calculateCenterPoint().getX() * Math.cos(getBeginTheta(i))
                    + _pair.calculateCenterPoint().getY() * Math.sin(getBeginTheta(i));
            double D2 = _pair.calculateCenterPoint().getX() * Math.cos(getEndTheta(i))
                    + _pair.calculateCenterPoint().getY() * Math.sin(getEndTheta(i));

            //距離側のグレード導出
            for (int j = 0; j < NUM_OF_DIVISION_PIXELS; j++) {

                double v1 = getBeginRho(j - NUM_OF_DIVISION_PIXELS / 2);
                double v2 = getEndRho(j - NUM_OF_DIVISION_PIXELS / 2);




                    //直線近似
                    gradeRho = _pair.calculateGradeRho(getBeginRho(j - NUM_OF_DIVISION_PIXELS / 2), getEndRho(j - NUM_OF_DIVISION_PIXELS / 2), getBeginTheta(i), getEndTheta(i));

                    //直線近似なし
//                    gradeRho = _pair.calculateGradeRho2(getBeginRho(j - NUM_OF_DIVISION_PIXELS / 2), getEndRho(j - NUM_OF_DIVISION_PIXELS / 2), i * intervalAngle);

                    //ビンの中心を使った直線近似
//                    gradeRho = _pair.calculateGradeRho3(getBeginRho(j - NUM_OF_DIVISION_PIXELS / 2), getEndRho(j - NUM_OF_DIVISION_PIXELS / 2), (j - NUM_OF_DIVISION_PIXELS / 2) * R / NUM_OF_DIVISION_PIXELS, getBeginTheta(i), getEndTheta(i), i * intervalAngle);

                    //θとρのグレードを比較しそのAND(Minimum)を取り、ハフ投票
                    addGradeToPairList(i, j, Math.min(gradeTheta, gradeRho));

                    /*ペア毎の投票結果を保存*/
                    VotesOfPair[i][j] = Math.min(gradeTheta, gradeRho);
//                }

            }
        }
        /*ペア毎の投票結果をCSVファイルとして出力*/
        try {
            Output.writeToCSV2(VotesOfPair,_pair.getM_p1(),_pair.getM_p2());
        } catch (InterruptedException ex) {
            Logger.getLogger(FuzzySymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static double getBeginTheta(int _num){
        double span = 2 * Math.PI / NUM_OF_DIVISION_ANGLES;
        return _num * span - span / 2;
    }

    public static double getEndTheta(int _num){
        double span = 2 * Math.PI / NUM_OF_DIVISION_ANGLES;
        return _num * span + span / 2;
    }

    public static double getBeginRho(int _num){
        double span = R / NUM_OF_DIVISION_PIXELS;
        return _num * span - span / 2;
    }

    public static double getEndRho(int _num){
        double span = R / NUM_OF_DIVISION_PIXELS;
        return _num * span + span / 2;
    }

    /**
     * ハフ空間の初期化を行う
     */
    public static void initializeHoughPlane() {
        for (int i = 0; i < NUM_OF_DIVISION_ANGLES; i++) {
            for (int j = 0; j < NUM_OF_DIVISION_PIXELS; j++) {
                m_houghPlane[i][j] = 0;
            }
        }
    }

    /**
     * 条件を満たす(PASSING_AVERAGEを超える)グレードの情報を
     * ハフ空間への投票候補となるペアリストへと追加する．
     *
     * @param _i 参照している座標のx座標
     * @param _j 参照している座標のy座標
     * @param _grade グレード
     */
    private static void addGradeToPairList(int _i, int _j, double _grade) {
        if (PASSING_AVERAGE < _grade) {
            if (m_matchingMode == 0) {
                if (m_pivotI == m_pivotJ) {
                    m_houghPlaneCalculate[_i][_j].add(Vote.create(_grade, m_pivotI, m_pivotI + SimpleSymmetryForPoints.m_numOfPoints));
                } else {
                    m_houghPlaneCalculate[_i][_j].add(Vote.create(_grade, m_pivotI, m_pivotJ));
                }
            } else if (m_matchingMode == 1 || m_matchingMode == 2) {
                m_houghPlaneCalculate[_i][_j].add(Vote.create(_grade, m_pivotI, m_pivotJ));
            }

        }
    }

    /**
     * ハフ空間への投票候補となるグレードのリストから投票するグレードを決定する．
     * 
     * 各対称軸に対する支持率に基づくペアの順序付け操作　及び
     * 各対称軸に対するファジィ点群の支持率のハフ投票操作を行うメソッド
     */
    private static void voteHoughSpace() {
        double start = System.currentTimeMillis();
        for (int i = 0; i < NUM_OF_DIVISION_ANGLES; i++) {
            for (int j = 0; j < NUM_OF_DIVISION_PIXELS; j++) {

                //点群のうち半分以上のペアが許容範囲定数(PASSING_RATE(以上の投票をしていないとマッチング操作へ移らない
                if (m_houghPlaneCalculate[i][j].size() >= SimpleSymmetryForPoints.m_numOfPoints / 2) {
                    List<Vote> voteSorts;
                    //各対称軸に対する支持率に基づくペアの順序付け操作
                    //としてペアリストのソーティングを行う
                    voteSorts = Vote.sort(m_houghPlaneCalculate[i][j]);

                    //どのマッチングを使うかをスイッチ
                    if (m_matchingMode == 0) {
                        m_houghPlane[i][j] = Vote.decideGrade(voteSorts).getGrade();
                    } else if (m_matchingMode == 1) {
                        m_houghPlane[i][j] = Vote.decideGradeUsingBipartiteGraph(voteSorts).getGrade();
                    } else if (m_matchingMode == 2) {
                        m_houghPlane[i][j] = Vote.decideGradeUsingKuhn(voteSorts).getGrade();
                    }

                }
            }
        }
        double stop = System.currentTimeMillis();
        System.out.println("マッチングにかかった時間は " + (stop - start) / 1000 + " 秒です。");

    }



    /**
     * ハフ空間への投票候補となるペアリストを初期化する
     */
    private static void initializeHoughPlaneCalculate() {
        m_houghPlaneCalculate = new ArrayList[NUM_OF_DIVISION_ANGLES][NUM_OF_DIVISION_PIXELS];

        for (int i = 0; i < NUM_OF_DIVISION_ANGLES; i++) {
            for (int j = 0; j < NUM_OF_DIVISION_PIXELS; j++) {
                m_houghPlaneCalculate[i][j] = new ArrayList<>();
            }
        }
    }

    /**
     * 現在どの点を参照(投票)しているかを表す
     */
    private static int m_pivotI = 0;
    private static int m_pivotJ = 0;

    /**
     * 許容範囲定数(0から１まで)
     * 
     * 高くすると対称軸が抽出されにくくなり
     * 計算量が削減できる
     */
    public static final double PASSING_AVERAGE = 0;

    /**
     * 距離の最大値
     */

    public static final double R = 2 * Math.sqrt(Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_X, 2) + Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_Y, 2));

    /**
     * ハフ平面におけるθとρの分割数の分割倍率
     */
    public static final int MAGNIFICATION_OF_NUM_OF_DIVISION_PIXELS = 1;
    public static final int MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES = 1;

    /**
     * ハフ平面におけるρの分割数
     * N_ρ
     * 論文中では正部分での分割数を指定していたが，本プログラム中では
     * 正負方向全体の分割数を指定している点に注意
     */

    public static final int NUM_OF_DIVISION_PIXELS = 1600/MAGNIFICATION_OF_NUM_OF_DIVISION_PIXELS;

    /**
     * ハフ平面におけるθの分割数
     * N_θ
     */

    public static final int NUM_OF_DIVISION_ANGLES = 360/MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES;

    /**
     * ハフ平面
     */
    public static double[][] m_houghPlane
            = new double[NUM_OF_DIVISION_ANGLES][NUM_OF_DIVISION_PIXELS];

    /**
     * ハフ空間への投票候補となるグレードのリスト
     */
    public static List<Vote>[][] m_houghPlaneCalculate;

    /**
     * 扱うマッチング手法を設定する 
     * 0 : Edmonds(一般グラフ・バグ有)
     * 1 : ford-fulkerson(二部グラフ・低速・佐々木が実装)
     * 2 : Kuhn's(二部グラフ・比較的高速・ホームページより持ってきたもの)
     */
    private static final int m_matchingMode = 1;

}
