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


//       String fileName = "2023_11_20_16_52_26_385.csv";
        String fileName = "test4Point150.csv";
//        String fileName = "2023_10_25_13_43_14_711.csv";

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
