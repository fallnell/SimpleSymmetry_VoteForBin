/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.util.*;

/**
 *
 * Kuhnのアルゴリズムに基づき，2分グラフの最大マッチング問題を解くクラスです．
 * https://sites.google.com/site/indy256/algo/kuhn_matching2
 * @author sasaki
 */
public class KuhnsAlgorithm {

    public static int[] maxMatching(List<Integer>[] graph, int n2) {
        int n1 = graph.length;
        int[] matching = new int[n2];
        Arrays.fill(matching, -1);
        int matches = 0;
        for (int u = 0; u < n1; u++) {
            if (findPath(graph, u, matching, new boolean[n1])) {
                ++matches;
            }
        }
        return matching;
    }

    static boolean findPath(List<Integer>[] graph, int u1, int[] matching, boolean[] vis) {
        vis[u1] = true;
        for (int v : graph[u1]) {
            int u2 = matching[v];
            if (u2 == -1 || !vis[u2] && findPath(graph, u2, matching, vis)) {
                matching[v] = u1;
                return true;
            }
        }
        return false;
    }

    public static int KuhnsAlgorithm(int _n1, List<Integer>[] _graphs) {

        int n1 = _n1;

        int[] res = maxMatching(_graphs, n1);
//        int res2 = slowMinVertexCover(_graphs, n1);
        int res1 = 0;

        for (int i = 0; i < res.length; i++) {
            if (res[i] == -1) {
                continue;
            }
            res1++;
        }

//        if (res1 != res2) {
//            throw new RuntimeException();
//        }

        return res1;
    }
//  }

    static int slowMinVertexCover(List<Integer>[] g, int n2) {
        int n1 = g.length;
        int[] mask = new int[n1];
        for (int i = 0; i < n1; i++) {
            for (int j : g[i]) {
                mask[i] |= 1 << j;
            }
        }
        int res = n2;
        for (int m = 0; m < 1 << n2; m++) {
            int cur = Integer.bitCount(m);
            for (int i = 0; i < n1; i++) {
                if ((mask[i] & m) != mask[i]) {
                    ++cur;
                }
            }
            res = Math.min(res, cur);
        }
        return res;
    }
}
