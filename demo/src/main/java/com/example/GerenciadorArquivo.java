package com.example;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivo {
    private static final String DIRETORIO_TEMPORARIO = System.getProperty("java.io.tmpdir");
    private List<File> arquivosTemporarios;
    private int indiceArquivoAtual;

    public GerenciadorArquivo() {
        arquivosTemporarios = new ArrayList<>();
        indiceArquivoAtual = 0;
    }

    public File criarArquivoTemporario() throws IOException {
        File arquivo = File.createTempFile("temp_", ".txt", new File(DIRETORIO_TEMPORARIO));
        arquivosTemporarios.add(arquivo);
        return arquivo;
    }

    public void escreverNoArquivo(File arquivo, String conteudo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write(conteudo);
        }
    }

    public void escreverLinha(File arquivo, String conteudo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.write(conteudo); 
            writer.newLine();       
        }
    }

    public String lerDoArquivo(File arquivo) throws IOException {
        StringBuilder conteudo = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                conteudo.append(linha).append("\n");
            }
        }
        return conteudo.toString();
    }


    public void deletarArquivosTemporarios() {
        for (File arquivo : arquivosTemporarios) {
            if (arquivo.exists()) {
                arquivo.delete();
            }
        }
        arquivosTemporarios.clear();
    }


}
