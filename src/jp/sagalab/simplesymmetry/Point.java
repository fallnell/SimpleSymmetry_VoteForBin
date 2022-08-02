/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

/**
 * 点を取り扱うクラス
 * @author sasaki
 */
public class Point {

    /**
     * X座標値を取得
     *
     * @return X座標値
     */
    public double getX() {
        return m_x;
    }

    /**
     * y座標値を取得
     *
     * @return Y座標値
     */
    public double getY() {
        return m_y;
    }
    
    /**
     * Z座標値を取得
     * @return Z座標値
     */
    public double getZ() {
        return m_z;
    }

    /**
     * ファジネスを取得
     *
     * @return ファジネス
     */
    public double getF() {
        return m_f;
    }
    

    /**
     * Pointを設定する
     *
     * @param _x x座標値
     * @param _y y座標値
     * @param _f ファジネスの値
     * @throws IllegalArgumentException 引数にNaNまたはInfiniteが指定された場合
     * @return インスタンス
     */
    public static Point create(double _x, double _y, double _f) {

        if (Double.isNaN(_x)) {
            throw new IllegalArgumentException("_x is NaN.");
        }
        if (Double.isInfinite(_x)) {
            throw new IllegalArgumentException("_x is Infinite.");
        }
        if (Double.isNaN(_y)) {
            throw new IllegalArgumentException("_y is NaN.");
        }
        if (Double.isInfinite(_y)) {
            throw new IllegalArgumentException("_y is Infinite.");
        }
        if (Double.isNaN(_f)) {
            throw new IllegalArgumentException("_f is NaN.");
        }
        if (Double.isInfinite(_f)) {
            throw new IllegalArgumentException("_f is Infinite.");
        }

        return new Point(_x, _y, 0, _f);

    }
    
    /**
     * 2点間の距離を算出
     * @param _p1 1つ目の点
     * @param _p2 2つ目の点
     * @return 距離
     */
    public static double getDistance(Point _p1, Point _p2){
        
        return Math.sqrt(Math.pow((_p1.getX() - _p2.getX()), 2)
                            + Math.pow((_p1.getY() - _p2.getY()), 2));
        
    }

    /**
     * コンストラクタ
     *
     * @param _x X座標値
     * @param _y Y座標値
     * @param _f ファジネス
     */
    private Point(double _x, double _y, double _z, double _f) {
        m_x = _x;
        m_y = _y;
        m_z = _z;
        m_f = _f;
    }

    /**
     * X座標値
     */
    private final double m_x;
    /**
     * Y座標値
     */
    private final double m_y;
    /**
     * Z座標値
     */
    private final double m_z;
    /**
     * ファジネス値
     */
    private final double m_f;


}
