/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.util.Arrays;
import java.util.List;

/**
 * Edmondsのアルゴリズムに基づき，無向グラフの最大マッチング問題を解くクラス
 * ※点の重なりが生じた場合にバグが出る可能性有
 * https://sites.google.com/site/indy256/algo/edmonds_matching
 * @author sasaki
 */
public class EdmondsAlgorithm {

    static int lca(int[] match, int[] base, int[] p, int a, int b) {
        boolean[] used = new boolean[match.length];
        while (true) {
            a = base[a];
            used[a] = true;
            if (match[a] == -1) {
                break;
            }
            a = p[match[a]];
        }
        while (true) {
            b = base[b];
            if (used[b]) {
                return b;
            }
            b = p[match[b]];
        }
    }

    static void markPath(int[] match, int[] base, boolean[] blossom, int[] p, int v, int b, int children) {
        for (; base[v] != b; v = p[match[v]]) {
            blossom[base[v]] = blossom[base[match[v]]] = true;
            p[v] = children;
            children = match[v];
        }
    }

    static int findPath(List<Integer>[] graph, int[] match, int[] p, int root) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        Arrays.fill(p, -1);
        int[] base = new int[n];
        for (int i = 0; i < n; ++i) {
            base[i] = i;
        }

        used[root] = true;
        int qh = 0;
        int qt = 0;
        int[] q = new int[n];
        q[qt++] = root;
        while (qh < qt) {
            int v = q[qh++];

            for (int to : graph[v]) {
                if (base[v] == base[to] || match[v] == to) {
                    continue;
                }
                if (to == root || match[to] != -1 && p[match[to]] != -1) {
                    int curbase = lca(match, base, p, v, to);
                    boolean[] blossom = new boolean[n];
                    markPath(match, base, blossom, p, v, curbase, to);
                    markPath(match, base, blossom, p, to, curbase, v);
                    for (int i = 0; i < n; ++i) {
                        if (blossom[base[i]]) {
                            base[i] = curbase;
                            if (!used[i]) {
                                used[i] = true;
                                q[qt++] = i;
                            }
                        }
                    }
                } else if (p[to] == -1) {
                    p[to] = v;
                    if (match[to] == -1) {
                        return to;
                    }
                    to = match[to];
                    used[to] = true;
                    q[qt++] = to;
                }
            }
        }
        return -1;
    }

    public static int[] maxMatching(List<Integer>[] graph) {
        int n = graph.length;
        int[] match = new int[n];
        Arrays.fill(match, -1);
        int[] p = new int[n];
        for (int i = 0; i < n; ++i) {
            if (match[i] == -1) {
                int v = findPath(graph, match, p, i);
                while (v != -1) {
                    int pv = p[v];
                    int ppv = match[pv];
                    match[v] = pv;
                    match[pv] = v;
                    v = ppv;
                }
            }
        }

//        int matches = 0;
//        for (int i = 0; i < n; ++i) {
//            if (match[i] != -1) {
//                ++matches;
//            }
//        }
        return match;
    }

    public static int[] maxMatching(List<Integer>[] graph, int[] _match) {
        int n = graph.length;
        int[] match = new int[n];
        for (int i = 0; i < _match.length; i++) {
            match[i] = _match[i];
        }
        match[n - 1] = -1;
        int[] p = new int[n];
        for (int i = 0; i < n; ++i) {
            if (match[i] == -1) {
                int v = findPath(graph, match, p, i);
                while (v != -1) {
                    int pv = p[v];
                    int ppv = match[pv];
                    match[v] = pv;
                    match[pv] = v;
                    v = ppv;
                }
            }
        }

//        int matches = 0;
//        for (int i = 0; i < n; ++i) {
//            if (match[i] != -1) {
//                ++matches;
//            }
//        }
        return match;
    }

    /**
     * マッチングに関わった点の数を判定する
     * @param _match マッチング状態を示す配列
     * @return マッチングに関わった点の数
     */
    public static int judgeCompleteMatching(int[] _match) {

        int matches = 0;
        for (int i = 0; i < _match.length / 2; i++) {
            if (_match[i] != -1) {
                matches++;
            }
        }
        
        

        return matches;

    }

}
