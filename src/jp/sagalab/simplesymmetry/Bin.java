package jp.sagalab.simplesymmetry;

public class Bin {

    private final double beginTheta;

    private final double endTheta;

    private final double beginRho;

    private final double endRho;

    private double grade;

    public static Bin createBinOnlyTheta(double _beginTheta, double _endTheta){
        return new Bin(_beginTheta, _endTheta, 0.0, 0.0);
    }

    public static Bin creteBinOnlyRho(double _beginRho, double _endRho){
        return new Bin(0.0, 0.0, _beginRho, _endRho);
    }

    public static Bin createBox(double _beginTheta, double _endTheta, double _beginRho, double _endRho){
        return new Bin(_beginTheta, _endTheta, _beginRho, _endRho);
    }

    private Bin(double _beginTheta, double _endTheta, double _beginRho, double _endRho){
        beginTheta = _beginTheta;
        endTheta = _endTheta;
        beginRho = _beginRho;
        endRho = _endRho;
        grade = 0.0;
    }

    public void setGrade(double _grade) {
        grade = _grade;
    }

    public double getRangeTheta(){
        return endTheta - beginTheta;
    }

    public double getRangeRho(){
        return endRho - beginRho;
    }

    public double getPivotTheta(){
        return beginTheta + getRangeTheta()/2;
    }

    public double getPivotRho(){
        return beginRho + getRangeRho()/2;
    }

    public double getBeginTheta(){
        return beginTheta;
    }

    public double getEndTheta(){
        return endTheta;
    }

    public double getBeginRho(){
        return beginRho;
    }

    public double getEndRho(){
        return endRho;
    }

    public double getGrade() {
        return grade;
    }

}
