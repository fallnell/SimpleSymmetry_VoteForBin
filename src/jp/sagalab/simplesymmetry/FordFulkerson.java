/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.util.List;

/**
 * Ford-Fulkerson法のアルゴリズム
 * 修論における参考文献[3]を参考に作成
 * @author sasaki
 */
public class FordFulkerson {

    public static int calculateMaxFlow(int[][] cap, int s, int t) {
        for (int flow = 0;;) {
            int df = findPath(cap, new boolean[cap.length], s, t, Integer.MAX_VALUE);
            if (df == 0) {
                return flow;
            }
            flow += df;
        }
    }

    private static int findPath(int[][] cap, boolean[] vis, int u, int t, int f) {
        if (u == t) {
            return f;
        }
        vis[u] = true;
        for (int v = 0; v < vis.length; v++) {
            if (!vis[v] && cap[u][v] > 0) {
                int df = findPath(cap, vis, v, t, Math.min(f, cap[u][v]));
                if (df > 0) {
                    cap[u][v] -= df;
                    cap[v][u] += df;
                    return df;
                }
            }
        }
        return 0;
    }

    public static int generateMatching(int _n, List<Integer>[] _graphs) {

        return calculateMaxFlow(convertToMatrix(_n, _graphs), 0, 2*_n + 1);
    }

    private static int[][] convertToMatrix(int _n, List<Integer>[] _graphs) {

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

        for (int i = 0; i < _graphs.length; i++) {
            for (int j = 0; j < _graphs[i].size(); j++) {
                capacity[i + 1][_graphs[i].get(j) +_n + 1] =1;
            }
        }

        return capacity;
    }

}
