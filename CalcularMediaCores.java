import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class CalcularMediaCores {

    final static int freq = (1) * 3;

    public static void main(String[] args) throws IOException {
        int IMG61[][][] = lerImagem("1961");
        int medias61[][][] = calcularMediasQuadriculados(IMG61);
        int IMG61Med[][][] = criaImagemComMediasQuadriculados(medias61, freq/3);

        int IMG70[][][] = lerImagem("1970");
        int medias70[][][] = calcularMediasQuadriculados(IMG70);
        int IMG70Med[][][] = criaImagemComMediasQuadriculados(medias70, freq/3);

        int IMG80[][][] = lerImagem("1980");
        int medias80[][][] = calcularMediasQuadriculados(IMG80);
        int IMG80Med[][][] = criaImagemComMediasQuadriculados(medias80, freq/3);

        int IMG90[][][] = lerImagem("1990");
        int medias90[][][] = calcularMediasQuadriculados(IMG90);
        int IMG90Med[][][] = criaImagemComMediasQuadriculados(medias90, freq/3);

        int IMG00[][][] = lerImagem("2000");
        int medias00[][][] = calcularMediasQuadriculados(IMG00);
        int IMG00Med[][][] = criaImagemComMediasQuadriculados(medias00, freq/3);

        int IMG10[][][] = lerImagem("2010");
        int medias10[][][] = calcularMediasQuadriculados(IMG10);
        int IMG10Med[][][] = criaImagemComMediasQuadriculados(medias10, freq/3);

        int IMG19[][][] = lerImagem("2019");
        int medias19[][][] = calcularMediasQuadriculados(IMG19);
        int IMG19Med[][][] = criaImagemComMediasQuadriculados(medias19, freq/3);

        int[][][] dif = calcularDiferenca(medias19, medias10);
        salvarImagem(dif, "diferenca1910");

    }

    public static int[][][] calcularDiferenca(int[][][] mediaAnoX, int[][][] mediaAnoY){
        int[][][] mediaFinal = new int[mediaAnoX.length][mediaAnoX[0].length][3];

        for(int i = 0; i < mediaFinal.length; i ++){
            for(int j = 0; j < mediaFinal[0].length; j ++){
                int red = Math.abs(mediaAnoX[i][j][0] - mediaAnoY[i][j][0]);
                int green = Math.abs(mediaAnoX[i][j][1] - mediaAnoY[i][j][1]);
                int blue = Math.abs(mediaAnoX[i][j][2] - mediaAnoY[i][j][2]);
                
                mediaFinal[i][j][0] = red;
                mediaFinal[i][j][1] = green;
                mediaFinal[i][j][2] = blue;

            }
        }

        return mediaFinal;

    }

    public static int[][][] lerImagem(String ano) throws IOException {
        File arquivo = new File("mapa-" + ano + ".jpg");
        BufferedImage imgAux = ImageIO.read(arquivo);
        int linhas = imgAux.getWidth();
        int colunas = imgAux.getHeight();
        int imagem[][][] = new int[linhas][colunas][3];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                int pixel = imgAux.getRGB(i, j);
                Color color = new Color(pixel);

                imagem[i][j][0] = color.getRed();
                imagem[i][j][1] = color.getGreen();
                imagem[i][j][2] = color.getBlue();
            }
        }

        return imagem;
    }

    public static void gerarQuadriculados(int imagem[][][]) {
        for (int k = freq; k < imagem.length; k+=freq) {
            for (int i = 0; i < imagem.length; i++) {
                for (int j = 0; j < imagem[0].length; j++) {
                    if (i == k || j == k) {
                        imagem[i][j][0] = 0;
                        imagem[i][j][1] = 0;
                        imagem[i][j][2] = 0;
                    }
                }
            }
        }
    }

    public static void salvarImagem(int imagem[][][], String nome) throws IOException {
        BufferedImage image = new BufferedImage(imagem.length, imagem[0].length, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < imagem.length; i++) {
            for (int j = 0; j < imagem[0].length; j++) {
                Color cor = new Color(imagem[i][j][0], imagem[i][j][1], imagem[i][j][2]);
                image.setRGB(i, j, cor.getRGB());
            }
        }
        
        File img = new File(nome + ".png");
        ImageIO.write(image, "png", img);
    }

    public static int[] calcularMediaDoQuadriculado(int imagem[][][], int I, int J) {
        int somaR = 0, somaG = 0, somaB = 0, mediaDiv = 0;
        int limI = calcLim(I, imagem.length), limJ = calcLim(J, imagem[0].length);

        for (int i = I; i < limI ; i++) {
            for (int j = J; j < limJ; j++) {
                if (!(verificarBranco(imagem[i][j]) || verificarPreto(imagem[i][j]) || verificarCinza(imagem[i][j]) || verificarCoresMistas(imagem[i][j]))) {
                    mediaDiv++;
                    somaR += imagem[i][j][0];
                    somaG += imagem[i][j][1];
                    somaB += imagem[i][j][2];
                }
            }
        }

        int medias[] = {255, 255, 255};
        if (mediaDiv != 0) {
            medias[0] = Math.round(somaR / mediaDiv);
            medias[1] = Math.round(somaG / mediaDiv);
            medias[2] = Math.round(somaB / mediaDiv);
        }

        return medias;
    }

    public static boolean verificarBranco(int rgb[]) {
        return rgb[0] + rgb[1] + rgb[2] == 765;
    }

    public static boolean verificarPreto(int rgb[]) {
        return rgb[0] + rgb[1] + rgb[2] == 0;
    }

    public static boolean verificarCinza(int rgb[]) {
        return Character.getNumericValue((rgb[0] + "").charAt(0)) == 
                Character.getNumericValue((rgb[1] + "").charAt(0)) &&
                Character.getNumericValue((rgb[0] + "").charAt(0)) ==
                Character.getNumericValue((rgb[2] + "").charAt(0));
    }

    public static boolean verificarCoresMistas(int rgb[]) {
        return rgb[0] < 210 && rgb[1] < 120 && rgb[2] < 100;
    }

    public static int[][][] calcularMediasQuadriculados(int imagem[][][]) {
        int medias[][][] = new int[(int) Math.ceil(imagem.length/(double) freq)][(int) Math.ceil(imagem[0].length/(double) freq)][3];

        for (int i = 0; i < calcLim(i, imagem.length); i+=freq) {
            for (int j = 0; j < calcLim(j, imagem[0].length); j+=freq) {
                medias[i/freq][j/freq] = calcularMediaDoQuadriculado(imagem, i, j);
            }
        }

        return medias;
    }

    public static int[][][] criaImagemComMediasQuadriculados(int medias[][][], int freq){
        int[][][] imagem = new int[medias.length][medias[0].length][3];
        
        for(int i = 0; i < medias.length; i++){
            for(int j = 0; j < medias[0].length; j ++){
                imagem[i][j][0] = medias[i][j][0];
                imagem[i][j][1] = medias[i][j][1];
                imagem[i][j][2] = medias[i][j][2];         
            }
        }


        return imagem;
    }

    public static int calcLim(int x, int length) {
        return Math.min(x + freq, length);
    }

    public static void imprimirMedias(int medias[][][]) {
        for (int i = 0; i < medias.length; i++) {
            for (int j = 0; j < medias[0].length; j++) {
                System.out.printf("Média do quadrante %d %d\n", j + 1, i + 1);
                System.out.printf("R: %d\nG: %d\nB: %d\n", medias[i][j][0], medias[i][j][1], medias[i][j][2]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
