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


//       String fileName = "2022_12_1_19_52_50_313.csv";
       String fileName = "2023_2_2_19_28_3_355.csv";
//       String fileName = "2022_12_1_20_14_0_278.csv";

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
