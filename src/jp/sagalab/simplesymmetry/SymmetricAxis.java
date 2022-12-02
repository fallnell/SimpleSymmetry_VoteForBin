/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.util.List;
import javafx.scene.paint.Color;

/**
 * 対称軸をあらわすクラスです．
 *
 * @author sasaki
 */
public class SymmetricAxis {

    /**
     * 対称軸のx軸からの角度を取得
     *
     * @return 対称軸のx軸からの角度
     */
    public double getAngle() {
        return m_angle;
    }

    /**
     * 対称軸の原点からの距離を取得
     *
     * @return 対称軸の原点からの距離
     */
    public double getDistance() {
        return m_distance;
    }

    /**
     * 対称軸の振れ幅となる角度を取得
     *
     * @return 対称軸の振れ幅となる角度
     */
    public double getDeltaAngle() {
        return m_deltaAngle;
    }

    /**
     * 対称軸の原点からの距離の振れ幅を取得
     *
     * @return 対称軸の原点からの距離の振れ幅
     */
    public double getDeltaDistance() {
        return m_deltaDistance;
    }

    /**
     * ２つのファジィ点の中心を取得
     *
     * @return ２つのファジィ点の中心
     */
    public Point getCentralPoint() {
        return m_centralPoint;
    }

    /**
     * ハット形ファジィ点における底面の高さ
     *
     * @return 高さ
     */
    public double getMu() {
        return m_mu;
    }

    /**
     * 軸の色を取得
     *
     * @return 色
     */
    public Color getColor() {
        return m_color;
    }

    /**
     * 軸のグレードを取得
     *
     * @return グレード
     */
    public double getGrade() {
        return m_grade;
    }

    public int numOfCurve() {
        return m_numOfCurve1;
    }

    /**
     * 対称軸候補を生成
     *
     * @param _angle 対称軸のx軸に対する角度
     * @param _distance 対称軸の原点からの距離
     * @param _deltaAngle 対称軸の振れ幅となる角度
     * @param _deltaDistance 対称軸の原点からの距離の振れ幅
     * @param _mu ハット型のファジィ点モデルを採用した際の底面の高さ
     * @param _centralPoint ２つのファジィ点の中心
     * @throws IllegalArgumentException 引数にNaNまたはInfiniteが指定された場合
     * @return インスタンス
     */
    public static SymmetricAxis create(double _angle, double _distance,
            double _deltaAngle, double _deltaDistance, double _mu, Point _centralPoint) {

        if (Double.isNaN(_angle)) {
            throw new IllegalArgumentException("_angle is NaN.");
        }
        if (Double.isInfinite(_angle)) {
            throw new IllegalArgumentException("_angle is Infinite.");
        }
        if (Double.isNaN(_distance)) {
            throw new IllegalArgumentException("_distance is NaN.");
        }
        if (Double.isInfinite(_distance)) {
            throw new IllegalArgumentException("_distance is Infinite.");
        }
        if (Double.isNaN(_deltaAngle)) {
            throw new IllegalArgumentException("_deltaAngle is NaN.");
        }
        if (Double.isInfinite(_deltaAngle)) {
            throw new IllegalArgumentException("_deltaAngle is Infinite.");
        }
        if (Double.isInfinite(_deltaDistance)) {
            throw new IllegalArgumentException("_deltaDistance is Infinite.");
        }
        if (Double.isNaN(_deltaDistance)) {
            throw new IllegalArgumentException("_deltaDistance is NaN.");
        }
        if (Double.isInfinite(_mu)) {
            throw new IllegalArgumentException("_deltaDistance is Infinite.");
        }
        if (Double.isNaN(_mu)) {
            throw new IllegalArgumentException("_deltaDistance is NaN.");
        }

        return new SymmetricAxis(_angle, _distance, _deltaAngle, _deltaDistance, _mu, _centralPoint, Color.rgb(0, 0, 0, 0.5), 0, 0, 0);
    }

    /**
     * 対称軸候補を生成
     *
     * @param _angle 対称軸のx軸に対する角度
     * @param _distance 対称軸の原点からの距離
     * @param _deltaAngle 対称軸の振れ幅となる角度
     * @param _deltaDistance 対称軸の原点からの距離の振れ幅
     * @param _mu ハット型のファジィ点モデルを採用した際の底面の高さ
     * @param _centralPoint ２つのファジィ点の中心
     * @param _numOfCurve1 参照している点の番号
     * @param _numOfCurve2 参照している点の番号その２
     * @throws IllegalArgumentException 引数にNaNまたはInfiniteが指定された場合
     * @return インスタンス
     */
    public static SymmetricAxis create(double _angle, double _distance,
            double _deltaAngle, double _deltaDistance, double _mu, Point _centralPoint, int _numOfCurve1, int _numOfCurve2) {

        if (Double.isNaN(_angle)) {
            throw new IllegalArgumentException("_angle is NaN.");
        }
        if (Double.isInfinite(_angle)) {
            throw new IllegalArgumentException("_angle is Infinite.");
        }
        if (Double.isNaN(_distance)) {
            throw new IllegalArgumentException("_distance is NaN.");
        }
        if (Double.isInfinite(_distance)) {
            throw new IllegalArgumentException("_distance is Infinite.");
        }
        if (Double.isNaN(_deltaAngle)) {
            throw new IllegalArgumentException("_deltaAngle is NaN.");
        }
        if (Double.isInfinite(_deltaAngle)) {
            throw new IllegalArgumentException("_deltaAngle is Infinite.");
        }
        if (Double.isInfinite(_deltaDistance)) {
            throw new IllegalArgumentException("_deltaDistance is Infinite.");
        }
        if (Double.isNaN(_deltaDistance)) {
            throw new IllegalArgumentException("_deltaDistance is NaN.");
        }
        if (Double.isInfinite(_mu)) {
            throw new IllegalArgumentException("_deltaDistance is Infinite.");
        }
        if (Double.isNaN(_mu)) {
            throw new IllegalArgumentException("_deltaDistance is NaN.");
        }

        return new SymmetricAxis(_angle, _distance, _deltaAngle, _deltaDistance,
                _mu, _centralPoint, Color.rgb(0, 0, 0, 0.5), 0, _numOfCurve1, _numOfCurve2);
    }

    /**
     * 対称軸候補を生成
     *
     * @param _angle 対称軸のx軸に対する角度
     * @param _distance 対称軸の原点からの距離
     * @param _deltaAngle 対称軸の振れ幅となる角度
     * @param _deltaDistance 対称軸の原点からの距離の振れ幅
     * @param _mu ハット型のファジィ点モデルを採用した際の底面の高さ
     * @param _centralPoint ２つのファジィ点の中心
     * @param _color 対称軸の色
     * @throws IllegalArgumentException 引数にNaNまたはInfiniteが指定された場合
     * @return インスタンス
     */
    public static SymmetricAxis create(double _angle, double _distance,
            double _deltaAngle, double _deltaDistance, double _mu, Point _centralPoint, Color _color, double _grade) {

        if (Double.isNaN(_angle)) {
            throw new IllegalArgumentException("_angle is NaN.");
        }
        if (Double.isInfinite(_angle)) {
            throw new IllegalArgumentException("_angle is Infinite.");
        }
        if (Double.isNaN(_distance)) {
            throw new IllegalArgumentException("_distance is NaN.");
        }
        if (Double.isInfinite(_distance)) {
            throw new IllegalArgumentException("_distance is Infinite.");
        }
        if (Double.isNaN(_deltaAngle)) {
            throw new IllegalArgumentException("_deltaAngle is NaN.");
        }
        if (Double.isInfinite(_deltaAngle)) {
            throw new IllegalArgumentException("_deltaAngle is Infinite.");
        }
        if (Double.isInfinite(_deltaDistance)) {
            throw new IllegalArgumentException("_deltaDistance is Infinite.");
        }
        if (Double.isNaN(_deltaDistance)) {
            throw new IllegalArgumentException("_deltaDistance is NaN.");
        }
        if (Double.isInfinite(_mu)) {
            throw new IllegalArgumentException("_deltaDistance is Infinite.");
        }
        if (Double.isNaN(_mu)) {
            throw new IllegalArgumentException("_deltaDistance is NaN.");
        }
        if (Double.isNaN(_grade)) {
            throw new IllegalArgumentException("_deltaDistance is NaN.");
        }

        return new SymmetricAxis(_angle, _distance, _deltaAngle, _deltaDistance, _mu, _centralPoint, _color, _grade, 0, 0);
    }

    /**
     * 与えられた軸のリストをグレード順(昇順)にソーティングする
     *
     * @param _axises 対称軸
     * @return ソーティング済の票
     */
    public static List<SymmetricAxis> sort(List<SymmetricAxis> _axises) {
        quick_sort(_axises, 0, _axises.size() - 1);

        return _axises;
    }

    public static Color setColor(double _grade) {

        double alpha = _grade / (1 - FuzzySymmetry.PASSING_AVERAGE)
                - FuzzySymmetry.PASSING_AVERAGE / (1 - FuzzySymmetry.PASSING_AVERAGE);

        if (_grade > 1) {
            _grade = 1.0;
        } else if (_grade < 0) {
            _grade = 0.0;
        }

        if (alpha > 1) {
            alpha = 1.0;
        } else if (alpha < 0) {
            alpha = 0;
        }

        if(_grade == 1.0) {
            return Color.hsb(120, _grade, 1, 0);
        }else{
            return Color.hsb(120, _grade, 1, 1);
        }
    }

    /**
     * 角度の正規化(0≦_angle＜πへと変換)する
     *
     * @param _angle 角度
     * @return 正規化後の角度
     */
    public static double normalizeAngle1(double _angle) {

        if (_angle > Math.PI) {
            return _angle - Math.PI;
        } else if (_angle < 0) {
            return _angle + Math.PI;
        } else {
            return _angle;
        }

    }

    /**
     * 角度の正規化(0≦_angle＜2πへと変換)する
     *
     * @param _angle 角度
     * @return 正規化後の角度
     */
    public static double normalizeAngle2(double _angle) {
        if (_angle > 2 * Math.PI) {
            return _angle - 2 * Math.PI;
        } else if (_angle < 0) {
            return _angle + 2 * Math.PI;
        } else {
            return _angle;
        }
    }

    /**
     * 垂直二等分線とx軸とのなす角をその法線とx軸とのなす角に変換
     *
     * @param _angle 垂直二等分線とx軸のなす角
     * @return 引数の垂直二等分線の法線とx軸がなす角
     */
    public static double convertAngle(double _angle) {

        double theta = normalizeAngle2(_angle);
        double angle;

        if (0 <= _angle && _angle < (Math.PI / 2)) {
            angle = (Math.PI / 2) + theta;
//            angle = ((Math.PI / 2) - SymmetricAxis.normalizeAngle1(theta) + Math.PI);
        } else if ((Math.PI / 2) <= _angle && _angle < Math.PI) {
            angle = theta - (Math.PI / 2);
        } else if (Math.PI <= _angle && _angle < (3 * Math.PI / 2)) {
            angle = Math.PI - ((Math.PI / 2) - normalizeAngle1(theta) + Math.PI);
        } else {
            angle = theta - (Math.PI / 2);
        }

        return angle;
    }

    /**
     * 与えられた対称軸のリストをグレード順(昇順)にクイックソートする
     *
     * @param _axises 対称軸
     * @param left 左側の値
     * @param right 右側の値
     */
    private static void quick_sort(List<SymmetricAxis> _axises, int left, int right) {
        if (left >= right) {
            return;
        }
        double p = _axises.get((left + right) / 2).getGrade();
        int l = left, r = right;
        SymmetricAxis tmp;
        while (l <= r) {
            while (_axises.get(l).getGrade() < p) {
                l++;
            }
            while (_axises.get(r).getGrade() > p) {
                r--;
            }
            if (l <= r) {
                tmp = _axises.get(l);
                _axises.set(l, _axises.get(r));
                _axises.set(r, tmp);
                l++;
                r--;
            }
        }
        quick_sort(_axises, left, r);  // ピボットより左側をクイックソート
        quick_sort(_axises, l, right); // ピボットより右側をクイックソート

    }

    /**
     * コンストラクタ
     *
     * @param _angle 対称軸のx軸に対する角度
     * @param _distance 対称軸の原点からの距離
     * @param _deltaAngle 対称軸の振れ幅となる角度
     * @param _deltaDistance 対称軸の原点からの距離の振れ幅
     * @param _centralPoint ２つのファジィ点の中心
     *
     */
    private SymmetricAxis(double _angle, double _distance,
            double _deltaAngle, double _deltaDistance, double _mu,
            Point _centralPoint, Color _color, double _grade, int _numOfCurve1, int _numOfCurve2) {

        m_angle = _angle;
        m_distance = _distance;
        m_deltaAngle = _deltaAngle;
        m_deltaDistance = _deltaDistance;
        m_centralPoint = _centralPoint;
        m_mu = _mu;
        m_color = _color;
        m_grade = _grade;
        m_numOfCurve1 = _numOfCurve1;
        m_numOfCurve2 = _numOfCurve2;
    }

    /**
     * 対称軸の角度 θ
     */
    private final double m_angle;
    /**
     * 対称軸の原点からの距離 ρ
     */
    private final double m_distance;
    /**
     * 対称軸の歪み幅 ⊿θ
     */
    private final double m_deltaAngle;
    /**
     * 原点からの距離の幅 ⊿ρ
     */
    private final double m_deltaDistance;
    /**
     * ２つのファジィ点の中心
     */
    private final Point m_centralPoint;
    /**
     * ハット型ファジィ点モデルにおける底面の高さ
     */
    private final double m_mu;
    /**
     * 対称軸出力の際の色
     */
    private final Color m_color;
    /**
     * 対称軸のグレード
     */
    private final double m_grade;
    /**
     * 曲線内での点の番号
     */
    private final int m_numOfCurve1;
    private final int m_numOfCurve2;

}
