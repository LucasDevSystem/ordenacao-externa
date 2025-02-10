package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    static int registros = 200;
    static int caminhos = 10;

    public static void main(String[] args) {
        long inicio = System.nanoTime();

        GerenciadorArquivo gerenciador = new GerenciadorArquivo();

        try {
            // INSTANCIANDO ARQUIVOS TEMPORARIOS
            ArrayList<File> arquivos = new ArrayList<>();
            ArrayList<File> arquivos2 = new ArrayList<>();

            for (int i = 0; i < caminhos; i++) {
                File arquivo = gerenciador.criarArquivoTemporario();
                arquivos.add(arquivo);

            }

            for (int i = 0; i < caminhos; i++) {
                File arquivo = gerenciador.criarArquivoTemporario();
                arquivos2.add(arquivo);

            }
            // POPULANDO METADE DOS ARQUIVOS TEMPORARIOS
            PopularAquivosTemporarios(arquivos, gerenciador, registros);

            int i = 0;

            // INTERCALAÇÃO E ORDENAÇÃO
            for (;;) {
                ArrayList<BufferedReader> leitores = new ArrayList<>();
                int maxBloco = (int) Math.round(Math.pow(2, (i))) * registros;

                // intercala os arquivos com uma parte para escrita e outra leitura
                if (i % 2 == 0) {

                    for (int index = 0; index < caminhos; index++) {
                        leitores.add(new BufferedReader(new FileReader(arquivos.get(index))));
                    }

                    while (true) {
                        boolean finished = false;
                        for (int index = 0; index < caminhos; index++) {
                            finished = intercalar(leitores, maxBloco, gerenciador, arquivos2.get(index));
                        }

                        if (finished) {
                            break;
                        }
                    }

                    if (arquivos2.get(1).length() == 0) {
                        break;
                    }

                    // libera arquivo temporario
                    for (File arquivo : arquivos) {
                        try (FileWriter writer = new FileWriter(arquivo, false)) {
                            writer.write("");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    for (int index = 0; index < caminhos; index++) {
                        leitores.add(new BufferedReader(new FileReader(arquivos2.get(index))));
                    }

                    while (true) {
                        boolean finished = false;
                        for (int index = 0; index < caminhos; index++) {
                            finished = intercalar(leitores, maxBloco, gerenciador, arquivos.get(index));
                        }

                        if (finished) {
                            break;
                        }
                    }
                    if (arquivos.get(1).length() == 0) {
                        break;
                    }

                    // libera arquivo temporario
                    for (File arquivo : arquivos2) {
                        try (FileWriter writer = new FileWriter(arquivo, false)) {
                            writer.write("");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                i++;
            }


            // ANÁLISE DE DESEMPENHO
            long fim = System.nanoTime();
            long tempoExecucao = fim - inicio;

            System.out.println("Tempo de execução: " + (tempoExecucao / 1_000_000.0) + " milissegundos");
            System.out.println(String.format("Tempo de execução: %.2f segundos", tempoExecucao / 1_000_000_000.0));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void PopularAquivosTemporarios(ArrayList<File> arquivos, GerenciadorArquivo gerenciador, int registros) {


        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            int currentLines = 0;
            int intercalacao = 0;
            ArrayList<Double> buffLines = new ArrayList<>();

            // popular os arquivos temporarios
            while ((linha = reader.readLine()) != null) {
                Double numero = Double.parseDouble(linha);

                buffLines.add(numero);

                if (currentLines == (registros - 1)) {
                    buffLines.sort(Comparator.naturalOrder());

                    for (int i = 0; i < arquivos.size(); i++) {
                        if (intercalacao % arquivos.size() == i) {

                            for (Double num : buffLines) {
                                gerenciador.escreverLinha(arquivos.get(i), Double.toString(num));

                            }
                        }
                    }

                    intercalacao++;
                    buffLines.clear();
                    currentLines = 0;

                } else {
                    currentLines++;
                }

            }

            buffLines.sort(Comparator.naturalOrder());
            HeapSort hs = new HeapSort();

            hs.heapSort(buffLines);

            for (int i = 0; i < arquivos.size(); i++) {
                if (intercalacao % arquivos.size() == i) {
                    for (Double num : buffLines) {
                        gerenciador.escreverLinha(arquivos.get(i), Double.toString(num));

                    }
                }
            }

            buffLines.clear();

        } catch (Exception e) {
        }
    }

    public static boolean intercalar(List<BufferedReader> leitores, int maxBloco, GerenciadorArquivo gerenciador, File arquivo) {
        try {

            ArrayList<Double> linhaDeComparacao = new ArrayList<>();
            ArrayList<Integer> limiteLeituraBloco = new ArrayList<>();
            boolean hasValue = false;

            for (BufferedReader leitor : leitores) {
                String linhaArquivo = leitor.readLine();

                if (linhaArquivo != null) {
                    hasValue = true;
                    linhaDeComparacao.add(Double.parseDouble(linhaArquivo));
                } else {
                    linhaDeComparacao.add(Double.MAX_VALUE);
                }

                limiteLeituraBloco.add(maxBloco - 1);
            }

            if (hasValue == false) {
                return true;
            }

            for (int blocoAtual = 0; blocoAtual < (maxBloco * leitores.size()); blocoAtual++) {

                Double menorNumero = Double.MAX_VALUE;
                int indiceMenor = -1;

                for (int i = 0; i < linhaDeComparacao.size(); i++) {
                    if (linhaDeComparacao.get(i) < menorNumero) {
                        menorNumero = linhaDeComparacao.get(i);
                        indiceMenor = i;
                    }
                }

                if (menorNumero != Integer.MAX_VALUE) {
                    gerenciador.escreverLinha(arquivo, Double.toString(menorNumero));
                }

                if (limiteLeituraBloco.get(indiceMenor) != 0) {
                    String novaLinha = leitores.get(indiceMenor).readLine();
                    if (novaLinha != null) {
                        linhaDeComparacao.set(indiceMenor, Double.parseDouble(novaLinha));
                    } else {
                        linhaDeComparacao.set(indiceMenor, Double.MAX_VALUE);
                    }
                    limiteLeituraBloco.set(indiceMenor, limiteLeituraBloco.get(indiceMenor) - 1);
                } else {
                    linhaDeComparacao.set(indiceMenor, Double.MAX_VALUE);
                }
            }

        } catch (Exception e) {
            return true;
        }
        return false;
    }

}
