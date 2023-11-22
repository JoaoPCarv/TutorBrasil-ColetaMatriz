//Autor: João Pedro L.A. de Carvalho, moderador 'JoaoPCarv' do fórum fórum TutorBrasil.
//Algoritmo criado para o tópico "Matriz" em 17/08/2022."
//Link do tópico: https://www.tutorbrasil.com.br/forum/viewtopic.php?f=3&t=103241
//Última alteração: 14/03/2023."

package br.com.JoaoPCarv.exemplo.tutorbrasil;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//Classe abstrata, cujos todos métodos são estáticos, por se tratar de uma classe puramente funcional,
// sem a necessidade de instanciação.
public abstract class ColetaMatriz {

    //Constante que guarda o caminho do arquivo de relatório.
    private static final String path = "report/Relatorio.txt";

    //Método que retorna a regra, conforme o contexto explicado no enunciado do tópico.
    private static boolean getRule(int regra, int l, int c) throws Exception {

        switch (regra) {

            case 1:
                return (l < c);

            case 2:
                return (l == c);

            case 3:
                return (l + c == 6);

            case 4:
                return (l > c);

            case 5:
                return (l - c > 5);

            default:
                throw new Exception("Argumento inválido.");

        }

    }

    //Método que retorna a matriz com as dimensões requisitadas pelo usuário.
    private static double[][] getMatriz(int nLinhas, int nColunas) throws Exception {

        if (nLinhas <= 0 | nColunas <= 0) throw new Exception("Dimensões inválidas.");

        double[][] matriz = new double[nLinhas][nColunas];

        for (int i = 0; i < nLinhas; i++) {

            for (int j = 0; j < nColunas; j++) matriz[i][j] = Math.random();

        }

        return matriz;
    }

    //Método que retorna as células coletadas através da regra estabelecida
    private static List<Double> getColetados(int regra, int nLinhas, int nColunas, BufferedWriter bWriter) throws Exception {

        List<Double> coletados = new ArrayList<>();

        double[][] matriz = ColetaMatriz.getMatriz(nLinhas, nColunas);

        String temp = "";

        for (int i = 0; i < nLinhas; i++) {

            StringBuilder msgStream = new StringBuilder();
            for (int j = 0; j < nColunas; j++) {

                if (getRule(regra, i, j)) {
                    coletados.add(matriz[i][j]);
                    temp = "Coletado pela regra " + regra + ": elemento A[" + i + "][" + j + "]; \n";
                    msgStream.append(temp);
                    System.out.print(temp);
                }
            }
            bWriter.write(msgStream.toString());
        }

        bWriter.write("\n");
        return coletados;
    }

    //Método que faz a contagem das coletas de elementos da matriz feitos por cada regra.
    public static void comparadorDeColetas(int numeroDeRegras, int nLinhas, int nColunas) throws Exception {

        String path = ColetaMatriz.path;
        int flagRegra = 0;
        int maxSize = 0;
        String thisMsg = "";

        StringBuilder msgStream = new StringBuilder();

        try (BufferedWriter uniqueWriter = ColetaMatriz.getUniqueBufferedWriter(path)) {

            for (int i = 1; i <= numeroDeRegras; i++) {

                List<Double> lista = getColetados(i, nLinhas, nColunas, uniqueWriter);

                if (lista.size() > maxSize) {
                    maxSize = lista.size();
                    flagRegra = i;
                }
                thisMsg = "A regra " + i + " coletou " + lista.size() + " elementos da matriz. \n";
                msgStream.append(thisMsg);
                System.out.println(thisMsg);

            }

            uniqueWriter.write(msgStream.toString());
            String lastMsg = "\nA regra " + flagRegra + " foi quem coletou mais dados.\n";
            System.out.println(lastMsg);
            uniqueWriter.write(lastMsg);
            String endOfFile = "\nExecutado em " + getDataFormatada() + " às " + getHorario() + ".";
            uniqueWriter.write(endOfFile);
        }
    }

    //Método que gerencia a criação e reescrita do arquivo de relatório.
    private static File fileManager(String path) throws IOException {

        File report = new File(path);

        if (!report.exists()) {
            if (!report.createNewFile()) throw new IOException();
        } else {
            String eof = "";
            boolean eofFlag = false;


            try (BufferedReader bSearcher = new BufferedReader(new FileReader(report))) {
                while (!eofFlag) {
                    String temp = bSearcher.readLine();
                    if (temp != null) eof = temp;
                    else eofFlag = true;
                }
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }

            if (eof.startsWith("Executado em")) {
                try (BufferedWriter bEraser = new BufferedWriter(new FileWriter(report))) {
                    bEraser.flush();
                }
            }
        }

        return report;
    }

    //Método que gera a data atual formatada conforme o padrão "dd/MM/yyyy".
    private static String getDataFormatada() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cInstance = Calendar.getInstance();

        return dateFormat.format(cInstance.getTime());

    }

    //Método que retorna o horário local conforme o padrão "hh:MM:ss".
    private static String getHorario() {

        return LocalTime.now().toString().substring(0, 8);

    }

    //Método que gera o escritor universal do relatório, para ser utilizado por todas as requisições de forma unificada.
    private static BufferedWriter getUniqueBufferedWriter(String path) throws IOException {

        return new BufferedWriter(new FileWriter(ColetaMatriz.fileManager(path)));

    }

    //Método executor do programa.
    public static void main(String[] args) {
        try {
            comparadorDeColetas(5, 12, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}