package utils;

public class GenerateMatrices {
    private static double[][] commMatrix, execMatrix;

    public GenerateMatrices() {
        int a=400;
        commMatrix = new double[1000][1000];
        execMatrix = new double[1000][1000];
        
        for (int i=0; i<a;i++) {
            for (int j=0; j<a;j++) {
            	
                commMatrix[i][j] = Math.random()*10 + a;
                execMatrix[i][j] = Math.random()*10 + a;
            }
        }
    }

    public static double[][] getCommMatrix() {
        return commMatrix;
    }

    public static double[][] getExecMatrix() {
        return execMatrix;
    }
}
