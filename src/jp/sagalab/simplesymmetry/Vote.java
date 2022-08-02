/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.util.ArrayList;
import java.util.List;

/**
 * 投票時の票に関する情報を持つクラス
 *
 * @author sasaki
 */
public class Vote {

    /**
     * 票を設定する．
     *
     * @param _grade グレード
     * @param _index1 1つ目の点のインデックス
     * @param _index2 2つ目の点のインデックス
     * @return インスタンス
     */
    public static Vote create(double _grade, int _index1, int _index2) {

        return new Vote(_grade, _index1, _index2);

    }

    /**
     * 与えられた票のリストをグレード順(降順)にソーティングする
     *
     * @param _votes 票
     * @return ソーティング済の票
     */
    public static List<Vote> sort(List<Vote> _votes) {
        quick_sort(_votes, 0, _votes.size() - 1);

        return _votes;
    }

    /**
     * ペアリストから最大マッチングを用いて実際に投票する支持率を決定する
     * 一般グラフにおいてEdmondsアルゴリズムを使用した例
     * @param _votes ペアリスト
     * @return 対称軸抽出の条件を満たした直線に追加されたペアリストの要素
     */
    public static Vote decideGrade(List<Vote> _votes) {

        //Edmondsアルゴリズムで使うマッチ配列を用意
        int[] matches = null;

        int n = SimpleSymmetryForPoints.m_numOfPoints;
//        int n = SimpleSymmetryForCurves.m_numOfCurves;

        //自己対称を考えるために点の数の2倍の大きさのリストの配列を準備
        List<Integer>[] g = new List[n * 2];
        for (int i = 0; i < 2 * n; i++) {
            g[i] = new ArrayList<>();
        }

        for (int i = 0; i < _votes.size(); i++) {

            if (n == 1) {
                if (i == 0) {
                    g[_votes.get(i).m_index1].add(_votes.get(i).m_index2);
                    g[_votes.get(i).m_index2].add(_votes.get(i).m_index1);

                    matches = EdmondsAlgorithm.maxMatching(g);

                    if (EdmondsAlgorithm.judgeCompleteMatching(matches) == n) {
                        return _votes.get(i);
                    }

                } else {
                    g[_votes.get(i).m_index1].add(_votes.get(i).m_index2);
                    g[_votes.get(i).m_index2].add(_votes.get(i).m_index1);

                    matches = EdmondsAlgorithm.maxMatching(g);

                    if (EdmondsAlgorithm.judgeCompleteMatching(matches) == n) {
                        return _votes.get(i);
                    }
                }

            } else {

                if (i < n / 2) {
                    g[_votes.get(i).m_index1].add(_votes.get(i).m_index2);
                    g[_votes.get(i).m_index2].add(_votes.get(i).m_index1);
                }

                if (i == (n / 2 - 1)) {

                    matches = EdmondsAlgorithm.maxMatching(g);

                    if (EdmondsAlgorithm.judgeCompleteMatching(matches) == n) {
                        return _votes.get(i);
                    }
                }

                if (i >= n / 2) {
                    g[_votes.get(i).m_index1].add(_votes.get(i).m_index2);
                    g[_votes.get(i).m_index2].add(_votes.get(i).m_index1);

//                    matches = EdmondsAlgorithm.maxMatching(g, matches);
                    matches = EdmondsAlgorithm.maxMatching(g);

                    if (EdmondsAlgorithm.judgeCompleteMatching(matches) == n) {
                        return _votes.get(i);
                    }
                }

            }
        }

        return new Vote(0, 0, 0);

    }

    /**
     * ペアリストから最大マッチングを用いて実際に投票する支持率を決定する
     * 二部グラフにおいてFord-Fulkersonアルゴリズムを使用した例
     * @param _votes ペアリスト
     * @return 対称軸抽出の条件を満たした直線に追加されたペアリストの要素
     */
    public static Vote decideGradeUsingBipartiteGraph(List<Vote> _votes) {

        //入力点の数を格納
        int n = SimpleSymmetryForPoints.m_numOfPoints;

        //マッチングアルゴリズムをかける際に，グラフに追加されている枝の数が
        //点の数以下だと絶対にマッチングが成立しないため，枝の数を記録する変数を用意
        boolean[] addedCounter = new boolean[n];

        int[][] capacity = null;

        for (int i = 0; i < _votes.size(); i++) {

            if (i == 0) {
                capacity = initializeMatrix(n);
            }

            //同じ点に対する投票の時のみ操作を分ける
            if (_votes.get(i).m_index1 == _votes.get(i).m_index2) {
                capacity[_votes.get(i).m_index1 + 1][_votes.get(i).m_index2 + n + 1] = 1;
                addedCounter[_votes.get(i).m_index1] = true;
            } else {
                capacity[_votes.get(i).m_index1 + 1][_votes.get(i).m_index2 + n + 1] = 1;
                capacity[_votes.get(i).m_index2 + 1][_votes.get(i).m_index1 + n + 1] = 1;
                addedCounter[_votes.get(i).m_index1] = true;
                addedCounter[_votes.get(i).m_index2] = true;
            }

            if (countTrue(addedCounter) >= n) {
                int fordMatching = FordFulkerson.calculateMaxFlow(copyMatrix(capacity), 0, 2 * n + 1);

                if (fordMatching == n) {
                    return _votes.get(i);
                }
            }

        }
        return new Vote(0, 0, 0);
    }

    /**
     * ペアリストから最大マッチングを用いて実際に投票する支持率を決定する
     * 二部グラフにおいてKuhnのアルゴリズムを使用した例
     * @param _votes ペアリスト
     * @return 対称軸抽出の条件を満たした直線に追加されたペアリストの要素
     */
    public static Vote decideGradeUsingKuhn(List<Vote> _votes) {

        //入力点の数を格納
        int n = SimpleSymmetryForPoints.m_numOfPoints;

        //マッチングアルゴリズムをかける際に，グラフに追加されている枝の数が
        //点の数以下だと絶対にマッチングが成立しないため，枝の数を記録する変数を用意
        boolean[] addedCounter = new boolean[n];
        int test = 0;

        //Kuhnのアルゴリズムの引数となる2分グラフを用意
        List<Integer>[] graphs = new List[n];
        for (int i = 0; i < n; i++) {
            graphs[i] = new ArrayList<>();
        }

        for (int i = 0; i < _votes.size(); i++) {

            //同じ点に対する投票の時のみ操作を分ける
            if (_votes.get(i).m_index1 == _votes.get(i).m_index2) {
                graphs[_votes.get(i).m_index1].add(_votes.get(i).m_index2);
                addedCounter[_votes.get(i).m_index1] = true;
                test++;
            } else {
                graphs[_votes.get(i).m_index1].add(_votes.get(i).m_index2);
                graphs[_votes.get(i).m_index2].add(_votes.get(i).m_index1);
                addedCounter[_votes.get(i).m_index1] = true;
                addedCounter[_votes.get(i).m_index2] = true;
                test += 2;
            }

            if (countTrue(addedCounter) >= n) {
                if (KuhnsAlgorithm.KuhnsAlgorithm(n, graphs) == n) {
                    return _votes.get(i);
                }
            }

        }

        return new Vote(0, 0, 0);
    }

    /**
     * Ford-Fulkersonアルゴリズムに用いる行列の初期化を行う
     * @param _n 入力点数
     * @return 初期化後の行列
     */
    private static int[][] initializeMatrix(int _n) {

        int[][] capacity = new int[2 * _n + 2][2 * _n + 2];

        for (int i = 0; i < capacity.length; i++) {
            for (int j = 0; j < capacity.length; j++) {
                if ((i == 0 && (j > 0 && j < _n + 1))
                        || (j == capacity.length - 1 && (i > _n && i < 2 * _n + 1))) {
                    capacity[i][j] = 1;
                } else {
                    capacity[i][j] = 0;
                }
            }
        }

        return capacity;

    }

    /**
     * 行列の複製を行う
     * @param _matrix 複製元の行列
     * @return 複製された行列
     */
    private static int[][] copyMatrix(int[][] _matrix) {

        int[][] capacity = new int[_matrix.length][_matrix.length];

        for (int i = 0; i < capacity.length; i++) {
            for (int j = 0; j < capacity.length; j++) {
                capacity[i][j] = _matrix[i][j];
            }
        }

        return capacity;

    }

    /**
     * (処理の高速化を目的として) エッジが追加されているノードの数を返す
     * @param _bools エッジが追加されているノードを表す配列
     * @return エッジが追加されているノードの数
     */
    private static int countTrue(boolean[] _bools) {

        int counter = 0;

        for (int i = 0; i < _bools.length; i++) {
            if (_bools[i]) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * グレードを取得する
     *
     * @return グレード
     */
    public double getGrade() {
        return m_grade;
    }

    /**
     * 1つ目の点のインデックスを取得する
     *
     * @return 1つ目の点のインデックス
     */
    public int getIndex1() {
        return m_index1;
    }

    /**
     * 2つ目の点のインデックスを取得する
     *
     * @return 2つ目の点のインデックス
     */
    public int getIndex2() {
        return m_index2;
    }

    /**
     * 与えられた票のリストをグレード順(降順)にクイックソートする
     *
     * @param _votes 票
     * @param left 左側の値
     * @param right 右側の値
     */
    private static void quick_sort(List<Vote> _votes, int left, int right) {
        if (left >= right) {
            return;
        }
        double p = _votes.get((left + right) / 2).getGrade();
        int l = left, r = right;
        Vote tmp;
        while (l <= r) {
            while (_votes.get(l).getGrade() > p) {
                l++;
            }
            while (_votes.get(r).getGrade() < p) {
                r--;
            }
            if (l <= r) {
                tmp = _votes.get(l);
                _votes.set(l, _votes.get(r));
                _votes.set(r, tmp);
                l++;
                r--;
            }
        }
        quick_sort(_votes, left, r);  // ピボットより左側をクイックソート
        quick_sort(_votes, l, right); // ピボットより右側をクイックソート

    }

    /**
     * コンストラクタ
     *
     * @param _grade グレード
     * @param _index1 1つ目の点のインデックス
     * @param _index2 2つ目の点のインデックス
     */
    private Vote(double _grade, int _index1, int _index2) {

        m_grade = _grade;
        m_index1 = _index1;
        m_index2 = _index2;

    }

    /**
     * グレード
     */
    private final double m_grade;

    /**
     * 1つ目の点のインデックス
     */
    private final int m_index1;

    /**
     * 2つ目の点のインデックス
     */
    private final int m_index2;
    /**
     * 誤差許容定数
     */
    private static final double ALLOWANCE = 1.0;

}
