/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

/**
 * 2点のペアを取り扱うクラスです．
 *
 * @author sasaki
 */
public class Pair {

    /**
     * 2点からなるペアを生成 ※引数の2点の入力順番は処理に影響しません．順不同です．
     *
     * @param _p1 ペアを形成するひとつめの点
     * @param _p2 ペアを形成するふたつ目の点
     * @return インスタンス
     */
    public static Pair create(Point _p1, Point _p2) {

        return new Pair(_p1, _p2);

    }

    /**
     * θ軸上の地点_uにおけるグレードを算出
     *
     * @param _left θ軸上のビンの始点
     * @param _right θ軸上のビンの終点
     * @param _bool trueであれば0~πまで，falseであればπ~2πまで
     * @return グレード
     */
    public double calculateGrade(double _left, double _right, boolean _bool) {

        double leftB;
        double rightB;

        if (_bool) {
            leftB = calculateFuzzyTriangleLeftb(this.calculateRadian());
            rightB = calculateFuzzyTriangleRightb(this.calculateRadian());
        } else {
            leftB = calculateFuzzyTriangleLeftb(this.calculateRadian() + Math.PI);
            rightB = calculateFuzzyTriangleRightb(this.calculateRadian() + Math.PI);
        }

        double gradeFloor = 1 - (this.calculateDistance()) / (this.m_p1.getF() + this.m_p2.getF());

        double gradeMinus2PILeft = Math.max(calculateFuzzyTriangleLeftau(_left - 2 * Math.PI) + leftB,
                calculateFuzzyTriangleLeftau(_right - 2 * Math.PI) + leftB);
        double gradeMinus2PIRight = Math.max(calculateFuzzyTriangleRightau(_left - 2 * Math.PI) + rightB,
                calculateFuzzyTriangleRightau(_right - 2 * Math.PI) + rightB);

        double gradeMinus2PI;

        if (gradeMinus2PILeft < gradeMinus2PIRight) {
            gradeMinus2PI = gradeMinus2PILeft;
        } else {
            gradeMinus2PI = gradeMinus2PIRight;
        }

        if(gradeMinus2PI > 1.0){
            gradeMinus2PI = 1.0;
        }else if(gradeMinus2PI < 0) {
            gradeMinus2PI = 0;
        }

        double gradeLeft = Math.max(calculateFuzzyTriangleLeftau(_left) + leftB,calculateFuzzyTriangleLeftau(_right) + leftB);
        double gradeRight = Math.max(calculateFuzzyTriangleRightau(_left) + rightB,calculateFuzzyTriangleRightau(_right) + rightB);

        double gradeCenter;

        if (gradeLeft < gradeRight) {
            gradeCenter = gradeLeft;
        } else {
            gradeCenter = gradeRight;
        }

        if(gradeCenter > 1.0){
            gradeCenter = 1.0;
        }else if(gradeCenter < 0) {
            gradeCenter = 0;
        }

        double gradePlus2PILeft = Math.max(calculateFuzzyTriangleLeftau(_left + 2 * Math.PI) + leftB,
                calculateFuzzyTriangleLeftau(_right + 2 * Math.PI) + leftB);
        double gradePlus2PIRight = Math.max(calculateFuzzyTriangleRightau(_left + 2 * Math.PI) + rightB,
                calculateFuzzyTriangleRightau(_right + 2 * Math.PI) + rightB);

        double gradePlus2PI;

        if (gradePlus2PILeft < gradePlus2PIRight) {
            gradePlus2PI = gradePlus2PILeft;
        } else {
            gradePlus2PI = gradePlus2PIRight;
        }

        if(gradePlus2PI > 1.0){
            gradePlus2PI = 1.0;
        }else if(gradePlus2PI < 0) {
            gradePlus2PI = 0;
        }

        double grade;

        if (gradePlus2PI > gradeCenter && gradePlus2PI > gradeMinus2PI) {
            grade = gradePlus2PI;
        } else if (gradeMinus2PI > gradeCenter && gradeMinus2PI > gradePlus2PI) {
            grade = gradeMinus2PI;
        } else {
            grade = gradeCenter;
        }

        if (grade < gradeFloor) {
            grade = gradeFloor;
        }


        return grade;


    }

    /**
     * ρ軸上の地点_vにおけるグレードを算出
     *
     * @param _rhoLeft ρ軸上のビンの始点
     * @param _rhoRight ρ軸上のビンの終点
     * @param _thetaLeft θ軸上のビンの始点
     * @param _thetaRight θ軸上のビンの終点
     * @return グレード
     */
    public double calculateGradeRho(double _rhoLeft,double _rhoRight, double _thetaLeft, double _thetaRight) {

        double gradeLeft, gradeRight, grade, leftCenter, rightCenter;

        //θがbinの始点の時の原点からペアの中点までの距離
        leftCenter = this.calculateCenterPoint().getX() * Math.cos(_thetaLeft)
                + this.calculateCenterPoint().getY() * Math.sin(_thetaLeft);

        //θがbinの終点の時の原点からペアの中点までの距離
        rightCenter = this.calculateCenterPoint().getX() * Math.cos(_thetaRight)
                + this.calculateCenterPoint().getY() * Math.sin(_thetaRight);


            //leftCenterとrightCenterがどちらもρ軸のbinの始点より下にある場合
        if(leftCenter < _rhoLeft && rightCenter < _rhoLeft){
            gradeLeft = Math.max(calculateFuzzyRhoTriangleav(_rhoLeft) + 1 - calculateFuzzyRhoTriangleb(_thetaLeft),
                calculateFuzzyRhoTriangleav(_rhoRight) + 1 - calculateFuzzyRhoTriangleb(_thetaLeft));

            gradeRight = Math.max(-calculateFuzzyRhoTriangleav(_rhoLeft) + 1 + calculateFuzzyRhoTriangleb(_thetaLeft),
                -calculateFuzzyRhoTriangleav(_rhoRight) + 1 + calculateFuzzyRhoTriangleb(_thetaLeft));

            grade = Math.min(gradeLeft, gradeRight);

            if (grade < 0) {
                grade = 0;
            }

            if(grade > 1.0){
                grade = 1.0;
            }

            //leftCenterとrightCenterがどちらもρ軸のbinの終点より上にある場合
        }else if(leftCenter > _rhoRight && rightCenter > _rhoRight){
            gradeLeft = Math.max(calculateFuzzyRhoTriangleav(_rhoLeft) + 1 - calculateFuzzyRhoTriangleb(_thetaRight),
                    calculateFuzzyRhoTriangleav(_rhoRight) + 1 - calculateFuzzyRhoTriangleb(_thetaRight));

            gradeRight = Math.max(-calculateFuzzyRhoTriangleav(_rhoLeft) + 1 + calculateFuzzyRhoTriangleb(_thetaRight),
                -calculateFuzzyRhoTriangleav(_rhoRight) + 1 + calculateFuzzyRhoTriangleb(_thetaRight));

            grade = Math.min(gradeLeft, gradeRight);

            if (grade < 0) {
                grade = 0;
            }

            if(grade > 1.0){
                grade = 1.0;
            }

            //ビンの内部に中点がある場合
        }else{
            grade = 1.0;
        }


        return grade;
    }

    public double calculateGradeRho2(double _left,double _right, double _theta) {

        double gradeLeft, gradeRight, grade, leftCenter, rightCenter;



        gradeLeft = Math.max(calculateFuzzyRhoTriangleav(_left) + 1 - calculateFuzzyRhoTriangleb(_theta),
                calculateFuzzyRhoTriangleav(_right) + 1 - calculateFuzzyRhoTriangleb(_theta));

        gradeRight = Math.max(-calculateFuzzyRhoTriangleav(_left) + 1 + calculateFuzzyRhoTriangleb(_theta),
                -calculateFuzzyRhoTriangleav(_right) + 1 + calculateFuzzyRhoTriangleb(_theta));

        if (gradeLeft > gradeRight) {
            grade = gradeRight;
        } else {
            grade = gradeLeft;
        }

        if (grade < 0) {
            grade = 0;
        }

        if(grade > 1.0){
            grade = 1.0;
        }

        return grade;
    }

    /**
     * 2点の中点を返します．
     *
     * @return 2点の中点
     */
    public Point calculateCenterPoint() {

        double x = (m_p1.getX() + m_p2.getX()) / 2;
        double y = (m_p1.getY() + m_p2.getY()) / 2;
        double f = (m_p1.getF() + m_p2.getF()) / 2;

        return Point.create(x, y, f);

    }

    /**
     * 点のペア間の距離を導出します．
     *
     * @return 距離
     */
    public double calculateDistance() {

        return Math.sqrt(Math.pow((m_p1.getX() - m_p2.getX()), 2)
                + Math.pow((m_p1.getY() - m_p2.getY()), 2));

    }

    /**
     * 2点をつないだ直線のx軸とのなす角を算出
     *
     * @return 引数によって与えられた線分とx軸とのなす角の大きさ(単位:radian)
     */
    public double calculateRadian() {
        double radian = Math.atan2(m_p1.getY() - m_p2.getY(), m_p1.getX() - m_p2.getX());

        return normalizeAngle1(radian);
    }

    /**
     * ファジィ角度やファジィ距離で用いる三角ファジィ(ハット型も含む)モデルにおける左側の直線の方程式μ(u) = au + bのauの部分
     *
     * @param _u 関数μ(u)における変数uの値
     * @return μ(u)= au + bにおけるau
     */
    private double calculateFuzzyTriangleLeftau(double _u) {
        //2ファジィ点間の距離
        double l = calculateDistance();
        //分子
        double numerator = 2 * l * _u;
        //分母
        double denominator = Math.PI * (m_p1.getF() + m_p2.getF());

        return (numerator / denominator);
    }

    /**
     * ファジィ角度やファジィ距離で用いる三角ファジィ(ハット型も含む)モデルにおける左側の直線の方程式μ(u) = au + bのbの部分
     *
     * @param _theta 2点の角度
     * @return μ(u)=au + b におけるb
     */
    private double calculateFuzzyTriangleLeftb(double _theta) {

        double l = calculateDistance();
        //分子
        double numerator = Math.PI * (m_p1.getF() + m_p2.getF()) - l * (2 * _theta);
        //分母
        double denominator = Math.PI * (m_p1.getF() + m_p2.getF());

        return (numerator / denominator);
    }

    /**
     * ファジィ角度やファジィ距離で用いる三角ファジィ(ハット型も含む)モデル における右側の直線の方程式μ(u) = au + bのauの部分
     *
     * @param _u 関数μ(u)における変数uの値
     * @return μ(u)= au + bにおけるau
     */
    private double calculateFuzzyTriangleRightau(double _u) {
        //2ファジィ点間の距離
        double l = calculateDistance();
        //分子
        double numerator = -2 * l * _u;
        //分母
        double denominator = Math.PI * (m_p1.getF() + m_p2.getF());

        return (numerator / denominator);
    }

    /**
     * ファジィ角度やファジィ距離で用いる三角ファジィ(ハット型も含む)モデル における右側の直線の方程式μ(u) = au + bのbの部分
     *
     * @param _theta 2点の角度
     * @return μ(u)=au + b におけるb
     */
    private double calculateFuzzyTriangleRightb(double _theta) {

        double l = calculateDistance();

        double numerator = Math.PI * (m_p1.getF() + m_p2.getF()) + l * (2 * _theta);

        double denominator = Math.PI * (m_p1.getF() + m_p2.getF());

        return (numerator / denominator);
    }

    /**
     * ファジィ距離で用いる三角ファジィ(ハット型も含む)モデルにおける左側の直線の方程式μ(v) = av + bのavの部分
     *
     * @param _v 関数μ(v)における変数vの値
     * @return μ(v)= av + bにおけるav
     */
    private double calculateFuzzyRhoTriangleav(double _v) {

        return (1 / this.calculateCenterPoint().getF()) * _v;

    }

    /**
     * ファジィ距離で用いる三角ファジィ(ハット型も含む)モデルにおける左側の直線の方程式μ(v) = av + bのbの部分
     *
     * @param _theta 角度
     * @return μ(v)= av + bにおけるb
     */
    private double calculateFuzzyRhoTriangleb(double _theta) {

        double rho = this.calculateCenterPoint().getX() * Math.cos(_theta)
                + this.calculateCenterPoint().getY() * Math.sin(_theta);

        return (rho / this.calculateCenterPoint().getF());

    }

    /**
     * 角度の正規化(0≦_angle＜πへと変換)する
     *
     * @param _angle 角度
     * @return 正規化後の角度
     */
    private double normalizeAngle1(double _angle) {

        if (_angle > Math.PI) {
            return _angle - Math.PI;
        } else if (_angle < 0) {
            return _angle + Math.PI;
        } else {
            return _angle;
        }

    }

    /**
     * m_p1を返すメソッド
     * @return m_p1
     */
    public Point getM_p1(){
        return m_p1;
    }

    /**
     * m_p2を返すメソッド
     * @return m_p2
     */
    public Point getM_p2(){
        return m_p2;
    }

    /**
     * コンストラクタ ※引数の2点の入力順番は処理に影響しません．順不同です．
     *
     * @param _p1 ペアを形成するひとつめの点
     * @param _p2 ペアを形成するふたつ目の点
     */
    private Pair(Point _p1, Point _p2) {

        m_p1 = _p1;
        m_p2 = _p2;

    }

    //ペアとなる2点
    private final Point m_p1;
    private final Point m_p2;
}
