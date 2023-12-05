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
        initialize();

        if(useEmphasis)
            m_centroid = calculateCentroid(_points);
        else
            m_centroid = Point.create(0,0,0);

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
//            Output.writeToCSVPossibility(TreeData.getThisFloor());

//            Output.writeToCSVNecessityNode(TreeData.getThisFloor());
//            Output.writeToCSVNecessityNodeOnlyHold(TreeData.getThisFloor());

//            Output.writeToCSVNecessity(TreeData.getThisFloor());
            Output.writeToCSVNecessityOnlyHold(TreeData.getThisFloor());
        } catch (InterruptedException ex) {
            Logger.getLogger(FuzzySymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }

        //一定以上のグレードを満たす対称軸すべてを返すメソッドへ
        return searchPassingPoints();
    }

    /**
     * ハフ平面への投票結果により導出されたグレードがPASSING_AVERAGEより大きいものを返す
     *
//     * @param array2d ハフ平面
     * @return 得票数が一定より多かった直線
     */
    private static List<SymmetricAxis> searchPassingPoints() {

        List<SymmetricAxis> passedPoints = new ArrayList<>();

        //キャンバスの大きさから距離の取りうる最大値(R)を算出
//        double R = 2 * Math.sqrt(Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_X, 2) + Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_Y, 2));

        //許容範囲定数(PASSING_AVERAGE)を超えるグレードを持つ直線を出力する直線としてリストへ追加
//        for (int i = 0; i < m_NumOfDivisionTheta; i++) {
//            for (int j = 0; j < m_NumOfDivisionRho; j++) {
//                if (PASSING_AVERAGE < array2d[i][j]) {
//                    //(s, t)の情報を実際の角度・距離へと変換したものを追加
//                    passedPoints.add(SymmetricAxis.create(i * (2 * Math.PI / (double) m_NumOfDivisionTheta),
//                            (j - (m_NumOfDivisionRho / 2)) * (R / (double) m_NumOfDivisionRho), 0, 0, 0, null,
//                            SymmetricAxis.setColor(array2d[i][j]), array2d[i][j]));
//                }
//            }
//        }
        List<TreeData.NodeData> leafs = TreeData.getLeafNodes(0);
//        for(TreeData.NodeData leaf : leafs){
//            if(PASSING_AVERAGE < leaf.getPossibility() && !leaf.isHold()){
//                passedPoints.add(SymmetricAxis.create(leaf.getBeginTheta() + leaf.getRangeTheta()/2,
//                    leaf.getBeginRho() + leaf.getRangeRho()/2, 0, 0, 0, null,
//                    SymmetricAxis.setColor(leaf.getPossibility()), leaf.getPossibility(), leaf.getNecessity()));
//            }
//        }

//        passedPoints = SymmetricAxis.sort(passedPoints);

        for(TreeData.NodeData leaf : leafs){
            if(leaf.isHold()){
                passedPoints.add(SymmetricAxis.create(leaf.getBeginTheta() + leaf.getRangeTheta()/2,
                        leaf.getBeginRho() + leaf.getRangeRho()/2,leaf.getRangeTheta(),leaf.getRangeRho(),0,null,
                        Color.hsb(120, 1.0, 1, 1), leaf.getPossibility(), leaf.getNecessity()));
            }
        }


        //高いグレードを得た対称軸を最前面に表示するために，出力する直線をグレードが昇順となるようにソート
//        if (passedPointsSort.size() != 0) {
//            double angle = passedPointsSort.get(passedPointsSort.size() - 1).getAngle();
//            double deltaAngle = passedPointsSort.get(passedPointsSort.size() - 1).getDeltaAngle();
//            double distance = passedPointsSort.get(passedPointsSort.size() - 1).getDistance();
//            double deltaDistance = passedPointsSort.get(passedPointsSort.size() - 1).getDeltaDistance();
//            passedPointsSort.set(passedPointsSort.size() - 1, SymmetricAxis.create(angle, distance, deltaAngle, deltaDistance, 0, null, Color.hsb(240,passedPointsSort.get(passedPointsSort.size() - 1).getGrade(),1,1), passedPointsSort.get(passedPointsSort.size() - 1).getGrade(), passedPointsSort.get(passedPointsSort.size() - 1).getNecessity()));
//        }
//        double maxGrade = passedPointsSort.get(passedPointsSort.size() - 1).getGrade();
//        for(int i = passedPointsSort.size() - 1; i >= 0; --i){
//            if(maxGrade - 0.05 <= passedPointsSort.get(i).getGrade()){
//                passedPointsSort.set(i, SymmetricAxis.create(passedPointsSort.get(i).getAngle(), passedPointsSort.get(i).getDistance(),
//                        passedPointsSort.get(i).getDeltaAngle(), passedPointsSort.get(i).getDeltaDistance(), 0, null,
//                        Color.hsb(240,passedPointsSort.get(i).getGrade(),1,1), passedPointsSort.get(i).getGrade(), passedPointsSort.get(i).getNecessity()));
//            }
//        }
        return passedPoints;
    }

    /**
     * ハフ平面に投票を行うための票数(グレード)を算出
     * 各対称軸に対するペア毎の支持率のハフ投票
     *
     * @param _pair 投票を行う2点のペア
     */
    public static void calculateGrade(Pair _pair) {

        double possibilityTheta;
        double possibilityRho;
        double necessityTheta;
        double necessityRho;

        int index = 0;
        for(TreeData.NodeData parent : m_parentHough){
            for(TreeData.NodeData child : TreeData.getChildren(parent)){
                double beginTheta = child.getBeginTheta() * FuzzySymmetry.MAX_THETA;
                double endTheta = child.getEndTheta() * FuzzySymmetry.MAX_THETA;
                double beginRho = child.getBeginRho() * FuzzySymmetry.R;
                double endRho = child.getEndRho() * FuzzySymmetry.R;

                possibilityTheta = Math.max(_pair.calculatePossibilityTheta(beginTheta, endTheta,true),
                                        _pair.calculatePossibilityTheta(beginTheta, endTheta, false));
                possibilityRho = _pair.calculatePossibilityRho(beginRho, endRho,
                                                    beginTheta, endTheta);
                necessityTheta = Math.max(_pair.calculateNecessityTheta(beginTheta, endTheta,true),
                                            _pair.calculateNecessityTheta(beginTheta, endTheta, false));
                necessityRho = _pair.calculateNecessityRho(beginRho, endRho,
                                                            beginTheta, endTheta);

                addGradeToPairList(index, Math.min(possibilityTheta, possibilityRho), Math.min(necessityTheta, necessityRho), child.getBeginTheta() + child.getRangeTheta()/2, child.getBeginRho() + child.getRangeRho()/2);
                m_childIndex[index] = child.getThisIndex();

                ++index;
            }
        }

//        /*ペア毎の投票結果をCSVファイルとして出力*/
//        try {
//            Output.writeToCSV2(VotesOfPair,_pair.getM_p1(),_pair.getM_p2(), m_houghPlane.size() - 1);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(FuzzySymmetry.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }


    /**
     * 条件を満たす(PASSING_AVERAGEを超える)グレードの情報を
     * ハフ空間への投票候補となるペアリストへと追加する．
     *
     * @param _cnt 参照しているビンのインデックス
     * @param _grade グレード
     */
    private static void addGradeToPairList(int _cnt, double _grade, double _necessity, double _pivotTheta, double _pivotRho) {

        if (m_matchingMode == 0) {
            if (m_pivotI == m_pivotJ) {
                m_houghPlanePossibility[_cnt].add(Vote.create(_grade, m_pivotI, m_pivotI + SimpleSymmetryForPoints.m_numOfPoints, _pivotTheta, _pivotRho));
            } else {
                m_houghPlanePossibility[_cnt].add(Vote.create(_grade, m_pivotI, m_pivotJ, _pivotTheta, _pivotRho));
            }
        } else if (m_matchingMode == 1 || m_matchingMode == 2) {
            m_houghPlanePossibility[_cnt].add(Vote.create(_grade, m_pivotI, m_pivotJ, _pivotTheta, _pivotRho));
        }
        if (m_matchingMode == 0) {
            if (m_pivotI == m_pivotJ) {
                m_houghPlaneNecessity[_cnt].add(Vote.create(_necessity, m_pivotI, m_pivotI + SimpleSymmetryForPoints.m_numOfPoints, _pivotTheta, _pivotRho));
            } else {
                m_houghPlaneNecessity[_cnt].add(Vote.create(_necessity, m_pivotI, m_pivotJ, _pivotTheta, _pivotRho));
            }
        } else if (m_matchingMode == 1 || m_matchingMode == 2) {
            m_houghPlaneNecessity[_cnt].add(Vote.create(_necessity, m_pivotI, m_pivotJ, _pivotTheta, _pivotRho));
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
        for (int i = 0; i < m_houghPlanePossibility.length; ++i) {
            //点群のうち半分以上のペアが許容範囲定数(PASSING_RATE(以上の投票をしていないとマッチング操作へ移らない
            if (m_houghPlanePossibility[i].size() >= SimpleSymmetryForPoints.m_numOfPoints/ 2) {
                //各対称軸に対する支持率に基づくペアの順序付け操作
                //としてペアリストのソーティングを行う
                List<Vote> gradeSorts = Vote.sort(m_houghPlanePossibility[i]);
                List<Vote> necessitySorts = Vote.sort(m_houghPlaneNecessity[i]);
                Vote grade;
                Vote necessity;
                 //どのマッチングを使うかをスイッチ
                if (m_matchingMode == 0) {
                    grade = Vote.decideGrade(gradeSorts);
                    necessity = Vote.decideGrade(necessitySorts);
                    TreeData.setData(m_childIndex[i], grade.getGrade(), necessity.getGrade());
                } else if (m_matchingMode == 1) {
                    grade = Vote.decideGradeUsingBipartiteGraph(gradeSorts);
                    necessity = Vote.decideGradeUsingBipartiteGraph(necessitySorts);
                    TreeData.setData(m_childIndex[i], grade.getGrade(), necessity.getGrade());
                } else if (m_matchingMode == 2) {
                    grade = Vote.decideGradeUsingKuhn(gradeSorts);
                    necessity = Vote.decideGradeUsingKuhn(necessitySorts);
                    TreeData.setData(m_childIndex[i], grade.getGrade(), necessity.getGrade());
                }
            }
        }
        double stop = System.currentTimeMillis();
        System.out.println("マッチングにかかった時間は " + (stop - start) / 1000 + " 秒です。");
    }

    public static Point calculateCentroid(List<? extends Point> _points){
        double centerX = 0.0;
        double centerY = 0.0;
        double centerF = 0.0;
        for(Point p : _points){
            centerX += p.getX();
            centerY += p.getY();
            centerF += p.getF();
        }

        return Point.create(centerX/_points.size(), centerY/_points.size(), centerF/_points.size());
    }

    /**
     * ハフ空間の初期化を行う
     */
    public static void initializeHoughPlane() {
        TreeData.createStartHoughPlane();
    }

    private static void initialize(){

//        m_rangeTheta = 2 * Math.PI / (int)Math.pow(TreeData.THETA_SPLIT_NUM, TreeData.getThisFloor());
//        m_rangeRho = R / (int)Math.pow(TreeData.RHO_SPLIT_NUM, TreeData.getThisFloor());

        m_parentHough = TreeData.getParentHough(0);

        int childNum;
        if(m_parentHough.get(0).getSplitType() == TreeData.SPLIT_TYPE_THETA){
            childNum = m_parentHough.size() * TreeData.THETA_SPLIT_NUM;
        }else if(m_parentHough.get(0).getSplitType() == TreeData.SPLIT_TYPE_RHO){
            childNum = m_parentHough.size() * TreeData.RHO_SPLIT_NUM;
        }else{
            childNum = m_parentHough.size() * TreeData.THETA_SPLIT_NUM * TreeData.RHO_SPLIT_NUM;
        }

        m_childIndex = new int[childNum];

        m_houghPlanePossibility = new ArrayList[childNum];
        m_houghPlaneNecessity = new ArrayList[childNum];
        for (int i = 0; i < m_houghPlanePossibility.length; i++) {
            m_houghPlanePossibility[i] = new ArrayList<>();
            m_houghPlaneNecessity[i] = new ArrayList<>();
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

    public static final double R = Math.sqrt(Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_X, 2) + Math.pow(SimpleSymmetryForPoints.CANVAS_SIZE_Y, 2));

    public static final double MAX_THETA = Math.PI;

//    public static double m_rangeTheta = MAX_THETA;
//
//    public static double m_rangeRho = R;

    /**
     * ハフ空間への投票候補となるグレードのリスト
     */
    public static List<Vote>[] m_houghPlanePossibility;

    public static List<Vote>[] m_houghPlaneNecessity;

    public static List<TreeData.NodeData> m_parentHough;

    public static Point m_centroid;

    public static int[] m_childIndex;

   /**
     * 扱うマッチング手法を設定する 
     * 0 : Edmonds(一般グラフ・バグ有)
     * 1 : ford-fulkerson(二部グラフ・低速・佐々木が実装)
     * 2 : Kuhn's(二部グラフ・比較的高速・ホームページより持ってきたもの)
     */
    private static final int m_matchingMode = 1;

    public static final boolean useEmphasis = false;

}
