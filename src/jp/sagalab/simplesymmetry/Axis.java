package jp.sagalab.simplesymmetry;


import javafx.scene.paint.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 対称軸となる直線を取り扱うクラスです．
 * @author sasaki
 */
public class Axis {

    /**
     * x軸となす角度を返します．
     * @return x軸となす角度
     */
    public double getAngle() {
        return m_angle;
    }

    /**
     * 直線の原点からの距離を返します．
     * @return 直線の原点からの距離
     */
    public double getDistance() {
        return m_distance;
    }

    /**
     * 直線の色を返します．
     * @return 直線の色
     */
    public Color getColor() {
        return m_color;
    }

    /**
     * 軸のグレードを返します．
     * @return 軸のグレード
     */
    public double getGrade() {
        return m_grade;
    }
    
    /**
     * 対称軸となる直線を生成
     * @param _angle x軸となす角度
     * @param _distance 直線の原点からの距離
     * @param _color 直線の色(描画時に使用)
     * @param _grade 軸のグレード
     * @return 対称軸となる直線
     */
    public static Axis create(double _angle, double _distance, Color _color, double _grade){
        
        return new Axis(_angle, _distance, _color, _grade);
        
    }
    
    /**
     * コンストラクタ
     * @param _angle x軸となす角度
     * @param _distance 直線の原点からの距離
     * @param _color 直線の色(描画時に使用)
     * @param _grade 軸のグレード
     */
    private Axis(double _angle, double _distance, Color _color, double _grade){
        
        m_angle = _angle;
        m_distance = _distance;
        m_color = _color;
        m_grade = _grade;
        
    }
    
    final double m_angle;
    final double m_distance;
    final Color m_color;
    final double m_grade;
}
