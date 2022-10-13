/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ファイル入力関連を扱うクラス
 *
 * @author sasaki
 */
public class Input {

    /**
     * 点群データのファイルをロードする
     *
     * @return 点列
     */
    public static List<Point> LoadPoint() {

//        String fileName = "o15to60.csv";
       String fileName = "2022_10_13_22_42_12_985.csv";
//        String fileName = "masterThesisSample1.csv";
//        String fileName = "2017_8_24_14_10_55_887.csv";
//        String fileName = "pointsforplesentation.csv";
//        String fileName = "2017_8_25_12_13_55_228.csv";
//        String fileName = "2018_1_11_13_39_53_68.csv";
//        String fileName = "2018_11_6_11_10_14_348.csv";
//        String fileName = "Hokkaido_fuzzy.csv";
        List<Point> points = new ArrayList<>();
        double x, y, f;

        try {
            File file = new File("files/points/" + fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str;
            while ((str = br.readLine()) != null) {
                String[] pointsElements = str.split(",");
                x = Double.parseDouble(pointsElements[0]);
                y = Double.parseDouble(pointsElements[1]);
                f = Double.parseDouble(pointsElements[2]);
                points.add(Point.create(x, y, f));
            }

            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        return points;
    }

}
